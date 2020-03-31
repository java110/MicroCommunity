package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.auditApplyOrder.IApplyOrderBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.core.smo.purchaseApplyUser.IPurchaseApplyUserInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeComplaintConstant;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 订单审核
 */
@Java110Listener("auditApplyOrderListener")
public class AuditApplyOrderListener extends AbstractServiceApiListener {

    @Autowired
    private IApplyOrderBMO iApplyOrderBMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;


    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyConstant.AUDIT_PURCHASE_APPLY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单号不能为空");
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写批注");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setTaskId(reqJson.getString("taskId"));
        purchaseApplyDto.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyDto.setStoreId(reqJson.getString("storeId"));
        purchaseApplyDto.setAuditCode(reqJson.getString("state"));
        purchaseApplyDto.setAuditMessage(reqJson.getString("remark"));
        purchaseApplyDto.setCurrentUserId(reqJson.getString("userId"));

        boolean isLastTask = purchaseApplyUserInnerServiceSMOImpl.completeTask(purchaseApplyDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        if (isLastTask) {
            context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
            JSONArray businesses = new JSONArray();
            AppService service = event.getAppService();
            businesses.add(updateComplaint(reqJson, context));
            responseEntity = iApplyOrderBMOImpl.callService(context, service.getServiceCode(), businesses);
        }
        context.setResponseEntity(responseEntity);

    }


    /**
     *
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updateComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(paramInJson.getString("storeId"));
        complaintDto.setCommunityId(paramInJson.getString("communityId"));
        complaintDto.setComplaintId(paramInJson.getString("complaintId"));
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);

        Assert.listOnlyOne(purchaseApplyDtos, "存在多条记录，或不存在数据" + complaintDto.getComplaintId());


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PURCHASE_APPLY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(BeanConvertUtil.beanCovertMap(purchaseApplyDtos.get(0)));
        businessComplaint.put("state", "10002");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPurchaseApply", businessComplaint);
        return business;
    }


}
