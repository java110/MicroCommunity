package com.java110.fee.bmo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IQueryParkspaceFee {

    /**
     * 查询停车费
     * @param reqJson
     * @return
     */
    ResponseEntity<String> query(JSONObject reqJson);
}
