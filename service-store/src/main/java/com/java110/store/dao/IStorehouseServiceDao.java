package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 仓库组件内部之间使用，没有给外围系统提供服务能力
 * 仓库服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IStorehouseServiceDao {

    /**
     * 保存 仓库信息
     * @param businessStorehouseInfo 仓库信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessStorehouseInfo(Map businessStorehouseInfo) throws DAOException;



    /**
     * 查询仓库信息（business过程）
     * 根据bId 查询仓库信息
     * @param info bId 信息
     * @return 仓库信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessStorehouseInfo(Map info) throws DAOException;




    /**
     * 保存 仓库信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveStorehouseInfoInstance(Map info) throws DAOException;




    /**
     * 查询仓库信息（instance过程）
     * 根据bId 查询仓库信息
     * @param info bId 信息
     * @return 仓库信息
     * @throws DAOException DAO异常
     */
    List<Map> getStorehouseInfo(Map info) throws DAOException;



    /**
     * 修改仓库信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateStorehouseInfoInstance(Map info) throws DAOException;


    /**
     * 查询仓库总数
     *
     * @param info 仓库信息
     * @return 仓库数量
     */
    int queryStorehousesCount(Map info);

}
