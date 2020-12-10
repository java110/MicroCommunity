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
package com.java110.job.adapt.ximoIot.asyn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IMachineAttrInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.job.adapt.ximoIot.GetToken;
import com.java110.job.adapt.ximoIot.XimoIotConstant;
import com.java110.job.adapt.ximoIot.asyn.IXimoMachineAsyn;
import com.java110.po.machine.MachineAttrPo;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @desc add by 吴学文 11:55
 */
@Service
public class XimoMachineAsynImpl implements IXimoMachineAsyn {
    private static final Logger logger = LoggerFactory.getLogger(XimoMachineAsynImpl.class);


    @Autowired
    private RestTemplate formRestTemplate;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineAttrInnerServiceSMO machineAttrInnerServiceSMOImpl;

    @Override
    @Async
    public void send(MultiValueMap<String, Object> postParameters, List<MultiValueMap<String, Object>> ownerDtos) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.ADD_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }
        JSONObject tokenObj = JSONObject.parseObject(responseEntity.getBody());

        if (!tokenObj.containsKey("code") || ResultVo.CODE_OK != tokenObj.getInteger("code")) {
            return;
        }

        JSONObject data = tokenObj.getJSONObject("data");
        String devMac = data.getString("devMac");
        String appEkey = data.getString("appEkey");

        MachineDto machinePo = new MachineDto();
        machinePo.setMachineId(data.getString("uuid"));
        machinePo.setState("1700");
        machinePo.setMachineMac(devMac);
        machineInnerServiceSMOImpl.updateMachineState(machinePo);

        MachineAttrPo machineAttrPo = new MachineAttrPo();
        machineAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        machineAttrPo.setCommunityId(postParameters.get("extCommunityUuid").get(0).toString());
        machineAttrPo.setSpecCd("7127015495");
        machineAttrPo.setMachineId(data.getString("uuid"));
        machineAttrPo.setValue(appEkey);
        machineAttrInnerServiceSMOImpl.saveMachineAttrs(machineAttrPo);

        for (MultiValueMap<String, Object> owner : ownerDtos) {
            sendOwner(owner);
        }
    }

    @Override
    @Async
    public void updateSend(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.UPDATE_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    public void deleteSend(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.DELETE_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    public void sendOwner(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.ADD_OWNER, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    public void sendUpdateOwner(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.EDIT_OWNER, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    public void sendDeleteOwner(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(formRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = formRestTemplate.exchange(XimoIotConstant.DELETE_OWNER, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }
}
