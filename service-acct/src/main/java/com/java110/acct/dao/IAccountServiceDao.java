package com.java110.acct.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 账户组件内部之间使用，没有给外围系统提供服务能力
 * 账户服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAccountServiceDao {

    /**
     * 保存 账户信息
     * @param businessAccountInfo 账户信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAccountInfo(Map businessAccountInfo) throws DAOException;



    /**
     * 查询账户信息（business过程）
     * 根据bId 查询账户信息
     * @param info bId 信息
     * @return 账户信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAccountInfo(Map info) throws DAOException;




    /**
     * 保存 账户信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAccountInfoInstance(Map info) throws DAOException;




    /**
     * 查询账户信息（instance过程）
     * 根据bId 查询账户信息
     * @param info bId 信息
     * @return 账户信息
     * @throws DAOException DAO异常
     */
    List<Map> getAccountInfo(Map info) throws DAOException;



    /**
     * 修改账户信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAccountInfoInstance(Map info) throws DAOException;


    /**
     * 查询账户总数
     *
     * @param info 账户信息
     * @return 账户数量
     */
    int queryAccountsCount(Map info);

    /**
     * 查询账户总数
     *
     * @param info 账户信息
     * @return 账户数量
     */
    int updateAccount(Map info);

    void saveAccount(Map beanCovertMap);
}
