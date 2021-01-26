package com.java110.community.bmo.roomRenovation;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import org.springframework.http.ResponseEntity;
public interface IGetRoomRenovationBMO {


    /**
     * 查询装修申请
     * add by wuxw
     * @param  roomRenovationDto
     * @return
     */
    ResponseEntity<String> get(RoomRenovationDto roomRenovationDto);


}
