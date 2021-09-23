package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 小区相关设置组件内部之间使用，没有给外围系统提供服务能力
 * 小区相关设置服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface ICommunitySettingServiceDao {


    /**
     * 保存 小区相关设置信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCommunitySettingInfo(Map info) throws DAOException;


    /**
     * 查询小区相关设置信息（instance过程）
     * 根据bId 查询小区相关设置信息
     *
     * @param info bId 信息
     * @return 小区相关设置信息
     * @throws DAOException DAO异常
     */
    List<Map> getCommunitySettingInfo(Map info) throws DAOException;


    /**
     * 修改小区相关设置信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCommunitySettingInfo(Map info) throws DAOException;


    /**
     * 查询小区相关设置总数
     *
     * @param info 小区相关设置信息
     * @return 小区相关设置数量
     */
    int queryCommunitySettingsCount(Map info);

}
