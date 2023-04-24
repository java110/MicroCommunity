package com.java110.user.bmo.rentingPoolAttr;
import com.java110.dto.rentingPool.RentingPoolAttrDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolAttrBMO {


    /**
     * 查询出租房屋属性
     * add by wuxw
     * @param  rentingPoolAttrDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolAttrDto rentingPoolAttrDto);


}
