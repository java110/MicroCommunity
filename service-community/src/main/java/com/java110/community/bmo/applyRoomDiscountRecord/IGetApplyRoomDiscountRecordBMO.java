package com.java110.community.bmo.applyRoomDiscountRecord;

import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountRecordDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询空置房验房记录
 *
 * @author fqz
 * @date 2021-08-31 17:41
 */
public interface IGetApplyRoomDiscountRecordBMO {

    /**
     * 查询验房记录(关联文件表)
     *
     * @param applyRoomDiscountRecordDto
     * @return
     */
    ResponseEntity<String> get(ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

    /**
     * 查询验房记录(关联文件表)
     *
     * @param applyRoomDiscountRecordDto
     * @return
     */
    ResponseEntity<String> getRecord(ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto);

}
