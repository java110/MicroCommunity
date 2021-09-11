package com.java110.api.smo.app;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加应用接口
 *
 * add by wuxw 2019-06-30
 */
public interface IDeleteAppSMO {

    /**
     * 添加应用
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deleteApp(IPageData pd);
}
