package com.java110.agent.dao;


import com.java110.common.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 代理商组件内部之间使用，没有给外围系统提供服务能力
 * 代理商服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAgentServiceDao {

    /**
     * 保存 代理商信息
     * @param businessAgentInfo 代理商信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentInfo(Map businessAgentInfo) throws DAOException;

    /**
     * 保存代理商属性
     * @param businessAgentAttr 代理商属性信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentAttr(Map businessAgentAttr) throws DAOException;


    /**
     * 保存代理商照片信息
     * @param businessAgentPhoto 代理商照片
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentPhoto(Map businessAgentPhoto) throws DAOException;

    /**
     * 保存代理商证件信息
     * @param businessAgentCerdentials 代理商证件
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessAgentCerdentials(Map businessAgentCerdentials) throws DAOException;

    /**
     * 查询代理商信息（business过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getBusinessAgentInfo(Map info) throws DAOException;


    /**
     * 查询代理商属性信息（business过程）
     * @param info bId 信息
     * @return 代理商属性
     * @throws DAOException
     */
    public List<Map> getBusinessAgentAttrs(Map info) throws DAOException;


    /**
     * 查询代理商照片
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getBusinessAgentPhoto(Map info) throws DAOException;


    /**
     * 查询代理商证件信息
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getBusinessAgentCerdentials(Map info) throws DAOException;

    /**
     * 保存 代理商信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentInfoInstance(Map info) throws DAOException;


    /**
     * 保存 代理商属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 代理商照片信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentPhotoInstance(Map info) throws DAOException;


    /**
     * 保存 代理商证件信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void saveAgentCerdentialsInstance(Map info) throws DAOException;



    /**
     * 查询代理商信息（instance过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getAgentInfo(Map info) throws DAOException;


    /**
     * 查询代理商属性信息（instance过程）
     * @param info bId 信息
     * @return 代理商属性
     * @throws DAOException
     */
    public List<Map> getAgentAttrs(Map info) throws DAOException;


    /**
     * 查询代理商照片（instance 过程）
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getAgentPhoto(Map info) throws DAOException;

    /**
     * 查询代理商证件信息（instance 过程）
     * @param info bId 信息
     * @return 代理商照片
     * @throws DAOException
     */
    public List<Map> getAgentCerdentials(Map info) throws DAOException;

    /**
     * 修改代理商信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentInfoInstance(Map info) throws DAOException;


    /**
     * 修改代理商属性信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentAttrInstance(Map info) throws DAOException;


    /**
     * 修改代理商照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentPhotoInstance(Map info) throws DAOException;

    /**
     * 修改代理商证件信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateAgentCerdentailsInstance(Map info) throws DAOException;


    /**
     * 代理商成员加入信息
     * @param businessMemberAgent 代理商成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessMemberAgent(Map businessMemberAgent) throws DAOException;

    /**
     * 成员加入 保存信息至instance
     * @param info
     * @throws DAOException
     */
    public void saveMemberAgentInstance(Map info) throws DAOException;

    /**
     * 查询代理商成员加入信息（business过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getBusinessMemberAgent(Map info) throws DAOException;

    /**
     * 查询代理商成员加入信息（instance过程）
     * 根据bId 查询代理商信息
     * @param info bId 信息
     * @return 代理商信息
     * @throws DAOException
     */
    public Map getMemberAgent(Map info) throws DAOException;

    /**
     * 修改代理商成员加入信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateMemberAgentInstance(Map info) throws DAOException;

}