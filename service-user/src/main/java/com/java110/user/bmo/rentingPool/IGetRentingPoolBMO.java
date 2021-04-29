package com.java110.user.bmo.rentingPool;
import com.java110.dto.rentingPool.RentingPoolDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolBMO {


    /**
     * 查询房屋出租
     * add by wuxw
     * @param  rentingPoolDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolDto rentingPoolDto);


}
