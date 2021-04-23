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
package com.java110.common.bmo.machine.impl;

import com.java110.common.bmo.machine.IMachineHeartbeatBMO;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备心跳实现类
 *
 * @desc add by 吴学文 17:37
 */
@Service
public class MachineHeartbeatBMOImpl implements IMachineHeartbeatBMO {


    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMO;

    @Override
    public ResponseEntity<String> heartbeat(MachineDto machineDto) {

        MachineDto tmpMachineDto = new MachineDto();
        tmpMachineDto.setMachineCode(machineDto.getMachineCode());
        tmpMachineDto.setCommunityId(machineDto.getCommunityId());
        List<MachineDto> machineDtos = machineInnerServiceSMO.queryMachines(tmpMachineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

        machineDto.setMachineId(machineDtos.get(0).getMachineId());
        machineDto.setMachineCode("");

        int count = machineInnerServiceSMO.updateMachineState(machineDto);
        if (count > 0) {
            return ResultVo.success();
        }
        return ResultVo.error("上传记录失败");
    }
}
