package com.java110.goods.bmo.storeOrderCartReturnEvent.impl;

import com.java110.dto.storeOrderCartReturnEvent.StoreOrderCartReturnEventDto;
import com.java110.goods.bmo.storeOrderCartReturnEvent.IGetStoreOrderCartReturnEventBMO;
import com.java110.intf.goods.IStoreOrderCartReturnEventInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreOrderCartReturnEventBMOImpl")
public class GetStoreOrderCartReturnEventBMOImpl implements IGetStoreOrderCartReturnEventBMO {

    @Autowired
    private IStoreOrderCartReturnEventInnerServiceSMO storeOrderCartReturnEventInnerServiceSMOImpl;

    /**
     * @param storeOrderCartReturnEventDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreOrderCartReturnEventDto storeOrderCartReturnEventDto) {


        int count = storeOrderCartReturnEventInnerServiceSMOImpl.queryStoreOrderCartReturnEventsCount(storeOrderCartReturnEventDto);

        List<StoreOrderCartReturnEventDto> storeOrderCartReturnEventDtos = null;
        if (count > 0) {
            storeOrderCartReturnEventDtos = storeOrderCartReturnEventInnerServiceSMOImpl.queryStoreOrderCartReturnEvents(storeOrderCartReturnEventDto);
        } else {
            storeOrderCartReturnEventDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeOrderCartReturnEventDto.getRow()), count, storeOrderCartReturnEventDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
