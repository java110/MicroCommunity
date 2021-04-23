package com.java110.user.bmo.rentingPool;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.rentingPool.RentingPoolPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingPoolBMO {


    /**
     * 添加房屋出租
     * add by wuxw
     * @param rentingPoolPo
     * @return
     */
    ResponseEntity<String> save(RentingPoolPo rentingPoolPo, JSONArray photos);


}
