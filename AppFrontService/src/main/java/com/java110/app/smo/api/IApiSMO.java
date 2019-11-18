package com.java110.app.smo.api;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IApiSMO {

    public ResponseEntity<String> doApi(String body, Map<String, String> headers);
}
