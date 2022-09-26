package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.websocket.ParkingAreaWebsocket;
import com.java110.api.websocket.ParkingBoxWebsocket;
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
            ParkingBoxWebsocket.sendInfo(param.toJSONString(), param.getString("extBoxId"));

        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        try {
            ParkingAreaWebsocket.sendInfo(param.toJSONString(), param.getString("extPaId"));
        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        return 1;
    }
}
