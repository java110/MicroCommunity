package com.java110.user.bmo.owner;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IQueryTenants {

    /**
     * 查询 常驻人口
     * @param reqJson
     * @return
     */
    ResponseEntity<String> query(JSONObject reqJson);
}
