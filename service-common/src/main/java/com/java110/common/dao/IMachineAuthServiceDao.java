package com.java110.common.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 设备权限组件内部之间使用，没有给外围系统提供服务能力
 * 设备权限服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IMachineAuthServiceDao {

    /**
     * 保存 设备权限信息
     *
     * @param businessMachineAuthInfo 设备权限信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessMachineAuthInfo(Map businessMachineAuthInfo) throws DAOException;

    /**
     * 查询设备权限信息（business过程）
     * 根据bId 查询设备权限信息
     *
     * @param info bId 信息
     * @return 设备权限信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMachineAuthInfo(Map info) throws DAOException;

    /**
     * 保存 设备权限信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    int saveMachineAuthInfoInstance(Map info) throws DAOException;

    /**
     * 查询设备权限信息（instance过程）
     * 根据bId 查询设备权限信息
     *
     * @param info bId 信息
     * @return 设备权限信息
     * @throws DAOException DAO异常
     */
    List<Map> getMachineAuthInfo(Map info) throws DAOException;

    /**
     * 修改设备权限信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateMachineAuthInfoInstance(Map info) throws DAOException;

    /**
     * 查询设备权限总数
     *
     * @param info 设备权限信息
     * @return 设备权限数量
     */
    int queryMachineAuthsCount(Map info);

    /**
     * 添加员工门禁授权
     *
     * @param info
     * @return
     */
    int saveMachineAuth(Map info);

    /**
     * 删除员工门禁授权
     *
     * @param info
     * @return
     */
    int deleteMachineAuth(Map info);

}
