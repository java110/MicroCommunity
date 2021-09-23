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
package com.java110.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.oaWorkflow.StencilsetJson;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.api.smo.activiti.IModelSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @desc add by 吴学文 8:38
 */
@RestController
@RequestMapping(value = "/app/activiti")
public class ActivitiController {

    @Autowired
    private IModelSMO modelSMOImpl;

    /**
     * 查询工作流json
     *
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryJson(@PathVariable String modelId) {

        JSONObject paramOut = JSONObject.parseObject(modelSMOImpl.getJson(modelId).getBody());

        return paramOut.getJSONObject("data").toJSONString();
    }

    /**
     * 查询工作流json
     *
     * @param version
     * @return
     */
    @RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String stencilset(@RequestParam(value = "version", required = false) String version) {

        return StencilsetJson.JSON;
    }

    /**
     * 更新流程
     *
     * @param modelId     模型ID
     * @param xmlJson        流程模型名称
     */
    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.POST)
    public ResponseEntity<String> saveModel(@PathVariable String modelId
            , @RequestBody String xmlJson) {
        JSONObject paramJson = JSONObject.parseObject(xmlJson);
        WorkflowModelDto workflowModelDto = new WorkflowModelDto();
        workflowModelDto.setJson_xml(paramJson.getString("xml"));
        workflowModelDto.setSvg_xml(paramJson.getString("svg"));
        workflowModelDto.setModelId(modelId);
        return modelSMOImpl.saveModel(workflowModelDto);
    }
}
