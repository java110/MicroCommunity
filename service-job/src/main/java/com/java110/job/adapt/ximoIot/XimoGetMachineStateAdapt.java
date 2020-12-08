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
import com.java110.core.client.RestTemplate;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @desc add by 吴学文 12:28
 */
@Service("ximoGetMachineStateAdapt")
public class XimoGetMachineStateAdapt extends TaskSystemQuartz {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            queryMachineStatue(taskDto, communityDto);
        }
    }

    private void queryMachineStatue(TaskDto taskDto, CommunityDto communityDto) {

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityDto.getCommunityId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        StringBuilder devSns = new StringBuilder();

        for (MachineDto tmpMachineDto : machineDtos) {
            devSns.append(tmpMachineDto.getMachineCode());
            devSns.append(",");
        }

        String devSnsString = devSns.toString().endsWith(",")
                ? devSns.toString().substring(0, devSns.length() - 2) : devSns.toString();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("devSns", devSnsString);
        postParameters.add("accessToken", GetToken.get(outRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(XimoIotConstant.UPDATE_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        if (responseEntity.getStatusCode() != null) {
            throw new IllegalArgumentException("获取token失败" + responseEntity.getBody());
        }
        JSONObject tokenObj = JSONObject.parseObject(responseEntity.getBody());

        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
            throw new IllegalArgumentException("获取token失败" + responseEntity.getBody());
        }

        JSONArray data = tokenObj.getJSONArray("data");
        MachineDto tMachineDto = null;
        for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {
            for (MachineDto tmpMachineDto : machineDtos) {
                if (!data.getJSONObject(dataIndex).getString("devSn").equals(tmpMachineDto.getMachineCode())) {
                    continue;
                }
                tMachineDto = new MachineDto();
                tMachineDto.setMachineId(tmpMachineDto.getMachineId());
                tMachineDto.setState("1".equals(data.getJSONObject(dataIndex).getString("connectionStatus"))
                        ? MachineDto.MACHINE_STATE_ON : MachineDto.MACHINE_STATE_OFF);
                machineInnerServiceSMOImpl.updateMachineState(tMachineDto);
            }
        }


        logger.debug("调用吸墨信息：" + responseEntity);
    }
}
