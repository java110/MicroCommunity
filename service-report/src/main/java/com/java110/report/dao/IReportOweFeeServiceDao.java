package com.java110.report.dao;


import com.java110.dto.reportOweFee.ReportOweFeeItemDto;
import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 欠费统计组件内部之间使用，没有给外围系统提供服务能力
 * 欠费统计服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IReportOweFeeServiceDao {


    /**
     * 保存 欠费统计信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveReportOweFeeInfo(Map info) throws DAOException;


    /**
     * 查询欠费统计信息（instance过程）
     * 根据bId 查询欠费统计信息
     *
     * @param info bId 信息
     * @return 欠费统计信息
     * @throws DAOException DAO异常
     */
    List<Map> getReportOweFeeInfo(Map info) throws DAOException;


    /**
     * 修改欠费统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateReportOweFeeInfo(Map info) throws DAOException;

    void deleteReportOweFeeInfo(Map info) throws DAOException;


    /**
     * 查询欠费统计总数
     *
     * @param info 欠费统计信息
     * @return 欠费统计数量
     */
    int queryReportOweFeesCount(Map info);

    List<Map> queryReportAllOweFees(Map beanCovertMap);

    /**
     * 查询欠费(与房屋关联)
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> queryReportAllOweFeesByRoom(Map beanCovertMap);

    /**
     * 查询欠费(与车辆关联)
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> queryReportAllOweFeesByCar(Map beanCovertMap);
    /**
     * 查询欠费(与合同关联)
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> queryReportAllOweFeesByContract(Map beanCovertMap);

    double computeReportOweFeeTotalAmount(Map beanCovertMap);

    List<Map> computeReportOweFeeItemAmount(Map beanCovertMap);

    int deleteInvalidFee(Map reportFeeDto);

    List<Map> queryInvalidOweFee(Map reportFeeDto);

    List<Map> queryOweFeesByOwnerIds(Map info);

    List<Map> queryOweFeesByRoomIds(Map info);
}
