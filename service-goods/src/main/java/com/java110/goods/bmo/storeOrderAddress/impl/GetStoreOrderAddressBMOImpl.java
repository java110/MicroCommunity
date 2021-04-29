package com.java110.goods.bmo.storeOrderAddress.impl;

import com.java110.dto.storeOrderAddress.StoreOrderAddressDto;
import com.java110.goods.bmo.storeOrderAddress.IGetStoreOrderAddressBMO;
import com.java110.intf.goods.IStoreOrderAddressInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderAddressBMOImpl")
public class GetStoreOrderAddressBMOImpl implements IGetStoreOrderAddressBMO {

    @Autowired
    private IStoreOrderAddressInnerServiceSMO storeOrderAddressInnerServiceSMOImpl;

    /**
     * @param storeOrderAddressDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderAddressDto storeOrderAddressDto) {


        int count = storeOrderAddressInnerServiceSMOImpl.queryStoreOrderAddresssCount(storeOrderAddressDto);

        List<StoreOrderAddressDto> storeOrderAddressDtos = null;
        if (count > 0) {
            storeOrderAddressDtos = storeOrderAddressInnerServiceSMOImpl.queryStoreOrderAddresss(storeOrderAddressDto);
        } else {
            storeOrderAddressDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderAddressDto.getRow()), count, storeOrderAddressDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
