package com.java110.goods.bmo.storeOrderCartReturn.impl;

import com.java110.dto.storeOrderCartReturn.StoreOrderCartReturnDto;
import com.java110.goods.bmo.storeOrderCartReturn.IGetStoreOrderCartReturnBMO;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderCartReturnBMOImpl")
public class GetStoreOrderCartReturnBMOImpl implements IGetStoreOrderCartReturnBMO {

    @Autowired
    private IStoreOrderCartReturnInnerServiceSMO storeOrderCartReturnInnerServiceSMOImpl;

    /**
     * @param storeOrderCartReturnDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderCartReturnDto storeOrderCartReturnDto) {


        int count = storeOrderCartReturnInnerServiceSMOImpl.queryStoreOrderCartReturnsCount(storeOrderCartReturnDto);

        List<StoreOrderCartReturnDto> storeOrderCartReturnDtos = null;
        if (count > 0) {
            storeOrderCartReturnDtos = storeOrderCartReturnInnerServiceSMOImpl.queryStoreOrderCartReturns(storeOrderCartReturnDto);
        } else {
            storeOrderCartReturnDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderCartReturnDto.getRow()), count, storeOrderCartReturnDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
