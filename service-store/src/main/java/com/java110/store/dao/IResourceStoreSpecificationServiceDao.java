package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 物品规格组件内部之间使用，没有给外围系统提供服务能力
 * 物品规格服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IResourceStoreSpecificationServiceDao {

    /**
     * 保存 物品规格信息
     *
     * @param businessResourceStoreSpecificationInfo 物品规格信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessResourceStoreSpecificationInfo(Map businessResourceStoreSpecificationInfo) throws DAOException;


    /**
     * 查询物品规格信息（business过程）
     * 根据bId 查询物品规格信息
     *
     * @param info bId 信息
     * @return 物品规格信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessResourceStoreSpecificationInfo(Map info) throws DAOException;


    /**
     * 保存 物品规格信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveResourceStoreSpecificationInfoInstance(Map info) throws DAOException;


    /**
     * 查询物品规格信息（instance过程）
     * 根据bId 查询物品规格信息
     *
     * @param info bId 信息
     * @return 物品规格信息
     * @throws DAOException DAO异常
     */
    List<Map> getResourceStoreSpecificationInfo(Map info) throws DAOException;


    /**
     * 修改物品规格信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateResourceStoreSpecificationInfoInstance(Map info) throws DAOException;


    /**
     * 查询物品规格总数
     *
     * @param info 物品规格信息
     * @return 物品规格数量
     */
    int queryResourceStoreSpecificationsCount(Map info);

}
