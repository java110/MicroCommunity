package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "ownerRepair.updateOwnerRepair")
public class UpdateOwnerRepairCmd extends Cmd {

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "报修ID不能为空");
        Assert.hasKeyAndValue(reqJson, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(reqJson, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(reqJson, "repairObjType", "必填，请填写报修对象类型");
        Assert.hasKeyAndValue(reqJson, "repairObjId", "必填，请填写报修对象ID");
        Assert.hasKeyAndValue(reqJson, "repairObjName", "必填，请填写报修对象名称");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写报修内容");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(reqJson);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("删除工单");
        }
    }
}
