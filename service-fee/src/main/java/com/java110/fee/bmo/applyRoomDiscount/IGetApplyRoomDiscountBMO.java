package com.java110.fee.bmo.applyRoomDiscount;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import org.springframework.http.ResponseEntity;
public interface IGetApplyRoomDiscountBMO {


    /**
     * 查询房屋折扣申请
     * add by wuxw
     * @param  applyRoomDiscountDto
     * @return
     */
    ResponseEntity<String> get(ApplyRoomDiscountDto applyRoomDiscountDto);


}
