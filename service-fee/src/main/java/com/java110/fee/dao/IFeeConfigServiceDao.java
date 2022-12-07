package com.java110.fee.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 费用配置组件内部之间使用，没有给外围系统提供服务能力
 * 费用配置服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeConfigServiceDao {

    /**
     * 保存 费用配置信息
     *
     * @param businessFeeConfigInfo 费用配置信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFeeConfigInfo(Map businessFeeConfigInfo) throws DAOException;


    /**
     * 查询费用配置信息（business过程）
     * 根据bId 查询费用配置信息
     *
     * @param info bId 信息
     * @return 费用配置信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFeeConfigInfo(Map info) throws DAOException;


    /**
     * 保存 费用配置信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFeeConfigInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用配置信息（instance过程）
     * 根据bId 查询费用配置信息
     *
     * @param info bId 信息
     * @return 费用配置信息
     * @throws DAOException DAO异常
     */
    List<Map> getFeeConfigInfo(Map info) throws DAOException;


    /**
     * 修改费用配置信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFeeConfigInfoInstance(Map info) throws DAOException;


    /**
     * 查询费用配置总数
     *
     * @param info 费用配置信息
     * @return 费用配置数量
     */
    int queryFeeConfigsCount(Map info);


    /**
     * 保存费用配置
     *
     * @param info
     * @return
     */
    int saveFeeConfig(Map info);

    /**
     * 删除费用配置
     *
     * @param info
     * @return
     */
    int deleteFeeConfig(Map info);

}
