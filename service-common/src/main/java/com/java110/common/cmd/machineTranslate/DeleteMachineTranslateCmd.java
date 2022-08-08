package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.common.IMachineTranslateV1InnerServiceSMO;
import com.java110.po.machine.MachineTranslatePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "machineTranslate.deleteMachineTranslate")
public class DeleteMachineTranslateCmd extends Cmd {

    @Autowired
    private IMachineTranslateV1InnerServiceSMO machineTranslateV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "machineTranslateId", "同步ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(reqJson, MachineTranslatePo.class);

        int flag = machineTranslateV1InnerServiceSMOImpl.deleteMachineTranslate(machineTranslatePo);

        if(flag <1){
            throw new CmdException("删除失败");
        }
    }
}
