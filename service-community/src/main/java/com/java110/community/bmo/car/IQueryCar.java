package com.java110.community.bmo.car;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IQueryCar {

    /**
     * 查询车辆信息
     * @param reqJson
     * @return
     */
    ResponseEntity<String> queryCarInfo(JSONObject reqJson);
}
