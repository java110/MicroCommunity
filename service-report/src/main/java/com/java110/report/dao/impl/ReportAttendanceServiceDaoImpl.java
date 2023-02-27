package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.report.dao.IReportAttendanceServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
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
@Service("reportAttendanceServiceDaoImpl")
public class ReportAttendanceServiceDaoImpl extends BaseServiceDao implements IReportAttendanceServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportAttendanceServiceDaoImpl.class);

    @Override
    public int getMonthAttendanceCount(Map info) {
        logger.debug("查询费用月统计数据 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportAttendanceServiceDaoImpl.getMonthAttendanceCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getMonthAttendance(Map info) {
        logger.debug("查询费用信息 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> roomDtos = sqlSessionTemplate.selectList("reportAttendanceServiceDaoImpl.getMonthAttendance", info);

        return roomDtos;
    }

    @Override
    public List<Map> getMonthAttendanceDetail(Map info) {
        logger.debug("查询费用信息 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> roomDtos = sqlSessionTemplate.selectList("reportAttendanceServiceDaoImpl.getMonthAttendanceDetail", info);

        return roomDtos;
    }

}
