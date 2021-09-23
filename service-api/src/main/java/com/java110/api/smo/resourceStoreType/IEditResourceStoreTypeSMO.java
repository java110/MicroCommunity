package com.java110.api.smo.resourceStoreType;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改物品管理接口
 * <p>
 * add by wuxw 2019-06-30
 */
public interface IEditResourceStoreTypeSMO {

    /**
     * 修改小区
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateResourceStoreType(IPageData pd);
}
