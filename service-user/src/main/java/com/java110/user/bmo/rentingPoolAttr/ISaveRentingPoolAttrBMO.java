package com.java110.user.bmo.rentingPoolAttr;

import com.java110.po.rentingPoolAttr.RentingPoolAttrPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingPoolAttrBMO {


    /**
     * 添加出租房屋属性
     * add by wuxw
     * @param rentingPoolAttrPo
     * @return
     */
    ResponseEntity<String> save(RentingPoolAttrPo rentingPoolAttrPo);


}
