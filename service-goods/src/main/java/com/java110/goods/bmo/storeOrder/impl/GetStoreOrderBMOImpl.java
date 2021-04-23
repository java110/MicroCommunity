package com.java110.goods.bmo.storeOrder.impl;

import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.goods.bmo.storeOrder.IGetStoreOrderBMO;
import com.java110.intf.goods.IStoreOrderInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderBMOImpl")
public class GetStoreOrderBMOImpl implements IGetStoreOrderBMO {

    @Autowired
    private IStoreOrderInnerServiceSMO storeOrderInnerServiceSMOImpl;

    /**
     * @param storeOrderDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderDto storeOrderDto) {


        int count = storeOrderInnerServiceSMOImpl.queryStoreOrdersCount(storeOrderDto);

        List<StoreOrderDto> storeOrderDtos = null;
        if (count > 0) {
            storeOrderDtos = storeOrderInnerServiceSMOImpl.queryStoreOrders(storeOrderDto);
        } else {
            storeOrderDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderDto.getRow()), count, storeOrderDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
