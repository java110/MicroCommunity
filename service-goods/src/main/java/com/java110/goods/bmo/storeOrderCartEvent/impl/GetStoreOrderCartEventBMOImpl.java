package com.java110.goods.bmo.storeOrderCartEvent.impl;

import com.java110.dto.storeOrderCartEvent.StoreOrderCartEventDto;
import com.java110.goods.bmo.storeOrderCartEvent.IGetStoreOrderCartEventBMO;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderCartEventBMOImpl")
public class GetStoreOrderCartEventBMOImpl implements IGetStoreOrderCartEventBMO {

    @Autowired
    private IStoreOrderCartEventInnerServiceSMO storeOrderCartEventInnerServiceSMOImpl;

    /**
     * @param storeOrderCartEventDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderCartEventDto storeOrderCartEventDto) {


        int count = storeOrderCartEventInnerServiceSMOImpl.queryStoreOrderCartEventsCount(storeOrderCartEventDto);

        List<StoreOrderCartEventDto> storeOrderCartEventDtos = null;
        if (count > 0) {
            storeOrderCartEventDtos = storeOrderCartEventInnerServiceSMOImpl.queryStoreOrderCartEvents(storeOrderCartEventDto);
        } else {
            storeOrderCartEventDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderCartEventDto.getRow()), count, storeOrderCartEventDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
