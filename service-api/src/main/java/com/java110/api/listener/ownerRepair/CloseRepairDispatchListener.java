package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.repair.IRepairInnerServiceSMO;
import com.java110.core.smo.repair.IRepairUserInnerServiceSMO;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("closeRepairDispatchListener")
public class CloseRepairDispatchListener extends AbstractServiceApiPlusListener {

    private static Logger logger = LoggerFactory.getLogger(CloseRepairDispatchListener.class);

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;


    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");


        Assert.hasKeyAndValue(reqJson, "state", "未包含处理信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含处理内容");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含员工ID");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        //添加派单员工关联关系
        ownerRepairBMOImpl.modifyBusinessRepairUser(reqJson, context);

        //修改报修单状态
        ownerRepairBMOImpl.modifyBusinessRepair(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.CLOSE_REPAIR_DISPATCH;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }

    public IRepairUserInnerServiceSMO getRepairUserInnerServiceSMOImpl() {
        return repairUserInnerServiceSMOImpl;
    }

    public void setRepairUserInnerServiceSMOImpl(IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl) {
        this.repairUserInnerServiceSMOImpl = repairUserInnerServiceSMOImpl;
    }
}
