package com.java110.common.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 广告信息组件内部之间使用，没有给外围系统提供服务能力
 * 广告信息服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IAdvertServiceDao {

    /**
     * 保存 广告信息信息
     *
     * @param businessAdvertInfo 广告信息信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAdvertInfo(Map businessAdvertInfo) throws DAOException;

    /**
     * 查询广告信息信息（business过程）
     * 根据bId 查询广告信息信息
     *
     * @param info bId 信息
     * @return 广告信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAdvertInfo(Map info) throws DAOException;

    /**
     * 保存 广告信息信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAdvertInfoInstance(Map info) throws DAOException;

    /**
     * 查询广告信息信息（instance过程）
     * 根据bId 查询广告信息信息
     *
     * @param info bId 信息
     * @return 广告信息信息
     * @throws DAOException DAO异常
     */
    List<Map> getAdvertInfo(Map info) throws DAOException;

    /**
     * 修改广告信息信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAdvertInfoInstance(Map info) throws DAOException;

    /**
     * 查询广告信息总数
     *
     * @param info 广告信息信息
     * @return 广告信息数量
     */
    int queryAdvertsCount(Map info);

    /**
     * 修改广告信息
     *
     * @param info 修改信息
     * @return
     */
    void updateAdverts(Map info);
}
