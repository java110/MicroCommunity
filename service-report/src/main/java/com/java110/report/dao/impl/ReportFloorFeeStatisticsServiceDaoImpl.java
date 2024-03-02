package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
import com.java110.report.dao.IReportFloorFeeStatisticsServiceDao;
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
@Service("reportFloorFeeStatisticsServiceDaoImpl")
public class ReportFloorFeeStatisticsServiceDaoImpl extends BaseServiceDao implements IReportFloorFeeStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFloorFeeStatisticsServiceDaoImpl.class);



    @Override
    public List<Map> getFloorOweRoomCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorOweRoomCount", info);
        return infos;
    }

    @Override
    public List<Map> getFloorFeeRoomCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorFeeRoomCount", info);
        return infos;
    }

    @Override
    public List<Map> getFloorReceivedFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorReceivedFee", info);
        return infos;
    }

    @Override
    public List<Map> getFloorPreReceivedFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorPreReceivedFee", info);
        return infos;
    }

    @Override
    public List<Map> getFloorHisOweFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorHisOweFee", info);
        return infos;
    }

    @Override
    public List<Map> getFloorCurReceivableFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorCurReceivableFee", info);
        return infos;
    }

    @Override
    public List<Map> getFloorCurReceivedFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorCurReceivedFee", info);
        return infos;
    }

    @Override
    public List<Map> getFloorHisReceivedFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportFloorFeeStatisticsServiceDaoImpl.getFloorHisReceivedFee", info);
        return infos;
    }
}
