package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
@Service("reportFeeServiceDaoImpl")
public class ReportFeeServiceDaoImpl extends BaseServiceDao implements IReportFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeServiceDaoImpl.class);

    @Override
    public int getFeeCount(ReportFeeDto reportFeeDto) {
        logger.debug("查询费用月统计数据 入参 info : {}", JSONObject.toJSONString(reportFeeDto));

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeServiceDaoImpl.getFeeCount", reportFeeDto);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<ReportFeeDto> getFees(ReportFeeDto reportFeeDto) {
        logger.debug("查询费用信息 入参 info : {}", JSONObject.toJSONString(reportFeeDto));

        //List<ReportFeeDto> roomDtos = sqlSessionTemplate.selectList("reportFeeServiceDaoImpl.getFees", reportFeeDto);
        List<Map> roomMaps = sqlSessionTemplate.selectList("reportFeeServiceDaoImpl.getFees", BeanConvertUtil.beanCovertMap(reportFeeDto));
        List<ReportFeeDto> roomDtos = BeanConvertUtil.covertBeanList(roomMaps,ReportFeeDto.class);
        return roomDtos;
    }

    @Override
    public List<Map> getFeeConfigs(Map reportFeeDto) {
        logger.debug("getFeeConfigs 入参 info : {}", JSONObject.toJSONString(reportFeeDto));

        List<Map> feeConfigDtos = sqlSessionTemplate.selectList("reportFeeServiceDaoImpl.getFeeConfigs", reportFeeDto);

        return feeConfigDtos;
    }

    @Override
    public double getFeeReceivedAmount(ReportFeeDetailDto reportFeeDetailDto) {
        logger.debug("查询实收费用 入参 info : {}", JSONObject.toJSONString(reportFeeDetailDto));

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportFeeServiceDaoImpl.getFeeReceivedAmount", reportFeeDetailDto);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        Map receivedAmount = businessReportFeeMonthStatisticsInfos.get(0);

        return Double.parseDouble(receivedAmount == null ? "0" : receivedAmount.get("receivedAmount").toString());
    }
}
