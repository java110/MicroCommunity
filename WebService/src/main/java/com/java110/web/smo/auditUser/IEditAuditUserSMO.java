package com.java110.web.smo.auditUser;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改审核人员接口
 *
 * add by wuxw 2019-06-30
 */
public interface IEditAuditUserSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updateAuditUser(IPageData pd);
}
