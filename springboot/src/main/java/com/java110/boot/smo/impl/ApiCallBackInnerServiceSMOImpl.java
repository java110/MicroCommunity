package com.java110.boot.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.boot.websocket.ParkingAreaWebsocket;
import com.java110.intf.api.IApiCallBackInnerServiceSMO;
import com.java110.utils.exception.SMOException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiCallBackInnerServiceSMOImpl implements IApiCallBackInnerServiceSMO {
    @Override
    public int webSentParkingArea(@RequestBody JSONObject reqJson) {
        JSONObject param = JSONObject.parseObject(reqJson.toString());
        try {
            ParkingAreaWebsocket.sendInfo(param.toJSONString(), param.getString("extBoxId"));
        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        return 1;
    }
}
