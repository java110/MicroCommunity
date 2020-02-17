package com.java110.community.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 巡检计划组件内部之间使用，没有给外围系统提供服务能力
 * 巡检计划服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IInspectionPlanServiceDao {

    /**
     * 保存 巡检计划信息
     * @param businessInspectionPlanInfo 巡检计划信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessInspectionPlanInfo(Map businessInspectionPlanInfo) throws DAOException;



    /**
     * 查询巡检计划信息（business过程）
     * 根据bId 查询巡检计划信息
     * @param info bId 信息
     * @return 巡检计划信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessInspectionPlanInfo(Map info) throws DAOException;




    /**
     * 保存 巡检计划信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveInspectionPlanInfoInstance(Map info) throws DAOException;




    /**
     * 查询巡检计划信息（instance过程）
     * 根据bId 查询巡检计划信息
     * @param info bId 信息
     * @return 巡检计划信息
     * @throws DAOException DAO异常
     */
    List<Map> getInspectionPlanInfo(Map info) throws DAOException;



    /**
     * 修改巡检计划信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateInspectionPlanInfoInstance(Map info) throws DAOException;


    /**
     * 查询巡检计划总数
     *
     * @param info 巡检计划信息
     * @return 巡检计划数量
     */
    int queryInspectionPlansCount(Map info);

}
