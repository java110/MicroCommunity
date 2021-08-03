package com.java110.store.bmo.storeMsg.impl;

import com.java110.dto.storeMsg.StoreMsgDto;
import com.java110.intf.store.IStoreMsgInnerServiceSMO;
import com.java110.store.bmo.storeMsg.IGetStoreMsgBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreMsgBMOImpl")
public class GetStoreMsgBMOImpl implements IGetStoreMsgBMO {

    @Autowired
    private IStoreMsgInnerServiceSMO storeMsgInnerServiceSMOImpl;

    /**
     * @param storeMsgDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreMsgDto storeMsgDto) {


        int count = storeMsgInnerServiceSMOImpl.queryStoreMsgsCount(storeMsgDto);

        List<StoreMsgDto> storeMsgDtos = null;
        if (count > 0) {
            storeMsgDtos = storeMsgInnerServiceSMOImpl.queryStoreMsgs(storeMsgDto);
        } else {
            storeMsgDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeMsgDto.getRow()), count, storeMsgDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
