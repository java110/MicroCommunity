package com.java110.api.smo.addOwner;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加添加业主接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddOwnerRoomBindingSMO {

    /**
     * 添加添加业主
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> bindingAddOwnerRoom(IPageData pd);
}
