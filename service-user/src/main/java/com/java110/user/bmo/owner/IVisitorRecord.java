package com.java110.user.bmo.owner;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IVisitorRecord {

    /**
     * 查询访客记录
     * @param reqJson
     * @return
     */
    ResponseEntity<String> query(JSONObject reqJson);
}
