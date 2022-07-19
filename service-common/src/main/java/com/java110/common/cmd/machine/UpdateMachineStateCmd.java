package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineV1InnerServiceSMO;
import com.java110.po.machine.MachinePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "machine.updateMachineState")
public class UpdateMachineStateCmd extends Cmd {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineV1InnerServiceSMO machineV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "machineId", "设备ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写设备状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(reqJson.getString("communityId"));
        machineDto.setMachineId(reqJson.getString("machineId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "根据设备编码查询到多条记录，请检查数据");
        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(BeanConvertUtil.beanCovertMap(machineDtos.get(0)));
        businessMachine.put("state", reqJson.getString("state"));
        //计算 应收金额
        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        int flag = machineV1InnerServiceSMOImpl.updateMachine(machinePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }
}
