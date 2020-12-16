package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用月统计服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportFeeMonthStatisticsServiceDaoImpl")
//@Transactional
public class ReportFeeMonthStatisticsServiceDaoImpl extends BaseServiceDao implements IReportFeeMonthStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeMonthStatisticsServiceDaoImpl.class);


    /**
     * 保存费用月统计信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportFeeMonthStatisticsInfo(Map info) throws DAOException {
        logger.debug("保存费用月统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("reportFeeMonthStatisticsServiceDaoImpl.saveReportFeeMonthStatisticsInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用月统计信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeMonthStatisticsInfo(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getReportFeeMonthStatisticsInfo", info);

        return businessReportFeeMonthStatisticsInfos;
    }


    /**
     * 修改费用月统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeMonthStatisticsInfo(Map info) throws DAOException {
        logger.debug("修改费用月统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改费用月统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeMonthStatisticsOwe(Map info) throws DAOException {
        logger.debug("修改费用月统计信息Instance 入参 info : {}", info);

        sqlSessionTemplate.update("reportFeeMonthStatisticsServiceDaoImpl.updateReportFeeMonthStatisticsOwe", info);


    }

    /**
     * 查询费用月统计数量
     *
     * @param info 费用月统计信息
     * @return 费用月统计数量
     */
    @Override
    public int queryReportFeeMonthStatisticssCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeMonthStatisticssCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public int queryReportFeeSummaryCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportFeeSummary(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummary", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryReportFloorUnitFeeSummaryCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportFloorUnitFeeSummary(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummary", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryFeeBreakdownCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFeeBreakdown(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdown", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryFeeDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFeeDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryOweFeeDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryOweFeeDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public Map queryPayFeeDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return null;
        }

        return businessReportFeeMonthStatisticsInfos.get(0);
    }

    @Override
    public List<Map> queryPayFeeDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryDeadlineFeeCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFeeCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryDeadlineFee(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryDeadlineFee", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryPrePaymentNewCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentNewCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryPrePayment(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPrePayment", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryPrePaymentCount(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPrePaymentCount", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryDeadlinePaymentCount(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryDeadlinePaymentCount", info);

        return businessReportFeeMonthStatisticsInfos;
    }


    @Override
    public List<Map> queryOwePaymentCount(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryOwePaymentCount", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryFinishOweFee(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFinishOweFee", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryAllPaymentCount(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryAllPaymentCount", info);

        return businessReportFeeMonthStatisticsInfos;    }


}
