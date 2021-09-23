package com.java110.api.smo.advert;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加发布广告接口
 *
 * add by wuxw 2019-06-30
 */
public interface IDeleteAdvertSMO {

    /**
     * 添加发布广告
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deleteAdvert(IPageData pd);
}
