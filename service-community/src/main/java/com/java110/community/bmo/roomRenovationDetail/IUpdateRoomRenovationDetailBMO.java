package com.java110.community.bmo.roomRenovationDetail;

import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRoomRenovationDetailBMO {


    /**
     * 修改装修明细
     * add by wuxw
     *
     * @param roomRenovationDetailPo
     * @return
     */
    ResponseEntity<String> update(RoomRenovationDetailPo roomRenovationDetailPo);


}
