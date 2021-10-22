package com.java110.api.smo.devServiceProvide;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加开发服务提供接口
 *
 * add by wuxw 2019-06-30
 */
public interface IDevServiceProvideBindingSMO {

    /**
     * 添加开发服务提供
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> bindingDevServiceProvide(IPageData pd);
}
