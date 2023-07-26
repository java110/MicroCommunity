package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.report.dao.IReportFeeMonthStatisticsPrepaymentServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 预付期费用月统计服务 与数据库交互
 *
 * @author fqz
 * @date 2023-03-29 10:50
 */
@Service("reportFeeMonthStatisticsPrepaymentServiceDaoImpl")
public class ReportFeeMonthStatisticsPrepaymentServiceDaoImpl extends BaseServiceDao implements IReportFeeMonthStatisticsPrepaymentServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeMonthStatisticsPrepaymentServiceDaoImpl.class);

    @Override
    public void saveReportFeeMonthStatisticsPrepaymentInfo(Map info) {
        logger.debug("保存预付期费用月统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.saveReportFeeMonthStatisticsPrepaymentInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存预付期费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void updateReportFeeMonthStatisticsPrepaymentInfo(Map info) {
        logger.debug("修改预付期费用月统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改预付期费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public int queryReportFeeMonthStatisticsPrepaymentCount(Map info) {
        logger.debug("查询预付期费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryReportFeeMonthStatisticsPrepaymentCount", info);
        if (businessReportFeeMonthStatisticsPrepaymentInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsPrepaymentInfos.get(0).get("count").toString());
    }

    @Override
    public void deleteReportFeeMonthStatisticsPrepaymentInfo(Map info) {
        logger.debug("deleteReportFeeMonthStatisticsPrepaymentInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.deleteReportFeeMonthStatisticsPrepaymentInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改预付期费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> queryInvalidFeeMonthStatisticsPrepayment(Map info) {
        logger.debug("查询押金退费总金额信息 入参 info : {}", info);

        List<Map> deposits = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryInvalidFeeMonthStatistics", info);

        return deposits;
    }

    @Override
    public int deleteInvalidFee(Map info) {
        logger.debug("deleteInvalidFee 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.deleteInvalidFee", info);

        return saveFlag;
    }

    /**
     * 查询费用月统计信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeMonthStatisticsPrepaymentInfo(Map info) throws DAOException {
        logger.debug("查询预付期费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReportFeeMonthStatisticsPrepaymentInfo", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryPrepaymentConfigs(Map info) throws DAOException {
        logger.debug("查询预付期费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPrepaymentConfigs", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryAllPrepaymentConfigs(Map info) throws DAOException {
        logger.debug("查询预付期费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPrepaymentConfigs", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryAllPrepaymentDiscounts(Map info) throws DAOException {
        logger.debug("查询预付期费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPrepaymentDiscounts", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public double getReceivedAmountByMonth(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getReceivedAmountByMonth", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }
        return Double.parseDouble(businessReportFeeMonthStatisticsInfos.get(0).get("total").toString());
    }

    /**
     * 修改预付费费用月统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeMonthStatisticsPrepaymentOwe(Map info) throws DAOException {
        logger.debug("修改预付期费用月统计信息Instance 入参 info : {}", info);
        sqlSessionTemplate.update("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.updateReportFeeMonthStatisticsPrepaymentOwe", info);
    }

    @Override
    public List<Map> queryFinishOweFee(Map info) throws DAOException {
        logger.debug("查询预付期费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryFinishOweFee", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryPrePayment(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPrePayment", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public Map queryPayFeeDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetailCount", info);
        if (businessReportFeeMonthStatisticsPrepaymentInfos.size() < 1) {
            return null;
        }

        return businessReportFeeMonthStatisticsPrepaymentInfos.get(0);
    }

    @Override
    public Map queryReportCollectFeesCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryReportCollectFeesCount", info);
        if (businessReportFeeMonthStatisticsPrepaymentInfos.size() < 1) {
            return null;
        }

        return businessReportFeeMonthStatisticsPrepaymentInfos.get(0);
    }

    @Override
    public List<Map> queryPayFeeDetail(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetail", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryNewPayFeeDetail(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryNewPayFeeDetail", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryAllPayFeeDetail(Map info) throws DAOException {
        logger.debug("查询费用总数月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryAllPayFeeDetail", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> queryPayFeeDetailSum(Map info) {
        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryPayFeeDetailSum", info);
        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }

    @Override
    public List<Map> getFeeConfigInfo(Map info) throws DAOException {
        logger.debug("查询费用配置信息 入参 info : {}", info);

        List<Map> businessFeeConfigInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.getFeeConfigInfo", info);

        return businessFeeConfigInfos;
    }

    @Override
    public List<Map> queryRoomAndParkingSpace(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsPrepaymentInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsPrepaymentServiceDaoImpl.queryRoomAndParkingSpace", info);

        return businessReportFeeMonthStatisticsPrepaymentInfos;
    }
}
