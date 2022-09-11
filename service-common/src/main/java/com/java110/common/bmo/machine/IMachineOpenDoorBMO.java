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
package com.java110.common.bmo.machine;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IMachineOpenDoorBMO {

    /**
     * 开门接口类
     *
     * @param reqJson 请求报文信息
     * @return
     */
    ResponseEntity<String> openDoor(JSONObject reqJson);
    /**
     * 开门接口类
     *
     * @param reqJson 请求报文信息
     * @return
     */
    ResponseEntity<String> closeDoor(JSONObject reqJson);

    /**
     * 开门接口类
     *
     * @param reqJson 请求报文信息
     * @return
     */
    ResponseEntity<String> restartMachine(JSONObject reqJson);

    /**
     * 开门接口类
     *
     * @param reqJson 请求报文信息
     * @return
     */
    ResponseEntity<String> resendIot(JSONObject reqJson);

    /**
     * 获取二维码
     * @param reqJson
     * @return
     */
    ResponseEntity<String> getQRcode(JSONObject reqJson);

    /**
     * 手工进出场
     * @param reqJson
     * @return
     */
    ResponseEntity<String> customCarInOut(JSONObject reqJson);

    /**
     * 查询手工开闸记录
     * @param reqJson
     * @return
     */
    ResponseEntity<String> getManualOpenDoorLogs(JSONObject reqJson);
}
