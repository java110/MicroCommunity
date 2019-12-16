package com.java110.common.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 广告项信息组件内部之间使用，没有给外围系统提供服务能力
 * 广告项信息服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAdvertItemServiceDao {

    /**
     * 保存 广告项信息信息
     * @param businessAdvertItemInfo 广告项信息信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAdvertItemInfo(Map businessAdvertItemInfo) throws DAOException;



    /**
     * 查询广告项信息信息（business过程）
     * 根据bId 查询广告项信息信息
     * @param info bId 信息
     * @return 广告项信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAdvertItemInfo(Map info) throws DAOException;




    /**
     * 保存 广告项信息信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAdvertItemInfoInstance(Map info) throws DAOException;




    /**
     * 查询广告项信息信息（instance过程）
     * 根据bId 查询广告项信息信息
     * @param info bId 信息
     * @return 广告项信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getAdvertItemInfo(Map info) throws DAOException;



    /**
     * 修改广告项信息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAdvertItemInfoInstance(Map info) throws DAOException;


    /**
     * 查询广告项信息总数
     *
     * @param info 广告项信息信息
     * @return 广告项信息数量
     */
    int queryAdvertItemsCount(Map info);

}
