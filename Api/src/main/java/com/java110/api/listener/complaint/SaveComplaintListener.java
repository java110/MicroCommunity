package com.java110.api.listener.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.complaintUser.IComplaintUserInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeComplaintConstant;


import com.java110.core.annotation.Java110Listener;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveComplaintListener")
public class SaveComplaintListener extends AbstractServiceApiListener {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择投诉类型");
        Assert.hasKeyAndValue(reqJson, "roomId", "必填，请选择房屋编号");
        Assert.hasKeyAndValue(reqJson, "complaintName", "必填，请填写投诉人");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写投诉电话");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
        //Assert.hasKeyAndValue(reqJson, "state", "必填，请填写投诉状态");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写投诉内容");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(addComplaint(reqJson, context));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

        if(HttpStatus.OK == responseEntity.getStatusCode()){
            ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);
            complaintDto.setCurrentUserId(reqJson.getString("userId"));
            complaintUserInnerServiceSMOImpl.startProcess(complaintDto);
        }

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeComplaintConstant.ADD_COMPLAINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("complaintId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_complaintId));
        paramInJson.put("state", "10001");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMPLAINT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(paramInJson);
        //businessComplaint.put("complaintId", "-1");
        //businessComplaint.put("state", "10001");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessComplaint", businessComplaint);
        return business;
    }

    public IComplaintUserInnerServiceSMO getComplaintUserInnerServiceSMOImpl() {
        return complaintUserInnerServiceSMOImpl;
    }

    public void setComplaintUserInnerServiceSMOImpl(IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl) {
        this.complaintUserInnerServiceSMOImpl = complaintUserInnerServiceSMOImpl;
    }
}
