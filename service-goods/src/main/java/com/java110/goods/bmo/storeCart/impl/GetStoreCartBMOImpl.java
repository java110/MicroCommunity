package com.java110.goods.bmo.storeCart.impl;

import com.java110.dto.storeCart.StoreCartDto;
import com.java110.goods.bmo.storeCart.IGetStoreCartBMO;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreCartBMOImpl")
public class GetStoreCartBMOImpl implements IGetStoreCartBMO {

    @Autowired
    private IStoreCartInnerServiceSMO storeCartInnerServiceSMOImpl;

    /**
     * @param storeCartDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreCartDto storeCartDto) {


        int count = storeCartInnerServiceSMOImpl.queryStoreCartsCount(storeCartDto);

        List<StoreCartDto> storeCartDtos = null;
        if (count > 0) {
            storeCartDtos = storeCartInnerServiceSMOImpl.queryStoreCarts(storeCartDto);
        } else {
            storeCartDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeCartDto.getRow()), count, storeCartDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
