package com.java110.api.smo.payment;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 统一下单接口类
 */
public interface IToPayOweFeeSMO {

    /**
     * 下单
     * @param pd
     * @return
     */
     ResponseEntity<String> toPay(IPageData pd);
}
