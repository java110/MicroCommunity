package com.java110.api.smo.auditUser;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加审核人员接口
 *
 * add by wuxw 2019-06-30
 */
public interface IAddAuditUserSMO {

    /**
     * 添加审核人员
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveAuditUser(IPageData pd);
}
