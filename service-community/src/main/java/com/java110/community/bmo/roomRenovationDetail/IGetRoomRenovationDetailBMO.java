package com.java110.community.bmo.roomRenovationDetail;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetRoomRenovationDetailBMO {


    /**
     * 查询装修明细
     * add by wuxw
     * @param  roomRenovationDetailDto
     * @return
     */
    ResponseEntity<String> get(RoomRenovationDetailDto roomRenovationDetailDto);


}
