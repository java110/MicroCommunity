package com.java110.api.smo.service;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 服务绑定接口类
 */
public interface IBindingServiceSMO {

    //绑定服务
    public ResponseEntity<String> binding(IPageData pd);
}
