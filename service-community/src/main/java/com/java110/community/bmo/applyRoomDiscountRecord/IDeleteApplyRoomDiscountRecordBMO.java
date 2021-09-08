package com.java110.community.bmo.applyRoomDiscountRecord;

import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 删除空置房验房记录
 *
 * @author fqz
 * @date 2021-09-01 10:37
 */
public interface IDeleteApplyRoomDiscountRecordBMO {

    /**
     * 删除装修记录
     *
     * @param applyRoomDiscountRecordPo
     * @return
     */
    ResponseEntity<String> delete(ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo);

}
