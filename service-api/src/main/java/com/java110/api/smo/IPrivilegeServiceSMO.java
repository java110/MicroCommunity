package com.java110.api.smo;

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

    /**
     * 查下权限
     * @param pd
     * @return
     */
    public ResponseEntity<String> loadListPrivilege(IPageData pd);


    /**
     * 保存权限组
     * @param pd
     * @return
     */
    public ResponseEntity<String> savePrivilegeGroup(IPageData pd);

    /**
     * 编辑权限组
     * @param pd
     * @return
     */
    public ResponseEntity<String> editPrivilegeGroup(IPageData pd);

    /**
     * 删除权限组
     * @param pd
     * @return
     */
    public ResponseEntity<String> deletePrivilegeGroup(IPageData pd);

    /**
     * 查询未被绑定的权限
     * @param pd
     * @return
     */
    public ResponseEntity<String> listNoAddPrivilege(IPageData pd);

    /**
     * 添加权限
     * @param pd
     * @return
     */
    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(IPageData pd);


    /**
     * 删除权限
     * @param pd
     * @return
     */
    public ResponseEntity<String> deletePrivilegeFromPrivilegeGroup(IPageData pd);

    /**
     * 查询员工权限
     * @param pd
     * @return
     */
    public ResponseEntity<String> listStaffPrivileges(IPageData pd);
}
