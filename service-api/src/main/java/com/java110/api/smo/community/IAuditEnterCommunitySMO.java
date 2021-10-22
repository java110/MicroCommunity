package com.java110.api.smo.community;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 审核入驻小区接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAuditEnterCommunitySMO {

    /**
     * 审核小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> auditEnterCommunity(IPageData pd);
}
