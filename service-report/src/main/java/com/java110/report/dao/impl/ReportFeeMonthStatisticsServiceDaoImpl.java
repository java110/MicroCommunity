package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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


    @Override
    public void deleteReportFeeMonthStatisticsInfo(Map info) {
        logger.debug("deleteReportFeeMonthStatisticsInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsServiceDaoImpl.deleteReportFeeMonthStatisticsInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用月统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public double getReceivedAmountByMonth(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getReceivedAmountByMonth", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(businessReportFeeMonthStatisticsInfos.get(0).get("total").toString());
    }

    @Override
    public List<Map> queryRoomAndParkingSpace(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryRoomAndParkingSpace", info);

        return businessReportFeeMonthStatisticsInfos;
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
    public int queryReportFeeSummaryDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportFeeSummaryDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }



    @Override
    public Map queryReportFeeSummaryMajor(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFeeSummaryMajor", info);

        return businessReportFeeMonthStatisticsInfos.get(0);
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
    public int queryReportFloorUnitFeeSummaryDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportFloorUnitFeeSummaryDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public Map queryReportFloorUnitFeeSummaryMajor(Map info) {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryReportFloorUnitFeeSummaryMajor", info);

        return businessReportFeeMonthStatisticsInfos.get(0);
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
    public int queryFeeBreakdownDetailCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryFeeBreakdownDetail(Map info) throws DAOException {
        logger.debug("查询费用月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownDetail", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public Map queryFeeBreakdownMajor(Map info) {
        logger.debug("查询费用queryFeeBreakdownMajor 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeBreakdownMajor", info);

        return businessReportFeeMonthStatisticsInfos.get(0);
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
    public Map queryOweFeeDetailMajor(Map info) {
        logger.debug("查询费用queryOweFeeDetailMajor 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryOweFeeDetailMajor", info);

        return businessReportFeeMonthStatisticsInfos.get(0);
    }

    @Override
    public int queryHuaningOweFeeCount(Map info) {
        logger.debug("查询queryHuaningOweFeeCount数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHuaningOweFee(Map info) {
        logger.debug("查询queryHuaningOweFee 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFee", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryHuaningPayFeeCount(Map info) {
        logger.debug("查询queryHuaningPayFeeCount数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHuaningPayFee(Map info) {
        logger.debug("查询queryHuaningPayFee 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFee", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryHuaningPayFeeTwoCount(Map info) {
        logger.debug("查询queryHuaningPayFeeTwoCount数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeTwoCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHuaningPayFeeTwo(Map info) {
        logger.debug("查询queryHuaningPayFeeTwo 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningPayFeeTwo", info);

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryHuaningOweFeeDetailCount(Map info) {
        logger.debug("查询queryHuaningOweFeeDetailCount数据 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeDetailCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHuaningOweFeeDetail(Map info) {
        logger.debug("查询queryHuaningOweFeeDetail 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryHuaningOweFeeDetail", info);

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
    public List<Map> queryAllPayFeeDetail(Map info) throws DAOException {
        logger.debug("查询费用总数月统计信息 入参 info : {}", info);

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryAllPayFeeDetail", info);

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

        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryAllFeeDetail(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryAllFeeDetail", info);
        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> queryPayFeeDetailSum(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDetailSum", info);
        return businessReportFeeMonthStatisticsInfos;
    }

    /**
     * 查询费用配置信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeConfigInfo(Map info) throws DAOException {
        logger.debug("查询费用配置信息 入参 info : {}", info);

        List<Map> businessFeeConfigInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getFeeConfigInfo", info);

        return businessFeeConfigInfos;
    }

    @Override
    public List<Map> getRepairUserInfo(Map info) {
        logger.debug("查询报修配置信息 入参 info : {}", info);
        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getRepairInfo", info);
        return businessRepairUserInfos;
    }

    @Override
    public List<Map> getRepairWithOutPage(Map info) {
        logger.debug("查询报修配置信息 入参 info : {}", info);
        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getRepairWithOutPage", info);
        return businessRepairUserInfos;
    }

    @Override
    public List<Map> getRepairStaff(Map info) {
        logger.debug("查询报修配置信息 入参 info : {}", info);
        List<Map> businessRepairUserInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryRepairForStaff", info);
        return businessRepairUserInfos;
    }

    @Override
    public Map getReceivableInformation(Map beanCovertMap) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getReceivableInformation", beanCovertMap);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return null;
        }

        return businessReportFeeMonthStatisticsInfos.get(0);
    }

    @Override
    public List<Map> getFloorReceivableInformation(Map beanCovertMap) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getFloorReceivableInformation", beanCovertMap);
        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public List<Map> getFeeConfigReceivableInformation(Map beanCovertMap) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.getFeeConfigReceivableInformation", beanCovertMap);
        return businessReportFeeMonthStatisticsInfos;
    }

    @Override
    public int queryNoFeeRoomsCount(Map info) {
        logger.debug("查询未收费房屋统计数据 入参 info : {}", info);

        List<Map> roomInfos =
                sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryNoFeeRoomsCount", info);
        if (roomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(roomInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryNoFeeRooms(Map info) {
        logger.debug("查询未收费房屋统计信息 入参 info : {}", info);

        List<Map> roomInfos =
                sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryNoFeeRooms", info);

        return roomInfos;
    }

    @Override
    public List<Map> queryPayFeeDeposit(Map info) {
        logger.debug("查询押金统计信息 入参 info : {}", info);

        List<Map> deposits = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryPayFeeDeposit", info);

        return deposits;
    }

    @Override
    public List<Map> queryFeeDepositAmount(Map info) {
        logger.debug("查询押金退费总金额信息 入参 info : {}", info);

        List<Map> deposits = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryFeeDepositAmount", info);

        return deposits;
    }


    public int deleteInvalidFee(Map info){
        logger.debug("deleteInvalidFee 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthStatisticsServiceDaoImpl.deleteInvalidFee", info);

        return saveFlag;
    }

    @Override
    public List<Map> queryInvalidFeeMonthStatistics(Map info) {
        logger.debug("查询押金退费总金额信息 入参 info : {}", info);

        List<Map> deposits = sqlSessionTemplate.selectList("reportFeeMonthStatisticsServiceDaoImpl.queryInvalidFeeMonthStatistics", info);

        return deposits;
    }


}
