package com.java110.report.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 业主缴费明细组件内部之间使用，没有给外围系统提供服务能力
 * 业主缴费明细服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IReportOwnerPayFeeServiceDao {


    /**
     * 保存 业主缴费明细信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReportOwnerPayFeeInfo(Map info) throws DAOException;




    /**
     * 查询业主缴费明细信息（instance过程）
     * 根据bId 查询业主缴费明细信息
     * @param info bId 信息
     * @return 业主缴费明细信息
     * @throws DAOException DAO异常
     */
    List<Map> getReportOwnerPayFeeInfo(Map info) throws DAOException;



    /**
     * 修改业主缴费明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportOwnerPayFeeInfo(Map info) throws DAOException;


    /**
     * 查询业主缴费明细总数
     *
     * @param info 业主缴费明细信息
     * @return 业主缴费明细数量
     */
    int queryReportOwnerPayFeesCount(Map info);

    /**
     * 查询 月缴费
     * @param info
     * @return
     */
    List<Map> queryReportOwnerMonthPayFees(Map info);
}
