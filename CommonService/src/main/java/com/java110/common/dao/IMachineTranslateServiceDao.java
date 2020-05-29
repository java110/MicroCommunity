package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 设备同步组件内部之间使用，没有给外围系统提供服务能力
 * 设备同步服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IMachineTranslateServiceDao {

    /**
     * 保存 设备同步信息
     * @param businessMachineTranslateInfo 设备同步信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessMachineTranslateInfo(Map businessMachineTranslateInfo) throws DAOException;



    /**
     * 查询设备同步信息（business过程）
     * 根据bId 查询设备同步信息
     * @param info bId 信息
     * @return 设备同步信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMachineTranslateInfo(Map info) throws DAOException;




    /**
     * 保存 设备同步信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMachineTranslateInfoInstance(Map info) throws DAOException;




    /**
     * 查询设备同步信息（instance过程）
     * 根据bId 查询设备同步信息
     * @param info bId 信息
     * @return 设备同步信息
     * @throws DAOException DAO异常
     */
    List<Map> getMachineTranslateInfo(Map info) throws DAOException;



    /**
     * 修改设备同步信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateMachineTranslateInfoInstance(Map info) throws DAOException;


    /**
     * 查询设备同步总数
     *
     * @param info 设备同步信息
     * @return 设备同步数量
     */
    int queryMachineTranslatesCount(Map info);

    /**
     * 修改设备状态
     * @param info
     * @return
     */
    int updateMachineTranslateState(Map info);


    /**
     * 保存 硬件传输
     * @param info
     * @throws DAOException
     */
    void saveMachineTranslate(Map info) throws DAOException;


    /**
     * 修改 硬件传输
     * @param info
     * @throws DAOException
     */
    void updateMachineTranslate(Map info) throws DAOException;




}
