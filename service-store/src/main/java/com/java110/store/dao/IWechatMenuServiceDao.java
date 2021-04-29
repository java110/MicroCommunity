package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 公众号菜单组件内部之间使用，没有给外围系统提供服务能力
 * 公众号菜单服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IWechatMenuServiceDao {

    /**
     * 保存 公众号菜单信息
     * @param businessWechatMenuInfo 公众号菜单信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessWechatMenuInfo(Map businessWechatMenuInfo) throws DAOException;



    /**
     * 查询公众号菜单信息（business过程）
     * 根据bId 查询公众号菜单信息
     * @param info bId 信息
     * @return 公众号菜单信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessWechatMenuInfo(Map info) throws DAOException;




    /**
     * 保存 公众号菜单信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveWechatMenuInfoInstance(Map info) throws DAOException;




    /**
     * 查询公众号菜单信息（instance过程）
     * 根据bId 查询公众号菜单信息
     * @param info bId 信息
     * @return 公众号菜单信息
     * @throws DAOException DAO异常
     */
    List<Map> getWechatMenuInfo(Map info) throws DAOException;



    /**
     * 修改公众号菜单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateWechatMenuInfoInstance(Map info) throws DAOException;


    /**
     * 查询公众号菜单总数
     *
     * @param info 公众号菜单信息
     * @return 公众号菜单数量
     */
    int queryWechatMenusCount(Map info);

}
