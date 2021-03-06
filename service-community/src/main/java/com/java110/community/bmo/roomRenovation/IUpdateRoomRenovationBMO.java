package com.java110.community.bmo.roomRenovation;
import com.java110.dto.RoomDto;
import com.java110.po.roomRenovation.RoomRenovationPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRoomRenovationBMO {


    /**
     * 修改装修申请
     * add by wuxw
     * @param roomRenovationPo
     * @return
     */
    ResponseEntity<String> update(RoomRenovationPo roomRenovationPo);

    /**
     * 修改房屋状态
     * add by wuxw
     * @param roomDto
     * @return
     */
    ResponseEntity<String> updateRoom(RoomDto roomDto);

}
