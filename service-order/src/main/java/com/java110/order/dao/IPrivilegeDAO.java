package com.java110.order.dao;

import java.util.List;
import java.util.Map;

/**
 * 权限数据层操作接口
 * Created by Administrator on 2019/4/1.
 */
public interface IPrivilegeDAO {

    /**
     * 保存用户权限
     *
     * @param info
     * @return
     */
    public boolean saveUserDefaultPrivilege(Map info);


    /**
     * 删除用所有权限
     *
     * @param info
     * @return
     */
    public boolean deleteUserAllPrivilege(Map info);


    /**
     * 保存权限组
     *
     * @param info
     * @return
     */
    public boolean savePrivilegeGroup(Map info);

    /**
     * 编辑权限组
     *
     * @param info
     * @return
     */
    public boolean updatePrivilegeGroup(Map info);

    /**
     * 删除权限组
     *
     * @param info
     * @return
     */
    public boolean deletePrivilegeGroup(Map info);


    /**
     * 删除权限组下权限
     *
     * @param info
     * @return
     */
    public boolean deletePrivilegeRel(Map info);


    public List<Map> queryPrivilegeRel(Map info);

    public List<Map> queryPrivilegeGroup(Map info);

    public boolean addPrivilegeRel(Map info);

    /**
     * 查询权限
     *
     * @param info
     * @return
     */
    public List<Map> queryPrivilege(Map info);

    /**
     * 查询用户权限
     *
     * @param info
     * @return
     */
    public List<Map> queryUserPrivilege(Map info);

    /**
     * 添加用户权限
     *
     * @param info
     * @return
     */
    public boolean addUserPrivilege(Map info);

    /**
     * 删除用户权限
     *
     * @param info
     * @return
     */
    public boolean deleteUserPrivilege(Map info);

    /**
     * Query employees with this permission
     *
     * @param info
     * @return
     */
    List<Map> queryPrivilegeUsers(Map info);
}
