package com.java110.api.smo.service;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加服务提供接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddServiceProvideSMO {

    /**
     * 添加服务提供
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveServiceProvide(IPageData pd);
}
