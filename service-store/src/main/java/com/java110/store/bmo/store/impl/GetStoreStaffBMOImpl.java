package com.java110.store.bmo.store.impl;

import com.java110.dto.store.StoreUserDto;
import com.java110.store.bmo.store.IGetStoreStaffBMO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @ClassName GetStoreStaffBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2021/1/1 17:41
 * @Version 1.0
 * add by wuxw 2021/1/1
 **/
@Service(value = "getStoreStaffBMOImpl")
public class GetStoreStaffBMOImpl implements IGetStoreStaffBMO {
    @Override
    public ResponseEntity<String> getStoreStaffs(StoreUserDto storeUserDto) {
        return null;
    }
}
