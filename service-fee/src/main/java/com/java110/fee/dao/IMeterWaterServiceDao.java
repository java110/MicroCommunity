package com.java110.fee.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 水电费组件内部之间使用，没有给外围系统提供服务能力
 * 水电费服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IMeterWaterServiceDao {

    /**
     * 保存 水电费信息
     *
     * @param businessMeterWaterInfo 水电费信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessMeterWaterInfo(Map businessMeterWaterInfo) throws DAOException;


    /**
     * 查询水电费信息（business过程）
     * 根据bId 查询水电费信息
     *
     * @param info bId 信息
     * @return 水电费信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMeterWaterInfo(Map info) throws DAOException;


    /**
     * 保存 水电费信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMeterWaterInfoInstance(Map info) throws DAOException;


    /**
     * 查询水电费信息（instance过程）
     * 根据bId 查询水电费信息
     *
     * @param info bId 信息
     * @return 水电费信息
     * @throws DAOException DAO异常
     */
    List<Map> getMeterWaterInfo(Map info) throws DAOException;


    /**
     * 修改水电费信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateMeterWaterInfoInstance(Map info) throws DAOException;


    /**
     * 查询水电费总数
     *
     * @param info 水电费信息
     * @return 水电费数量
     */
    int queryMeterWatersCount(Map info);

    int insertMeterWaters(Map info);

}
