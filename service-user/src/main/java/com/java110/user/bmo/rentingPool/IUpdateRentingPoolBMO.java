package com.java110.user.bmo.rentingPool;

import com.java110.po.rentingPool.RentingPoolPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRentingPoolBMO {


    /**
     * 修改房屋出租
     * add by wuxw
     *
     * @param rentingPoolPo
     * @return
     */
    ResponseEntity<String> update(RentingPoolPo rentingPoolPo);


}
