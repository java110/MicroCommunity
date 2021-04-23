package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 个人物品组件内部之间使用，没有给外围系统提供服务能力
 * 个人物品服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IUserStorehouseServiceDao {

    /**
     * 保存 个人物品信息
     * @param businessUserStorehouseInfo 个人物品信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessUserStorehouseInfo(Map businessUserStorehouseInfo) throws DAOException;



    /**
     * 查询个人物品信息（business过程）
     * 根据bId 查询个人物品信息
     * @param info bId 信息
     * @return 个人物品信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessUserStorehouseInfo(Map info) throws DAOException;




    /**
     * 保存 个人物品信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveUserStorehouseInfoInstance(Map info) throws DAOException;




    /**
     * 查询个人物品信息（instance过程）
     * 根据bId 查询个人物品信息
     * @param info bId 信息
     * @return 个人物品信息
     * @throws DAOException DAO异常
     */
    List<Map> getUserStorehouseInfo(Map info) throws DAOException;



    /**
     * 修改个人物品信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateUserStorehouseInfoInstance(Map info) throws DAOException;


    /**
     * 查询个人物品总数
     *
     * @param info 个人物品信息
     * @return 个人物品数量
     */
    int queryUserStorehousesCount(Map info);

    int saveUserStorehouses(Map beanCovertMap);

}
