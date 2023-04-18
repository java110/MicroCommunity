/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.community.cmd.maintainancePlan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.equipmentAccount.EquipmentAccountDto;
import com.java110.dto.maintainance.MaintainancePlanMachineDto;
import com.java110.intf.common.IEquipmentAccountV1InnerServiceSMO;
import com.java110.intf.community.IMaintainancePlanMachineV1InnerServiceSMO;
import com.java110.po.maintainancePlanMachine.MaintainancePlanMachinePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：maintainancePlanMachine.saveMaintainancePlanMachine
 * 请求路劲：/app/maintainancePlanMachine.SaveMaintainancePlanMachine
 * add by 吴学文 at 2022-11-07 02:22:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "maintainancePlan.saveMaintainancePlanMachine")
public class SaveMaintainancePlanMachineCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMaintainancePlanMachineCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMaintainancePlanMachineV1InnerServiceSMO maintainancePlanMachineV1InnerServiceSMOImpl;

    @Autowired
    private IEquipmentAccountV1InnerServiceSMO equipmentAccountV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "planId", "请求报文中未包含planId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if(!reqJson.containsKey("machines")){
            throw new CmdException("未包含保养设备");
        }

        JSONArray items = reqJson.getJSONArray("machines");

        if(items.size() < 1){
            throw new CmdException("未包含保养设备");
        }
        String machineId = "";
        MaintainancePlanMachineDto maintainancePlanMachineDto = new MaintainancePlanMachineDto();
        maintainancePlanMachineDto.setPlanId(reqJson.getString("planId"));
        List<MaintainancePlanMachineDto> maintainancePlanMachineDtos = null;
        for(int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            machineId = items.getString(itemIndex);
            maintainancePlanMachineDto.setMachineId(machineId);
            maintainancePlanMachineDtos = maintainancePlanMachineV1InnerServiceSMOImpl.queryMaintainancePlanMachines(maintainancePlanMachineDto);

            if (maintainancePlanMachineDtos != null && maintainancePlanMachineDtos.size() >0) {
                throw new CmdException(maintainancePlanMachineDtos.get(0).getMachineName()+"已经添加");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MaintainancePlanMachinePo maintainancePlanMachinePo = null;

        int flag = 0;
        JSONArray machineIds = reqJson.getJSONArray("machines");
        EquipmentAccountDto equipmentAccountDto = null;
        List<EquipmentAccountDto> accountDtos = null;
        for (int machineIndex = 0; machineIndex < machineIds.size(); machineIndex++) {
            maintainancePlanMachinePo = new MaintainancePlanMachinePo();
            maintainancePlanMachinePo.setCommunityId(reqJson.getString("communityId"));
            maintainancePlanMachinePo.setPlanId(reqJson.getString("planId"));
            maintainancePlanMachinePo.setMpmId(GenerateCodeFactory.getGeneratorId("11"));
            maintainancePlanMachinePo.setMachineId(machineIds.getString(machineIndex));

            equipmentAccountDto = new EquipmentAccountDto();
            equipmentAccountDto.setMachineId(machineIds.getString(machineIndex));
            accountDtos = equipmentAccountV1InnerServiceSMOImpl.queryEquipmentAccounts(equipmentAccountDto);
            Assert.listOnlyOne(accountDtos, "设备不存在");

            maintainancePlanMachinePo.setMachineName(accountDtos.get(0).getMachineName());
            flag = maintainancePlanMachineV1InnerServiceSMOImpl.saveMaintainancePlanMachine(maintainancePlanMachinePo);
            if(flag < 1){
                throw new CmdException("未包含保养设备");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
