package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 映射组件内部之间使用，没有给外围系统提供服务能力
 * 映射服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IMappingServiceDao {

    /**
     * 保存 映射信息
     *
     * @param businessMappingInfo 映射信息 封装
     * @throws DAOException 操作数据库异常
     */
    int saveMappingInfo(Map businessMappingInfo) throws DAOException;


    /**
     * 查询映射信息（business过程）
     * 根据bId 查询映射信息
     *
     * @param info bId 信息
     * @return 映射信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMappingInfo(Map info) throws DAOException;


    /**
     * 保存 映射信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMappingInfoInstance(Map info) throws DAOException;


    /**
     * 查询映射信息（instance过程）
     * 根据bId 查询映射信息
     *
     * @param info bId 信息
     * @return 映射信息
     * @throws DAOException DAO异常
     */
    List<Map> getMappingInfo(Map info) throws DAOException;


    /**
     * 修改映射信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateMappingInfo(Map info) throws DAOException;


    /**
     * 查询映射总数
     *
     * @param info 映射信息
     * @return 映射数量
     */
    int queryMappingsCount(Map info);

}
