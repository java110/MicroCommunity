package com.java110.community.bmo.roomRenovationRecord;

import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 删除装修记录
 *
 * @author fqz
 * @date 2021-02-27 11:49
 */
public interface IDeleteRoomRenovationRecordBMO {

    /**
     * 删除装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    ResponseEntity<String> delete(RoomRenovationRecordPo roomRenovationRecordPo);

}
