package com.java110.community.bmo.roomRenovationRecord;

import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 存储装修记录信息
 *
 * @author fqz
 * @date 2021-02-23 16:56
 */
public interface ISaveRoomRenovationRecordBMO {

    /**
     * 添加装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    ResponseEntity<String> saveRecord(RoomRenovationRecordPo roomRenovationRecordPo);

}
