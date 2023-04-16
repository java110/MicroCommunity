package com.java110.store.bmo.contractRoom.impl;

import com.java110.dto.contract.ContractRoomDto;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.store.bmo.contractRoom.IGetContractRoomBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractRoomBMOImpl")
public class GetContractRoomBMOImpl implements IGetContractRoomBMO {

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    /**
     * @param contractRoomDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractRoomDto contractRoomDto) {


        int count = contractRoomInnerServiceSMOImpl.queryContractRoomsCount(contractRoomDto);

        List<ContractRoomDto> contractRoomDtos = null;
        if (count > 0) {
            contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
        } else {
            contractRoomDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractRoomDto.getRow()), count, contractRoomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
