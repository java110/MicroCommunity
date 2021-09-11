package com.java110.api.smo.payment;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品通知 支付完成
 */
public interface IGoodsToNotifySMO {

    /**
     * 支付完成
     * @param request
     * @return
     */
    public ResponseEntity<String> toNotify(String param,HttpServletRequest request);
}
