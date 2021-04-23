package com.java110.community.bmo.roomRenovationDetail.impl;

import com.java110.community.bmo.roomRenovationDetail.IGetRoomRenovationDetailBMO;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRoomRenovationDetailBMOImpl")
public class GetRoomRenovationDetailBMOImpl implements IGetRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * @param roomRenovationDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RoomRenovationDetailDto roomRenovationDetailDto) {


        int count = roomRenovationDetailInnerServiceSMOImpl.queryRoomRenovationDetailsCount(roomRenovationDetailDto);

        List<RoomRenovationDetailDto> roomRenovationDetailDtos = null;
        if (count > 0) {
            roomRenovationDetailDtos = roomRenovationDetailInnerServiceSMOImpl.queryRoomRenovationDetails(roomRenovationDetailDto);
        } else {
            roomRenovationDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationDetailDto.getRow()), count, roomRenovationDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
