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

import com.java110.dto.machine.MachineRecordDto;
import com.java110.po.machine.MachineRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 开门记录 存储
 * <p>
 * add by wuxw 2020-12-27
 * <p>
 * 协议：https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 */
public interface ISaveMachineRecordBMO {

    /**
     * 保存开门记录
     *
     * @param machineRecordDto
     * @return
     */
    ResponseEntity<String> saveRecord(MachineRecordDto machineRecordDto);
}
