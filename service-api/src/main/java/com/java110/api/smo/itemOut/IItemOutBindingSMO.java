package com.java110.api.smo.itemOut;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加物品出库接口
 *
 * add by wuxw 2019-06-30
 */
public interface IItemOutBindingSMO {

    /**
     * 添加物品出库
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> bindingItemOut(IPageData pd);
}
