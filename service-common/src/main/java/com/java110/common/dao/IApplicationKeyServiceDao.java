package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 钥匙申请组件内部之间使用，没有给外围系统提供服务能力
 * 钥匙申请服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IApplicationKeyServiceDao {

    /**
     * 保存 钥匙申请信息
     * @param businessApplicationKeyInfo 钥匙申请信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessApplicationKeyInfo(Map businessApplicationKeyInfo) throws DAOException;



    /**
     * 查询钥匙申请信息（business过程）
     * 根据bId 查询钥匙申请信息
     * @param info bId 信息
     * @return 钥匙申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessApplicationKeyInfo(Map info) throws DAOException;




    /**
     * 保存 钥匙申请信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveApplicationKeyInfoInstance(Map info) throws DAOException;




    /**
     * 查询钥匙申请信息（instance过程）
     * 根据bId 查询钥匙申请信息
     * @param info bId 信息
     * @return 钥匙申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getApplicationKeyInfo(Map info) throws DAOException;



    /**
     * 修改钥匙申请信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateApplicationKeyInfoInstance(Map info) throws DAOException;


    /**
     * 查询钥匙申请总数
     *
     * @param info 钥匙申请信息
     * @return 钥匙申请数量
     */
    int queryApplicationKeysCount(Map info);

}
