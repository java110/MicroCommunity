package com.java110.app.smo.payment;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信通知 支付完成
 */
public interface IToNotifySMO {

    /**
     * 支付完成
     * @param request
     * @return
     */
    public ResponseEntity<String> toNotify(HttpServletRequest request);
}
