package com.java110.front.smo.api;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IApiSMO {

    public ResponseEntity<String> doApi(String body, Map<String, String> headers);
}
