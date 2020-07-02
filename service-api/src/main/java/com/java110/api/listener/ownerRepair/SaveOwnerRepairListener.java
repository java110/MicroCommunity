package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeOwnerRepairConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveOwnerRepairListener")
public class SaveOwnerRepairListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "repairObjType", "必填，请填写报修对象类型");
        Assert.hasKeyAndValue(reqJson, "repairObjId", "必填，请填写报修对象ID");
        Assert.hasKeyAndValue(reqJson, "repairObjName", "必填，请填写报修对象名称");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写提交用户ID");
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写提交用户名称");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(reqJson);
        businessOwnerRepair.put("repairId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId));
        businessOwnerRepair.put("state", RepairDto.STATE_WAIT);
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.insert(context, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR);

        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(reqJson, RepairUserPo.class);
        repairUserPo.setContext("订单提交");
        repairUserPo.setPreStaffId("-1");
        repairUserPo.setPreStaffName("-1");
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
        repairUserPo.setStaffId(reqJson.getString("userId"));
        repairUserPo.setStaffName(reqJson.getString("userName"));
        repairUserPo.setRepairId(businessOwnerRepair.getString("repairId"));
        repairUserPo.setState(RepairUserDto.STATE_SUBMIT);
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRuId("-1");
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerRepairConstant.ADD_OWNERREPAIR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
