package com.java110.oa.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 审核人员组件内部之间使用，没有给外围系统提供服务能力
 * 审核人员服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IAuditUserServiceDao {

    /**
     * 保存 审核人员信息
     * @param businessAuditUserInfo 审核人员信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessAuditUserInfo(Map businessAuditUserInfo) throws DAOException;



    /**
     * 查询审核人员信息（business过程）
     * 根据bId 查询审核人员信息
     * @param info bId 信息
     * @return 审核人员信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessAuditUserInfo(Map info) throws DAOException;




    /**
     * 保存 审核人员信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveAuditUserInfoInstance(Map info) throws DAOException;




    /**
     * 查询审核人员信息（instance过程）
     * 根据bId 查询审核人员信息
     * @param info bId 信息
     * @return 审核人员信息
     * @throws DAOException DAO异常
     */
    List<Map> getAuditUserInfo(Map info) throws DAOException;



    /**
     * 修改审核人员信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateAuditUserInfoInstance(Map info) throws DAOException;


    /**
     * 查询审核人员总数
     *
     * @param info 审核人员信息
     * @return 审核人员数量
     */
    int queryAuditUsersCount(Map info);

    /**
     * 刷新表 委托人
     * @param info
     */
    void freshActHiTaskInstAssignee(Map info);

}
