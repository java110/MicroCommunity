package com.java110.community.bmo.roomRenovationRecord;

import com.java110.po.roomRenovationRecord.RoomRenovationRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 查询装修记录信息
 *
 * @author fqz
 * @date 2021-02-23 16:57
 */
public interface IGetRoomRenovationRecordBMO {

    /**
     * 查询装修记录(关联文件表)
     *
     * @param roomRenovationRecordPo
     * @return
     */
    ResponseEntity<String> get(RoomRenovationRecordPo roomRenovationRecordPo);

    /**
     * 查询装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    ResponseEntity<String> getRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo);

}
