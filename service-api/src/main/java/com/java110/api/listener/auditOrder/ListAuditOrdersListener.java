package com.java110.api.listener.auditOrder;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IResourceEntryStoreInnerServiceSMO;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAuditUserConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.resourceOrder.ApiResourceOrderDataVo;
import com.java110.vo.api.resourceOrder.ApiResourceOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询审核订单侦听类
 */
@Java110Listener("listAuditOrdersListener")
public class ListAuditOrdersListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceEntryStoreInnerServiceSMO resourceEntryStoreInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAuditUserConstant.LIST_AUDITORDERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IResourceEntryStoreInnerServiceSMO getResourceEntryStoreInnerServiceSMOImpl() {
        return resourceEntryStoreInnerServiceSMOImpl;
    }

    public void setResourceEntryStoreInnerServiceSMOImpl(IResourceEntryStoreInnerServiceSMO resourceEntryStoreInnerServiceSMOImpl) {
        this.resourceEntryStoreInnerServiceSMOImpl = resourceEntryStoreInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));

        long count = resourceEntryStoreInnerServiceSMOImpl.getUserTaskCount(auditUser);

        List<ApiResourceOrderDataVo> auditOrders = null;

        if (count > 0) {
            List<PurchaseApplyDto> purchaseApplyDtos = resourceEntryStoreInnerServiceSMOImpl.getUserTasks(auditUser);
            auditOrders = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiResourceOrderDataVo.class);
            for( ApiResourceOrderDataVo apiResourceOrderDataVo : auditOrders){
                switch (apiResourceOrderDataVo.getState()){
                    case "1000": apiResourceOrderDataVo.setStateName("待审核");break;
                    case "1001": apiResourceOrderDataVo.setStateName("审核中");break;
                    case "1002": apiResourceOrderDataVo.setStateName("已审核");break;
                }
                if(apiResourceOrderDataVo.getResOrderType().equals("10000")){
                    apiResourceOrderDataVo.setResOrderTypeName("采购申请");
                }else{
                    apiResourceOrderDataVo.setResOrderTypeName("出库申请");
                }

            }
        } else {
            auditOrders = new ArrayList<>();
        }

        ApiResourceOrderVo apiResourceOrderVo = new ApiResourceOrderVo();

        apiResourceOrderVo.setTotal((int) count);
        apiResourceOrderVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiResourceOrderVo.setResourceOrders(auditOrders);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiResourceOrderVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
