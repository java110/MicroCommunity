package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 服务组件内部之间使用，没有给外围系统提供服务能力
 * 服务服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IServiceServiceDao {

    /**
     * 保存 服务信息
     * @param businessServiceInfo 服务信息 封装
     * @throws DAOException 操作数据库异常
     */
    int saveServiceInfo(Map businessServiceInfo) throws DAOException;



    /**
     * 查询服务信息（business过程）
     * 根据bId 查询服务信息
     * @param info bId 信息
     * @return 服务信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessServiceInfo(Map info) throws DAOException;




    /**
     * 保存 服务信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveServiceInfoInstance(Map info) throws DAOException;




    /**
     * 查询服务信息（instance过程）
     * 根据bId 查询服务信息
     * @param info bId 信息
     * @return 服务信息
     * @throws DAOException DAO异常
     */
    List<Map> getServiceInfo(Map info) throws DAOException;



    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateServiceInfo(Map info) throws DAOException;


    /**
     * 查询服务总数
     *
     * @param info 服务信息
     * @return 服务数量
     */
    int queryServicesCount(Map info);


    /**
     * 保存 服务信息
     * @param businessServiceProvideInfo 服务信息 封装
     * @throws DAOException 操作数据库异常
     */
    int saveServiceProvideInfo(Map businessServiceProvideInfo) throws DAOException;


    /**
     * 查询服务信息（instance过程）
     * 根据bId 查询服务信息
     * @param info bId 信息
     * @return 服务信息
     * @throws DAOException DAO异常
     */
    List<Map> getServiceProvideInfo(Map info) throws DAOException;



    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int updateServiceProvideInfo(Map info) throws DAOException;


    /**
     * 查询服务总数
     *
     * @param info 服务信息
     * @return 服务数量
     */
    int queryServiceProvidesCount(Map info);

}
