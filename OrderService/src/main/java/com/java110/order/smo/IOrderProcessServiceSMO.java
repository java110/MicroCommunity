package com.java110.order.smo;

import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IOrderProcessServiceSMO {

    /**
     * 预生单
     * @param reqJson
     * @param headers
     * @return
     * @throws SMOException
     */
    public ResponseEntity<String> preService(String reqJson, Map<String, String> headers) throws SMOException;

    /**
     * 确认提交方法
     * @param reqJson
     * @param headers
     * @return
     * @throws SMOException
     */
    public ResponseEntity confirmService(String reqJson, Map<String, String> headers) throws SMOException;

}
