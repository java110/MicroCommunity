package com.java110.community.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 执行计划人组件内部之间使用，没有给外围系统提供服务能力
 * 执行计划人服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IInspectionPlanStaffServiceDao {

    /**
     * 保存 执行计划人信息
     * @param businessInspectionPlanStaffInfo 执行计划人信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessInspectionPlanStaffInfo(Map businessInspectionPlanStaffInfo) throws DAOException;



    /**
     * 查询执行计划人信息（business过程）
     * 根据bId 查询执行计划人信息
     * @param info bId 信息
     * @return 执行计划人信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessInspectionPlanStaffInfo(Map info) throws DAOException;




    /**
     * 保存 执行计划人信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveInspectionPlanStaffInfoInstance(Map info) throws DAOException;




    /**
     * 查询执行计划人信息（instance过程）
     * 根据bId 查询执行计划人信息
     * @param info bId 信息
     * @return 执行计划人信息
     * @throws DAOException DAO异常
     */
    List<Map> getInspectionPlanStaffInfo(Map info) throws DAOException;



    /**
     * 修改执行计划人信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateInspectionPlanStaffInfoInstance(Map info) throws DAOException;


    /**
     * 查询执行计划人总数
     *
     * @param info 执行计划人信息
     * @return 执行计划人数量
     */
    int queryInspectionPlanStaffsCount(Map info);

}
