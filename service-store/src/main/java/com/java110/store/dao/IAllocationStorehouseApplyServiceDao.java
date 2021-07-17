package com.java110.store.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 调拨申请组件内部之间使用，没有给外围系统提供服务能力
 * 调拨申请服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IAllocationStorehouseApplyServiceDao {

    /**
     * 保存 调拨申请信息
     *
     * @param businessAllocationStorehouseApplyInfo 调拨申请信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAllocationStorehouseApplyInfo(Map businessAllocationStorehouseApplyInfo) throws DAOException;

    /**
     * 查询调拨申请信息（business过程）
     * 根据bId 查询调拨申请信息
     *
     * @param info bId 信息
     * @return 调拨申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAllocationStorehouseApplyInfo(Map info) throws DAOException;

    /**
     * 保存 调拨申请信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAllocationStorehouseApplyInfoInstance(Map info) throws DAOException;

    /**
     * 保存调拨申请信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAllocationStorehouseApplyInfo(Map info) throws DAOException;

    /**
     * 查询调拨申请信息（instance过程）
     * 根据bId 查询调拨申请信息
     *
     * @param info bId 信息
     * @return 调拨申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getAllocationStorehouseApplyInfo(Map info) throws DAOException;

    /**
     * 修改调拨申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAllocationStorehouseApplyInfoInstance(Map info) throws DAOException;

    /**
     * 查询调拨申请总数
     *
     * @param info 调拨申请信息
     * @return 调拨申请数量
     */
    int queryAllocationStorehouseApplysCount(Map info);

}
