package com.java110.fee.bmo.applyRoomDiscount;

import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateApplyRoomDiscountBMO {


    /**
     * 修改房屋折扣申请
     * add by wuxw
     *
     * @param applyRoomDiscountPo
     * @return
     */
    ResponseEntity<String> update(ApplyRoomDiscountPo applyRoomDiscountPo);


}
