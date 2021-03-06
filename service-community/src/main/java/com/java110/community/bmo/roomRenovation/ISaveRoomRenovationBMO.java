package com.java110.community.bmo.roomRenovation;

import com.java110.po.roomRenovation.RoomRenovationPo;
import org.springframework.http.ResponseEntity;

public interface ISaveRoomRenovationBMO {

    /**
     * 添加装修申请
     * add by wuxw
     *
     * @param roomRenovationPo
     * @return
     */
    ResponseEntity<String> save(RoomRenovationPo roomRenovationPo);

}
