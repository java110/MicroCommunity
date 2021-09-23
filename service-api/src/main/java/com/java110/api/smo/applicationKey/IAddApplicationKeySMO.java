package com.java110.api.smo.applicationKey;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加钥匙申请接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddApplicationKeySMO {

    /**
     * 添加钥匙申请
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveApplicationKey(IPageData pd);
}
