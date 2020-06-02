package com.java110.community.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 报修派单组件内部之间使用，没有给外围系统提供服务能力
 * 报修派单服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IRepairUserServiceDao {

    /**
     * 保存 报修派单信息
     * @param businessRepairUserInfo 报修派单信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessRepairUserInfo(Map businessRepairUserInfo) throws DAOException;



    /**
     * 查询报修派单信息（business过程）
     * 根据bId 查询报修派单信息
     * @param info bId 信息
     * @return 报修派单信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessRepairUserInfo(Map info) throws DAOException;




    /**
     * 保存 报修派单信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveRepairUserInfoInstance(Map info) throws DAOException;




    /**
     * 查询报修派单信息（instance过程）
     * 根据bId 查询报修派单信息
     * @param info bId 信息
     * @return 报修派单信息
     * @throws DAOException DAO异常
     */
    List<Map> getRepairUserInfo(Map info) throws DAOException;



    /**
     * 修改报修派单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateRepairUserInfoInstance(Map info) throws DAOException;


    /**
     * 查询报修派单总数
     *
     * @param info 报修派单信息
     * @return 报修派单数量
     */
    int queryRepairUsersCount(Map info);

}
