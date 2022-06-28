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
package com.java110.job.adapt.hcIot.machine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.machine.MachineDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.machine.MachinePo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 设备同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteMachineToIotAdapt")
public class DeleteMachineToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcMachineAsynImpl;

    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    /**
     * {
     * "extMachineId": "702020042194860037"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessMachines = new JSONArray();
        if (data.containsKey(MachinePo.class.getSimpleName())) {
            Object bObj = data.get(MachinePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessMachines.add(bObj);
            } else if (bObj instanceof List) {
                businessMachines = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessMachines = (JSONArray) bObj;
            }
            //JSONObject businessMachine = data.getJSONObject("businessMachine");

        }else {
            if (data instanceof JSONObject) {
                businessMachines.add(data);
            }
        }
        for (int bMachineIndex = 0; bMachineIndex < businessMachines.size(); bMachineIndex++) {
            JSONObject businessMachine = businessMachines.getJSONObject(bMachineIndex);
            doSendMachine(business, businessMachine);
        }
    }

    private void doSendMachine(Business business, JSONObject businessMachine) {
        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineId(machinePo.getMachineId());
        machineDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        Assert.listOnlyOne(machineDtos, "未找到设备");
        JSONObject postParameters = new JSONObject();
        postParameters.put("extMachineId", machinePo.getMachineId());
        postParameters.put("machineCode", machineDtos.get(0).getMachineCode());
        postParameters.put("machineName", machineDtos.get(0).getMachineName());
        postParameters.put("extCommunityId", machineDtos.get(0).getCommunityId());
        hcMachineAsynImpl.deleteMachine(postParameters);
    }
}
