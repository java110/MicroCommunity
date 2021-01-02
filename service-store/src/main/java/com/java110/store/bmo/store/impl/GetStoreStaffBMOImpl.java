package com.java110.store.bmo.store.impl;

import com.java110.dto.store.StoreUserDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.store.bmo.store.IGetStoreStaffBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> getStoreStaffs(StoreUserDto storeUserDto) {


        int count = storeInnerServiceSMOImpl.getStoreStaffCount(storeUserDto);

        List<StoreUserDto> storeUserDtos = null;
        if (count > 0) {
            storeUserDtos = storeInnerServiceSMOImpl.getStoreStaffs(storeUserDto);
        } else {
            storeUserDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeUserDto.getRow()), count, storeUserDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;

    }
}
