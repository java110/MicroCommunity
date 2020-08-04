package com.java110.fee.bmo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IPayOweFee {

    /**
     * 欠费缴费接口
     * @param reqJson 缴费报文
     * @return
     */
    ResponseEntity<String> pay(JSONObject reqJson);
}
