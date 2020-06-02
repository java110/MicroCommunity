package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 服务实现组件内部之间使用，没有给外围系统提供服务能力
 * 服务实现服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IServiceBusinessServiceDao {

    /**
     * 保存 服务实现信息
     * @param businessServiceBusinessInfo 服务实现信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessServiceBusinessInfo(Map businessServiceBusinessInfo) throws DAOException;



    /**
     * 查询服务实现信息（business过程）
     * 根据bId 查询服务实现信息
     * @param info bId 信息
     * @return 服务实现信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessServiceBusinessInfo(Map info) throws DAOException;




    /**
     * 保存 服务实现信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveServiceBusinessInfoInstance(Map info) throws DAOException;




    /**
     * 查询服务实现信息（instance过程）
     * 根据bId 查询服务实现信息
     * @param info bId 信息
     * @return 服务实现信息
     * @throws DAOException DAO异常
     */
    List<Map> getServiceBusinessInfo(Map info) throws DAOException;



    /**
     * 修改服务实现信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateServiceBusinessInfoInstance(Map info) throws DAOException;


    /**
     * 查询服务实现总数
     *
     * @param info 服务实现信息
     * @return 服务实现数量
     */
    int queryServiceBusinesssCount(Map info);

    /**
     * 保存服务实现
     *
     * @param info 数据对象分享
     * @return 小区下的小区楼记录数
     */
    int saveServiceBusiness(Map info);

    /**
     * 修改服务实现
     *
     * @param info 数据对象分享
     * @return 小区下的小区楼记录数
     */
    int updateServiceBusiness(Map info);

    /**
     * 删除服务实现
     *
     * @param info 数据对象分享
     * @return 小区下的小区楼记录数
     */
    int deleteServiceBusiness(Map info);

}
