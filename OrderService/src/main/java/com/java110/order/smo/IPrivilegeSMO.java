package com.java110.order.smo;

import org.springframework.http.ResponseEntity;

/**
 * 权限业务处理类
 * Created by Administrator on 2019/4/1.
 */
public interface IPrivilegeSMO {

    /**
     * 保存用户默认权限
     * @param privilegeInfo 权限信息
     * @return
     */
    public ResponseEntity<String> saveUserDefaultPrivilege(String privilegeInfo);


    /**
     * 删除所有权限
     * @param privilegeInfo
     * @return
     */
    public ResponseEntity<String> deleteUserAllPrivilege(String privilegeInfo);


    /**
     * 保存权限组
     * @param privilegeGroupInfo
     * @return
     */
    public ResponseEntity<String> savePrivilegeGroup(String privilegeGroupInfo);


    /**
     * 编辑权限组
     * @param privilegeGroupInfo
     * @return
     */
    public ResponseEntity<String> editPrivilegeGroup(String privilegeGroupInfo);
    /**
     * 删除权限组
     * @param privilegeGroupInfo
     * @return
     */
    public ResponseEntity<String> deletePrivilegeGroup(String privilegeGroupInfo);

    /**
     * 添加权限至权限组
     * @param privilegeInfo
     * @return
     */
    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(String privilegeInfo);


    /**
     * 从权限组中删除权限
     * @param privilegeInfo
     * @return
     */
    public ResponseEntity<String> deletePrivilegeToPrivilegeGroup(String privilegeInfo);

    /**
     * 员工添加权限
     * @param privilegeInfo
     * @return
     */
    public ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(String privilegeInfo);

    /**
     * 删除员工权限
     * @param privilegeInfo
     * @return
     */
    public ResponseEntity<String> deleteStaffPrivilegeOrPrivilegeGroup(String privilegeInfo);




}
