package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 通知组件内部之间使用，没有给外围系统提供服务能力
 * 通知服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface INoticeServiceDao {

    /**
     * 保存 通知信息
     * @param businessNoticeInfo 通知信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessNoticeInfo(Map businessNoticeInfo) throws DAOException;



    /**
     * 查询通知信息（business过程）
     * 根据bId 查询通知信息
     * @param info bId 信息
     * @return 通知信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessNoticeInfo(Map info) throws DAOException;




    /**
     * 保存 通知信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveNoticeInfoInstance(Map info) throws DAOException;




    /**
     * 查询通知信息（instance过程）
     * 根据bId 查询通知信息
     * @param info bId 信息
     * @return 通知信息
     * @throws DAOException DAO异常
     */
    List<Map> getNoticeInfo(Map info) throws DAOException;



    /**
     * 修改通知信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateNoticeInfoInstance(Map info) throws DAOException;


    /**
     * 查询通知总数
     *
     * @param info 通知信息
     * @return 通知数量
     */
    int queryNoticesCount(Map info);

}
