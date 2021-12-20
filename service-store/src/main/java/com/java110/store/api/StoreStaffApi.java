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
package com.java110.store.api;

import com.java110.dto.store.StoreDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.store.bmo.store.IGetStoreStaffBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户员工相关服务接口类
 * add by 吴学文 2021-01-01
 **/
@RestController
@RequestMapping(value = "/storeStaff")
public class StoreStaffApi {

    @Autowired
    private IGetStoreStaffBMO getStoreStaffBMOImpl;

    /**
     * 查询商户商户员工
     *
     * @param
     * @return
     * @serviceCode /storeStaff/getPropertyStaffs
     * @path /app/storeStaff/getPropertyStaffs
     */
    @RequestMapping(value = "/getPropertyStaffs", method = RequestMethod.GET)
    public ResponseEntity<String> getPropertyStaffs(@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "staffName", required = false) String staffName,
                                                    @RequestParam(value = "shareId", required = false) String tel,
                                                    @RequestParam(value = "storeTypeCd", required = false) String storeTypeCd,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row,
                                                    @RequestParam(value = "storeId", required = false) String storeId,
                                                    @RequestParam(value = "relCd", required = false) String relCd
    ) {
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setPage(page);
        storeUserDto.setRow(row);
        storeUserDto.setStoreTypeCd(storeTypeCd);//StoreDto.STORE_TYPE_PROPERTY
        storeUserDto.setStoreId(storeId);
        storeUserDto.setRelCd(relCd);
        storeUserDto.setName(name);
        storeUserDto.setStaffName(staffName);
        storeUserDto.setTel(tel);
        return getStoreStaffBMOImpl.getStoreStaffs(storeUserDto);
    }

}
