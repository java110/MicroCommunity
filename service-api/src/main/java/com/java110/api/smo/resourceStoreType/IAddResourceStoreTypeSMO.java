package com.java110.api.smo.resourceStoreType;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加物品管理接口
 * <p>
 * add by fqz 2021-04-21
 */
public interface IAddResourceStoreTypeSMO {

    /**
     * 添加物品管理
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveResourceStoreType(IPageData pd);
}
