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

import com.java110.core.client.RestTemplate;
import com.java110.job.adapt.ximoIot.GetToken;
import com.java110.job.adapt.ximoIot.XimoIotConstant;
import com.java110.job.adapt.ximoIot.asyn.IXimoMachineAsyn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * @desc add by 吴学文 11:55
 */
@Service
public class XimoMachineAsynImpl implements IXimoMachineAsyn {
    private static final Logger logger = LoggerFactory.getLogger(XimoMachineAsynImpl.class);


    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    @Async
    public void send(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(outRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(XimoIotConstant.ADD_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    @Async
    public void updateSend(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(outRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(XimoIotConstant.UPDATE_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }

    @Override
    public void deleteSend(MultiValueMap<String, Object> postParameters) {
        postParameters.add("accessToken", GetToken.get(outRestTemplate));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters, httpHeaders);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(XimoIotConstant.DELETE_MACHINE_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("调用吸墨信息：" + responseEntity);
    }
}
