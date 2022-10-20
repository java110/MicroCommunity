package com.java110.acct.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 账户交易组件内部之间使用，没有给外围系统提供服务能力
 * 账户交易服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IAccountDetailServiceDao {

    /**
     * 保存 账户交易信息
     *
     * @param businessAccountDetailInfo 账户交易信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAccountDetailInfo(Map businessAccountDetailInfo) throws DAOException;


    /**
     * 查询账户交易信息（business过程）
     * 根据bId 查询账户交易信息
     *
     * @param info bId 信息
     * @return 账户交易信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAccountDetailInfo(Map info) throws DAOException;


    /**
     * 保存 账户交易信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAccountDetailInfoInstance(Map info) throws DAOException;


    /**
     * 查询账户交易信息（instance过程）
     * 根据bId 查询账户交易信息
     *
     * @param info bId 信息
     * @return 账户交易信息
     * @throws DAOException DAO异常
     */
    List<Map> getAccountDetailInfo(Map info) throws DAOException;


    /**
     * 修改账户交易信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAccountDetailInfoInstance(Map info) throws DAOException;


    /**
     * 查询账户交易总数
     *
     * @param info 账户交易信息
     * @return 账户交易数量
     */
    int queryAccountDetailsCount(Map info);

    /**
     * 保存账户 明细
     *
     * @param beanCovertMap
     * @return
     */
    int saveAccountDetails(Map beanCovertMap);

    int updateAccountDetails(Map beanCovertMap);
}
