package com.java110.fee.bmo.applyRoomDiscount;

import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import org.springframework.http.ResponseEntity;
public interface ISaveApplyRoomDiscountBMO {


    /**
     * 添加房屋折扣申请
     * add by wuxw
     * @param applyRoomDiscountPo
     * @return
     */
    ResponseEntity<String> save(ApplyRoomDiscountPo applyRoomDiscountPo);


}
