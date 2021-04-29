package com.java110.store.dao;

import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 物品使用记录组件内部之间使用，没有给外围系统提供服务能力
 * 物品使用记录服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IResourceStoreUseRecordServiceDao {

    /**
     * 保存 物品使用记录信息
     *
     * @param businessResourceStoreUseRecordInfo 物品使用记录信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessResourceStoreUseRecordInfo(Map businessResourceStoreUseRecordInfo) throws DAOException;


    /**
     * 查询物品使用记录信息（business过程）
     * 根据bId 查询物品使用记录信息
     *
     * @param info bId 信息
     * @return 物品使用记录信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessResourceStoreUseRecordInfo(Map info) throws DAOException;


    /**
     * 保存 物品使用记录信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveResourceStoreUseRecordInfoInstance(Map info) throws DAOException;


    /**
     * 查询物品使用记录信息（instance过程）
     * 根据bId 查询物品使用记录信息
     *
     * @param info bId 信息
     * @return 物品使用记录信息
     * @throws DAOException DAO异常
     */
    List<Map> getResourceStoreUseRecordInfo(Map info) throws DAOException;


    /**
     * 修改物品使用记录信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateResourceStoreUseRecordInfoInstance(Map info) throws DAOException;


    /**
     * 查询物品使用记录总数
     *
     * @param info 物品使用记录信息
     * @return 物品使用记录数量
     */
    int queryResourceStoreUseRecordsCount(Map info);

}
