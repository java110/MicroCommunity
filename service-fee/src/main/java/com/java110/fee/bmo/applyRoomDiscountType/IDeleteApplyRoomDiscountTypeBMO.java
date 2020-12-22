package com.java110.fee.bmo.applyRoomDiscountType;

import com.java110.po.applyRoomDiscountType.ApplyRoomDiscountTypePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteApplyRoomDiscountTypeBMO {


    /**
     * 修改优惠申请类型
     * add by wuxw
     *
     * @param applyRoomDiscountTypePo
     * @return
     */
    ResponseEntity<String> delete(ApplyRoomDiscountTypePo applyRoomDiscountTypePo);


}
