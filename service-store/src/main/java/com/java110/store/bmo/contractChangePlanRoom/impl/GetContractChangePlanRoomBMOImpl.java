package com.java110.store.bmo.contractChangePlanRoom.impl;

import com.java110.dto.contract.ContractChangePlanRoomDto;
import com.java110.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.java110.store.bmo.contractChangePlanRoom.IGetContractChangePlanRoomBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractChangePlanRoomBMOImpl")
public class GetContractChangePlanRoomBMOImpl implements IGetContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     * @param contractChangePlanRoomDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractChangePlanRoomDto contractChangePlanRoomDto) {


        int count = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRoomsCount(contractChangePlanRoomDto);

        List<ContractChangePlanRoomDto> contractChangePlanRoomDtos = null;
        if (count > 0) {
            contractChangePlanRoomDtos = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRooms(contractChangePlanRoomDto);
        } else {
            contractChangePlanRoomDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanRoomDto.getRow()), count, contractChangePlanRoomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
