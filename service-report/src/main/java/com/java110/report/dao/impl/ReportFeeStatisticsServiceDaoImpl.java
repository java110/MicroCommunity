package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportCommunityServiceDaoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:15
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@Service("reportFeeStatisticsServiceDaoImpl")
public class ReportFeeStatisticsServiceDaoImpl extends BaseServiceDao implements IReportFeeStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeStatisticsServiceDaoImpl.class);

    @Override
    public double getHisMonthOweFee(Map info) {
        logger.debug("查询历史欠费 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getHisMonthOweFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("hisOweFee").toString());
    }

    @Override
    public double getCurMonthOweFee(Map info) {
        logger.debug("查询单月欠费 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getCurMonthOweFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("curOweFee").toString());
    }

    @Override
    public double getOweFee(Map info) {
        logger.debug("查询单月欠费 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getOweFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("oweFee").toString());
    }

    @Override
    public double getCurReceivableFee(Map info) {
        logger.debug("查询单月欠费 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getCurReceivableFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("curReceivableFee").toString());
    }


    /**
     * 查询欠费追回
     *
     * @param info
     * @return
     */
    @Override
    public double getHisReceivedFee(Map info) {
        logger.debug("查询 欠费追回 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getHisReceivedFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("hisReceivedFee").toString());
    }

    /**
     * 查询预交费用
     *
     * @param info
     * @return
     */
    @Override
    public double getPreReceivedFee(Map info) {
        logger.debug("查询 预交费用 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getPreReceivedFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("preReceivedFee").toString());
    }

    /**
     * 实收费用
     *
     * @param info
     * @return
     */
    @Override
    public double getReceivedFee(Map info) {
        logger.debug("查询 预交费用 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getReceivedFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("receivedFee").toString());
    }

    @Override
    public int getOweRoomCount(Map info) {
        logger.debug("查询 欠费户数 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getOweRoomCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("oweRoomCount").toString());
    }


    @Override
    public int getFeeRoomCount(Map info) {
        logger.debug("查询 收费户数 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getFeeRoomCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("feeRoomCount").toString());
    }

    @Override
    public List<Map> getFloorFeeSummary(Map info) {
        logger.debug("查询 楼栋收费率 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getFloorFeeSummary", info);


        return infos;
    }

    @Override
    public List<Map> getConfigFeeSummary(Map info) {
        logger.debug("查询 费用项收费率 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getConfigFeeSummary", info);


        return infos;
    }

    @Override
    public int getObjFeeSummaryCount(Map info) {
        logger.debug("查询 收费户数 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getObjFeeSummaryCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("total").toString());
    }

    @Override
    public List<Map> getObjFeeSummary(Map info) {
        logger.debug("查询 房屋明细表 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getObjFeeSummary", info);


        return infos;
    }

    @Override
    public List<Map> getOwnerFeeSummary(Map info) {
        logger.debug("查询 业主明细表 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getOwnerFeeSummary", info);


        return infos;
    }

    /**
     * 查询优惠费用
     *
     * @param info
     * @return
     */
    @Override
    public double getDiscountFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getDiscountFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("discountFee").toString());
    }

    /**
     * 查询滞纳金
     *
     * @param info
     * @return
     */
    @Override
    public double getLateFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getLateFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("lateFee").toString());
    }

    /**
     * 查询预存账户
     *
     * @param info
     * @return
     */
    @Override
    public double getPrestoreAccount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getPrestoreAccount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("prestoreAccount").toString());
    }

    /**
     * 查询扣款
     *
     * @param info
     * @return
     */
    @Override
    public double getWithholdAccount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getWithholdAccount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("withholdAccount").toString());
    }

    @Override
    public double getTempCarFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getTempCarFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("tempCarFee").toString());
    }

    /**
     * 押金 退还
     *
     * @param info
     * @return
     */
    @Override
    public double geRefundDeposit(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.geRefundDeposit", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("refundDeposit").toString());
    }

    @Override
    public double geRefundOrderCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.geRefundOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("refundOrderCount").toString());
    }

    @Override
    public double geRefundFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.geRefundFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("refundFee").toString());
    }

    @Override
    public double getChargeFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getChargeFee", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("chargeFee").toString());
    }

    @Override
    public List<Map> getReceivedFeeByFloor(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getReceivedFeeByFloor", info);
        return infos;
    }

    @Override
    public List<Map> getReceivedFeeByPrimeRate(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getReceivedFeeByPrimeRate", info);
        return infos;
    }

    @Override
    public List<Map> getOweFeeByFloor(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getOweFeeByFloor", info);
        return infos;
    }

    /**
     * 查询欠费对象
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> getObjOweFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getObjOweFee", info);
        return infos;
    }

    @Override
    public long getReceivedRoomCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getReceivedRoomCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public double getReceivedRoomAmount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getReceivedRoomAmount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("amount").toString());
    }

    @Override
    public long getHisOweReceivedRoomCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getHisOweReceivedRoomCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public double getHisOweReceivedRoomAmount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getHisOweReceivedRoomAmount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("amount").toString());
    }

    @Override
    public List<Map> getObjReceivedFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFeeStatisticsServiceDaoImpl.getObjReceivedFee", info);


        return infos;

    }

}
