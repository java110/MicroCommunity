package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 活动组件内部之间使用，没有给外围系统提供服务能力
 * 活动服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFastuserServiceDao {

    /**
     * 保存 活动信息
     * @param businessFastuserInfo 活动信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFastuserInfo(Map businessFastuserInfo) throws DAOException;



    /**
     * 保存 活动信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveFastuserInfoInstance(Map info) throws DAOException;




    /**
     * 查询活动信息（instance过程）
     * 根据bId 查询活动信息
     * @param info bId 信息
     * @return 活动信息
     * @throws DAOException DAO异常
     */
    List<Map> getFastuserInfo(Map info) throws DAOException;


    /**
     * 查询活动信息（business过程）
     * 根据bId 查询活动信息
     * @param info bId 信息
     * @return 活动信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessFastuserInfo(Map info) throws DAOException;
    /**
     * 查询活动总数
     *
     * @param info 活动信息
     * @return 活动数量
     */

    /**
     * 修改活动信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateFastuserInfoInstance(Map info) throws DAOException;


    int queryFastuserCount(Map info);

}
