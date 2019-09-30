package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 访客信息组件内部之间使用，没有给外围系统提供服务能力
 * 访客信息服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IVisitServiceDao {

    /**
     * 保存 访客信息信息
     * @param businessVisitInfo 访客信息信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessVisitInfo(Map businessVisitInfo) throws DAOException;



    /**
     * 查询访客信息信息（business过程）
     * 根据bId 查询访客信息信息
     * @param info bId 信息
     * @return 访客信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessVisitInfo(Map info) throws DAOException;




    /**
     * 保存 访客信息信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveVisitInfoInstance(Map info) throws DAOException;




    /**
     * 查询访客信息信息（instance过程）
     * 根据bId 查询访客信息信息
     * @param info bId 信息
     * @return 访客信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getVisitInfo(Map info) throws DAOException;



    /**
     * 修改访客信息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateVisitInfoInstance(Map info) throws DAOException;


    /**
     * 查询访客信息总数
     *
     * @param info 访客信息信息
     * @return 访客信息数量
     */
    int queryVisitsCount(Map info);

}
