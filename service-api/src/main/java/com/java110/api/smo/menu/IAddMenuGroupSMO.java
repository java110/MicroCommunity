package com.java110.api.smo.menu;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加菜单组接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddMenuGroupSMO {

    /**
     * 添加菜单组
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveMenuGroup(IPageData pd);
}
