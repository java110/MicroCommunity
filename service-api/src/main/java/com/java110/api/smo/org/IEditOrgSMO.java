package com.java110.api.smo.org;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改组织管理接口
 *
 * add by wuxw 2019-06-30
 */
public interface IEditOrgSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateOrg(IPageData pd);
}
