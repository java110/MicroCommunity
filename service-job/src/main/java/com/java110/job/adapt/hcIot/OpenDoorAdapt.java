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
package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.entity.order.Business;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 开门适配器
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 15:29
 */
@Component(value = "openDoorAdapt")
public class OpenDoorAdapt extends DatabusAdaptImpl {

    @Autowired
    RestTemplate outRestTemplate;

    @Override
    public void execute(Business business, List<Business> businesses) {

    }

    @Override
    public ResultVo openDoor(JSONObject paramIn) {
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        postParameters.add("extCommunityUuid", paramIn.getString("communityId"));
        //postParameters.add("devSn", machinePo.getMachineCode());
        postParameters.add("devSn", paramIn.getString("machineCode"));
        postParameters.add("empType", paramIn.getString("userType"));
        postParameters.add("empUuid", paramIn.getString("userId"));

        postParameters.add("accessToken", GetToken.get(outRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.OPEN_DOOR, HttpMethod.POST, httpEntity, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"));
    }
}
