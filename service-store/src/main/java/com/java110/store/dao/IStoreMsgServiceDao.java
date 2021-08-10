package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 商户消息组件内部之间使用，没有给外围系统提供服务能力
 * 商户消息服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IStoreMsgServiceDao {


    /**
     * 保存 商户消息信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveStoreMsgInfo(Map info) throws DAOException;




    /**
     * 查询商户消息信息（instance过程）
     * 根据bId 查询商户消息信息
     * @param info bId 信息
     * @return 商户消息信息
     * @throws DAOException DAO异常
     */
    List<Map> getStoreMsgInfo(Map info) throws DAOException;



    /**
     * 修改商户消息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateStoreMsgInfo(Map info) throws DAOException;


    /**
     * 查询商户消息总数
     *
     * @param info 商户消息信息
     * @return 商户消息数量
     */
    int queryStoreMsgsCount(Map info);

}
