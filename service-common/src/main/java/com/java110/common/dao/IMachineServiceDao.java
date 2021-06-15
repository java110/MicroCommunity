package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 设备组件内部之间使用，没有给外围系统提供服务能力
 * 设备服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IMachineServiceDao {

    /**
     * 保存 设备信息
     * @param businessMachineInfo 设备信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessMachineInfo(Map businessMachineInfo) throws DAOException;



    /**
     * 查询设备信息（business过程）
     * 根据bId 查询设备信息
     * @param info bId 信息
     * @return 设备信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMachineInfo(Map info) throws DAOException;




    /**
     * 保存 设备信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMachineInfoInstance(Map info) throws DAOException;




    /**
     * 查询设备信息（instance过程）
     * 根据bId 查询设备信息
     * @param info bId 信息
     * @return 设备信息
     * @throws DAOException DAO异常
     */
    List<Map> getMachineInfo(Map info) throws DAOException;



    /**
     * 修改设备信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateMachineInfoInstance(Map info) throws DAOException;


    /**
     * 查询设备总数
     *
     * @param info 设备信息
     * @return 设备数量
     */
    int queryMachinesCount(Map info);

}
