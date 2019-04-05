package com.java110.web.smo;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 权限管理接口类
 */
public interface IPrivilegeServiceSMO {

    /**
     * 查询权限组
     * @param pd
     * @return
     */
    public ResponseEntity<String> listPrivilegeGroup(IPageData pd);
}
