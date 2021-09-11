package com.java110.api.smo.notice;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加公告接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddNoticeSMO {

    /**
     * 添加公告
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveNotice(IPageData pd);
}
