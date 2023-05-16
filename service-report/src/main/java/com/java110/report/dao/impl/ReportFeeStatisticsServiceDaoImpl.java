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


}
