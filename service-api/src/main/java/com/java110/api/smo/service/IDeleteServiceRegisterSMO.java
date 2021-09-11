package com.java110.api.smo.service;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加服务绑定接口
 *
 * add by wuxw 2019-06-30
 */
public interface IDeleteServiceRegisterSMO {

    /**
     * 添加服务绑定
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deleteServiceRegister(IPageData pd);
}
