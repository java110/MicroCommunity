package com.java110.user.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 绑定业主组件内部之间使用，没有给外围系统提供服务能力
 * 绑定业主服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerAppUserServiceDao {

    /**
     * 保存 绑定业主信息
     * @param businessOwnerAppUserInfo 绑定业主信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerAppUserInfo(Map businessOwnerAppUserInfo) throws DAOException;



    /**
     * 查询绑定业主信息（business过程）
     * 根据bId 查询绑定业主信息
     * @param info bId 信息
     * @return 绑定业主信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerAppUserInfo(Map info) throws DAOException;




    /**
     * 保存 绑定业主信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerAppUserInfoInstance(Map info) throws DAOException;




    /**
     * 查询绑定业主信息（instance过程）
     * 根据bId 查询绑定业主信息
     * @param info bId 信息
     * @return 绑定业主信息
     * @throws DAOException DAO异常
     */
    List<Map> getOwnerAppUserInfo(Map info) throws DAOException;



    /**
     * 修改绑定业主信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOwnerAppUserInfoInstance(Map info) throws DAOException;


    /**
     * 查询绑定业主总数
     *
     * @param info 绑定业主信息
     * @return 绑定业主数量
     */
    int queryOwnerAppUsersCount(Map info);

}
