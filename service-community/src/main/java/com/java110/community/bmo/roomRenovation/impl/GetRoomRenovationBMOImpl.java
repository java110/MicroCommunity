package com.java110.community.bmo.roomRenovation.impl;

import com.java110.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.intf.community.IRoomRenovationInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<String> get(@RequestBody RoomRenovationDto roomRenovationDto) {


        int count = roomRenovationInnerServiceSMOImpl.queryRoomRenovationsCount(roomRenovationDto);

        List<RoomRenovationDto> roomRenovationDtos = new ArrayList<>();
        if (count > 0) {
            List<RoomRenovationDto> roomRenovationDtoList = roomRenovationInnerServiceSMOImpl.queryRoomRenovations(roomRenovationDto);
            for (RoomRenovationDto renovationDto : roomRenovationDtoList) {
                renovationDto.setUserId(roomRenovationDto.getUserId());
                roomRenovationDtos.add(renovationDto);
            }
        } else {
            roomRenovationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationDto.getRow()), count, roomRenovationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
