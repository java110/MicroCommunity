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
package com.java110.job.adapt.hcIot.asyn;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface IIotSendAsyn {

    /**
     * 添加小区信息
     *
     * @param postParameters
     */
    public void addCommunity(JSONObject postParameters);

    public void addMachine(MultiValueMap<String, Object> postParameters, List<MultiValueMap<String, Object>> ownerDtos);

    void updateSend(MultiValueMap<String, Object> postParameters);

    void deleteSend(MultiValueMap<String, Object> postParameters);

    public void sendOwner(MultiValueMap<String, Object> postParameters);

    /**
     * 修改业主
     *
     * @param postParameters
     */
    void sendUpdateOwner(MultiValueMap<String, Object> postParameters);

    /**
     * 删除业主
     *
     * @param postParameters
     */
    void sendDeleteOwner(MultiValueMap<String, Object> postParameters);
}
