package com.java110.community.bmo.roomRenovation.impl;

import com.java110.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.intf.IRoomRenovationInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRoomRenovationBMOImpl")
public class GetRoomRenovationBMOImpl implements IGetRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * @param roomRenovationDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RoomRenovationDto roomRenovationDto) {


        int count = roomRenovationInnerServiceSMOImpl.queryRoomRenovationsCount(roomRenovationDto);

        List<RoomRenovationDto> roomRenovationDtos = null;
        if (count > 0) {
            roomRenovationDtos = roomRenovationInnerServiceSMOImpl.queryRoomRenovations(roomRenovationDto);
        } else {
            roomRenovationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationDto.getRow()), count, roomRenovationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
