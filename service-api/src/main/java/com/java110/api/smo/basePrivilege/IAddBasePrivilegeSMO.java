package com.java110.api.smo.basePrivilege;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加权限接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddBasePrivilegeSMO {

    /**
     * 添加权限
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveBasePrivilege(IPageData pd);
}
