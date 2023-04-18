package com.java110.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineAuthDto;
import com.java110.intf.common.IMachineAuthInnerServiceSMO;
import com.java110.po.machineAuth.MachineAuthPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "machineAuth.deleteMachineAuth")
public class DeleteMachineAuth extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "authId", "请求报文中未包含授权ID");
        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthId(reqJson.getString("authId"));
        List<MachineAuthDto> machineAuthDtos = machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto);
        Assert.listOnlyOne(machineAuthDtos, "查询员工门禁授权错误！");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(reqJson, MachineAuthPo.class);
        int flag = machineAuthInnerServiceSMOImpl.deleteMachineAuth(machineAuthPo);
        if (flag < 1) {
            throw new CmdException("删除员工门禁授权失败");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
