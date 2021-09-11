package com.java110.api.smo.activiti;/*
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

import com.java110.dto.workflow.WorkflowModelDto;
import org.springframework.http.ResponseEntity;

public interface IModelSMO {

    /**
     * 获取json 信息
     *
     * @param modelId
     * @return
     */
    ResponseEntity<String> getJson(String modelId);

    ResponseEntity<String> saveModel(WorkflowModelDto workflowModelDto);
}
