package com.java110.api.smo.auditUser;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 审核人员管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IListAuditUsersSMO {

    /**
     * 查询审核人员信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listAuditUsers(IPageData pd) throws SMOException;
}
