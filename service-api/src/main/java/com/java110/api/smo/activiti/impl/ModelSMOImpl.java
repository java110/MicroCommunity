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
package com.java110.api.smo.activiti.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.dto.app.AppDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.api.smo.activiti.IModelSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @desc add by 吴学文 8:53
 */
@Service
public class ModelSMOImpl extends DefaultAbstractComponentSMO implements IModelSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> getJson(String modelId) {
        IPageData pd = PageData.newInstance().builder("-1", "-1", "", "",
                "", "", "", "", AppDto.WEB_APP_ID);
        ///workflow/getEditorJson
        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, "",
                "/workflow/getEditorJson?modelId=" + modelId,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveModel(WorkflowModelDto workflowModelDto) {
        IPageData pd = PageData.newInstance().builder("-1", "-1", "", "",
                "", "", "", "", AppDto.WEB_APP_ID);
        ///workflow/getEditorJson
        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, JSONObject.toJSONString(workflowModelDto),
                "/workflow/saveModel",
                HttpMethod.POST);

        return responseEntity;

    }

}
