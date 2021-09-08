package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 小区组件内部之间使用，没有给外围系统提供服务能力
 * 小区服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface ICommunityServiceDao {

    /**
     * 保存 小区信息
     *
     * @param businessCommunityInfo 小区信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommunityInfo(Map businessCommunityInfo) throws DAOException;

    /**
     * 保存小区属性
     *
     * @param businessCommunityAttr 小区属性信息封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommunityAttr(Map businessCommunityAttr) throws DAOException;


    /**
     * 保存小区照片信息
     *
     * @param businessCommunityPhoto 小区照片
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommunityPhoto(Map businessCommunityPhoto) throws DAOException;

    /**
     * 保存小区证件信息
     *
     * @param businessCommunityCerdentials 小区证件
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommunityCerdentials(Map businessCommunityCerdentials) throws DAOException;

    /**
     * 查询小区信息（business过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    Map getBusinessCommunityInfo(Map info) throws DAOException;


    /**
     * 查询小区属性信息（business过程）
     *
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
    List<Map> getBusinessCommunityAttrs(Map info) throws DAOException;


    /**
     * 查询小区照片
     *
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
    List<Map> getBusinessCommunityPhoto(Map info) throws DAOException;


    /**
     * 查询小区证件信息
     *
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
    List<Map> getBusinessCommunityCerdentials(Map info) throws DAOException;

    /**
     * 保存 小区信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    void saveCommunityInfoInstance(Map info) throws DAOException;


    /**
     * 保存 小区属性信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    void saveCommunityAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 小区照片信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    void saveCommunityPhotoInstance(Map info) throws DAOException;


    /**
     * 查询小区信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    Map getCommunityInfo(Map info) throws DAOException;


    /**
     * 查询小区属性信息（instance过程）
     *
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
    List<Map> getCommunityAttrs(Map info) throws DAOException;


    /**
     * 查询小区照片（instance 过程）
     *
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
    List<Map> getCommunityPhoto(Map info) throws DAOException;


    /**
     * 修改小区信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateCommunityInfoInstance(Map info) throws DAOException;


    /**
     * 修改小区属性信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateCommunityAttrInstance(Map info) throws DAOException;


    /**
     * 修改小区照片信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateCommunityPhotoInstance(Map info) throws DAOException;


    /**
     * 小区成员加入信息
     *
     * @param businessCommunityMember 小区成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommunityMember(Map businessCommunityMember) throws DAOException;

    /**
     * 成员加入 保存信息至instance
     *
     * @param info
     * @throws DAOException
     */
    void saveCommunityMemberInstance(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（business过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    List<Map> getBusinessCommunityMember(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    List<Map> getCommunityMember(Map info) throws DAOException;

    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateCommunityMemberInstance(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    List<Map> getCommunityMembers(Map info) throws DAOException;

    /**
     * 查询小区成员个数
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
    int getCommunityMemberCount(Map info);

    /**
     * 查询小区信息（instance过程）
     * 根据bId 查询小区信息
     *
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException DAO异常
     */
    List<Map> getCommunityInfoNew(Map info) throws DAOException;


    /**
     * 查询小区总数
     *
     * @param info 小区信息
     * @return 小区数量
     */
    int queryCommunitysCount(Map info);

    /**
     * 查询小区属性
     *
     * @param info
     * @return
     */
    int getCommunityAttrsCount(Map info);


    List<Map> getStoreCommunitys(Map beanCovertMap);


    /**
     * 添加属性
     *
     * @param info
     * @return
     */
    int saveCommunityAttr(Map info);
}