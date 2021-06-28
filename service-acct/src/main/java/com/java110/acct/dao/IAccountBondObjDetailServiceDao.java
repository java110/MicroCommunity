package com.java110.acct.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 保证金明细组件内部之间使用，没有给外围系统提供服务能力
 * 保证金明细服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAccountBondObjDetailServiceDao {


    /**
     * 保存 保证金明细信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAccountBondObjDetailInfo(Map info) throws DAOException;




    /**
     * 查询保证金明细信息（instance过程）
     * 根据bId 查询保证金明细信息
     * @param info bId 信息
     * @return 保证金明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getAccountBondObjDetailInfo(Map info) throws DAOException;



    /**
     * 修改保证金明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAccountBondObjDetailInfo(Map info) throws DAOException;


    /**
     * 查询保证金明细总数
     *
     * @param info 保证金明细信息
     * @return 保证金明细数量
     */
    int queryAccountBondObjDetailsCount(Map info);

}
