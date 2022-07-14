package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 路由组件内部之间使用，没有给外围系统提供服务能力
 * 路由服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IMenuServiceDao {


    /**
     * 保存 路由信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    int saveMenuGroupInfo(Map info) throws DAOException;


    /**
     * 查询路由信息（instance过程）
     * 根据bId 查询路由信息
     *
     * @param info bId 信息
     * @return 路由信息
     * @throws DAOException DAO异常
     */
    List<Map> getMenuGroupInfo(Map info) throws DAOException;


    /**
     * 修改路由信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateMenuGroupInfo(Map info) throws DAOException;


    /**
     * 查询路由总数
     *
     * @param info 路由信息
     * @return 路由数量
     */
    int queryMenuGroupsCount(Map info);


    /**
     * 保存 路由信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    int saveBasePrivilegeInfo(Map info) throws DAOException;


    /**
     * 查询路由信息（instance过程）
     * 根据bId 查询路由信息
     *
     * @param info bId 信息
     * @return 路由信息
     * @throws DAOException DAO异常
     */
    List<Map> getBasePrivilegeInfo(Map info) throws DAOException;


    /**
     * 修改路由信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateBasePrivilegeInfo(Map info) throws DAOException;


    /**
     * 查询菜单总数
     *
     * @param info 菜单信息
     * @return 菜单数量
     */
    int queryBasePrivilegesCount(Map info);

    List<Map> checkUserHasResource(Map info);


    /**
     * 保存 菜单信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    int saveMenuInfo(Map info) throws DAOException;


    /**
     * 查询菜单信息（instance过程）
     * 根据bId 查询菜单信息
     *
     * @param info bId 信息
     * @return 菜单信息
     * @throws DAOException DAO异常
     */
    List<Map> getMenuInfo(Map info) throws DAOException;


    /**
     * 修改菜单信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateMenuInfo(Map info) throws DAOException;


    /**
     * 查询菜单总数
     *
     * @param info 菜单信息
     * @return 菜单数量
     */
    int queryMenusCount(Map info);

    List<Map> hasPrivilege(Map info);
}
