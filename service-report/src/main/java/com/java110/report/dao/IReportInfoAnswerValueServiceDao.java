package com.java110.report.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 批量操作日志详情组件内部之间使用，没有给外围系统提供服务能力
 * 批量操作日志详情服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IReportInfoAnswerValueServiceDao {


    /**
     * 保存 批量操作日志详情信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReportInfoAnswerValueInfo(Map info) throws DAOException;




    /**
     * 查询批量操作日志详情信息（instance过程）
     * 根据bId 查询批量操作日志详情信息
     * @param info bId 信息
     * @return 批量操作日志详情信息
     * @throws DAOException DAO异常
     */
    List<Map> getReportInfoAnswerValueInfo(Map info) throws DAOException;



    /**
     * 修改批量操作日志详情信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportInfoAnswerValueInfo(Map info) throws DAOException;


    /**
     * 查询批量操作日志详情总数
     *
     * @param info 批量操作日志详情信息
     * @return 批量操作日志详情数量
     */
    int queryReportInfoAnswerValuesCount(Map info);

}
