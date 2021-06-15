package com.java110.user.bmo.owner;/*
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

import com.java110.dto.owner.OwnerDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询商铺出租日志记录
 * <p>
 * add by 吴学文 2021-01-13
 */
public interface IQueryShopsHireLog {

    /**
     * 商铺出租查询
     *
     * @return
     */
    ResponseEntity<String> query(OwnerDto ownerDto);
}
