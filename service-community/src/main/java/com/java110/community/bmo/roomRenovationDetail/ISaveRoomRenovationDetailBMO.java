package com.java110.community.bmo.roomRenovationDetail;

import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRoomRenovationDetailBMO {


    /**
     * 添加装修明细
     * add by wuxw
     * @param roomRenovationDetailPo
     * @return
     */
    ResponseEntity<String> save(RoomRenovationDetailPo roomRenovationDetailPo);


}
