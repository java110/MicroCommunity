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
package com.java110.job.adapt.ximoIot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.machine.MachineDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.ximoIot.asyn.IXimoMachineAsyn;
import com.java110.po.machine.MachinePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * HC iot 设备同步适配器
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "ximoModifyMachineTransactionIotAdapt")
public class XimoModifyMachineTransactionIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IXimoMachineAsyn ximoMachineAsynImpl;

    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        if (data.containsKey(MachinePo.class.getSimpleName())) {
            Object bObj = data.get(MachinePo.class.getSimpleName());
            JSONArray businessMachines = null;
            if (bObj instanceof JSONObject) {
                businessMachines = new JSONArray();
                businessMachines.add(bObj);
            } else if (bObj instanceof List) {
                businessMachines = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessMachines = (JSONArray) bObj;
            }
            //JSONObject businessMachine = data.getJSONObject("businessMachine");
            for (int bMachineIndex = 0; bMachineIndex < businessMachines.size(); bMachineIndex++) {
                JSONObject businessMachine = businessMachines.getJSONObject(bMachineIndex);
                doSendMachine(business, businessMachine);
            }
        }
    }

    private void doSendMachine(Business business, JSONObject businessMachine) {

        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineId(machinePo.getMachineId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "未找到设备");

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        postParameters.add("extCommunityUuid", machineDtos.get(0));
        //postParameters.add("devSn", machinePo.getMachineCode());
        postParameters.add("uuid", machinePo.getMachineId());
        postParameters.add("name", machinePo.getMachineName());
        postParameters.add("positionType", "0");
        postParameters.add("positionUuid", machinePo.getCommunityId());
        ximoMachineAsynImpl.updateSend(postParameters);
    }
}
