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
package com.java110.store.bmo.store;

import com.java110.dto.store.StoreUserDto;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName IGetStoreStaffBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2021/1/1 17:37
 * @Version 1.0
 * add by wuxw 2021/1/1
 **/
public interface IGetStoreStaffBMO {

    /**
     * 查询商户员工
     *
     * @param storeUserDto
     * @return
     */
    ResponseEntity<String> getStoreStaffs(StoreUserDto storeUserDto);
}
