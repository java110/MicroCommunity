package com.java110.acct.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 账户提现组件内部之间使用，没有给外围系统提供服务能力
 * 账户提现服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAccountWithdrawalApplyServiceDao {


    /**
     * 保存 账户提现信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAccountWithdrawalApplyInfo(Map info) throws DAOException;




    /**
     * 查询账户提现信息（instance过程）
     * 根据bId 查询账户提现信息
     * @param info bId 信息
     * @return 账户提现信息
     * @throws DAOException DAO异常
     */
    List<Map> getAccountWithdrawalApplyInfo(Map info) throws DAOException;



    /**
     * 修改账户提现信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAccountWithdrawalApplyInfo(Map info) throws DAOException;


    /**
     * 查询账户提现总数
     *
     * @param info 账户提现信息
     * @return 账户提现数量
     */
    int queryAccountWithdrawalApplysCount(Map info);

    /**
     * 查询账户提现信息（instance过程）
     * 根据bId 查询账户提现信息
     * @param info bId 信息
     * @return 账户提现信息
     * @throws DAOException DAO异常
     */
    List<Map> listStateWithdrawalApplys(Map info) throws DAOException;

    /**
     * 查询账户提现总数
     *
     * @param info 账户提现信息
     * @return 账户提现数量
     */
    int listStateWithdrawalApplysCount(Map info);
}
