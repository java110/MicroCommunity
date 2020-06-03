package com.java110.job.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 定时任务组件内部之间使用，没有给外围系统提供服务能力
 * 定时任务服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface ITaskServiceDao {

    /**
     * 保存 定时任务信息
     *
     * @param businessTaskInfo 定时任务信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessTaskInfo(Map businessTaskInfo) throws DAOException;


    /**
     * 查询定时任务信息（business过程）
     * 根据bId 查询定时任务信息
     *
     * @param info bId 信息
     * @return 定时任务信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessTaskInfo(Map info) throws DAOException;


    /**
     * 保存 定时任务信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveTaskInfoInstance(Map info) throws DAOException;


    /**
     * 查询定时任务信息（instance过程）
     * 根据bId 查询定时任务信息
     *
     * @param info bId 信息
     * @return 定时任务信息
     * @throws DAOException DAO异常
     */
    List<Map> getTaskInfo(Map info) throws DAOException;


    /**
     * 修改定时任务信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateTaskInfoInstance(Map info) throws DAOException;


    /**
     * 查询定时任务总数
     *
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    int queryTasksCount(Map info);


    /**
     * 查询定时任务模板总数
     *
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    int queryTaskTemplateCount(Map info);

    /**
     * 查询定时任务信息（instance过程）
     * 根据bId 查询定时任务信息
     *
     * @param info bId 信息
     * @return 定时任务信息
     * @throws DAOException DAO异常
     */
    List<Map> getTaskTemplateInfo(Map info) throws DAOException;


    /**
     * 查询定时任务数量
     *
     * @param info 定时任务信息
     * @return 定时任务数量
     */
    public int queryTaskTemplateSpecCount(Map info);

    /**
     * 查询定时任务信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    public List<Map> getTaskTemplateSpecInfo(Map info) throws DAOException;

}
