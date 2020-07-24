package com.java110.community.bmo.park;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IQueryParkSpace {

    /**
     * 查询车位
     * @param reqJson
     * @return
     */
    ResponseEntity<String> query(JSONObject reqJson);
}
