package com.java110.front.smo.api;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IApiSMO {

    public ResponseEntity<String> doApi(String body, Map<String, String> headers, HttpServletRequest request);
}
