package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMachineTranslateV1InnerServiceSMO;
import com.java110.po.machine.MachineTranslatePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "machineTranslate.updateMachineTranslate")
public class UpdateMachineTranslateCmd extends Cmd {

    @Autowired
    private IMachineTranslateV1InnerServiceSMO machineTranslateV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "machineTranslateId", "同步ID不能为空");
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择对象类型");
        Assert.hasKeyAndValue(reqJson, "objName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(reqJson, "objId", "必填，请填写对象Id");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请选择状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(reqJson, MachineTranslatePo.class);
        int flag = machineTranslateV1InnerServiceSMOImpl.updateMachineTranslate(machineTranslatePo);

        if (flag < 1) {
            throw new CmdException("修改数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
