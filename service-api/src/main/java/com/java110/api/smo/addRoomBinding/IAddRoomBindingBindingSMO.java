package com.java110.api.smo.addRoomBinding;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加添加房屋接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddRoomBindingBindingSMO {

    /**
     * 添加添加房屋
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> bindingAddRoomBinding(IPageData pd);
}
