package com.java110.common.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 工作流组件内部之间使用，没有给外围系统提供服务能力
 * 工作流服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IWorkflowServiceDao {

    /**
     * 保存 工作流信息
     * @param businessWorkflowInfo 工作流信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessWorkflowInfo(Map businessWorkflowInfo) throws DAOException;



    /**
     * 查询工作流信息（business过程）
     * 根据bId 查询工作流信息
     * @param info bId 信息
     * @return 工作流信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessWorkflowInfo(Map info) throws DAOException;




    /**
     * 保存 工作流信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveWorkflowInfoInstance(Map info) throws DAOException;




    /**
     * 查询工作流信息（instance过程）
     * 根据bId 查询工作流信息
     * @param info bId 信息
     * @return 工作流信息
     * @throws DAOException DAO异常
     */
    List<Map> getWorkflowInfo(Map info) throws DAOException;



    /**
     * 修改工作流信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateWorkflowInfoInstance(Map info) throws DAOException;


    /**
     * 查询工作流总数
     *
     * @param info 工作流信息
     * @return 工作流数量
     */
    int queryWorkflowsCount(Map info);

}
