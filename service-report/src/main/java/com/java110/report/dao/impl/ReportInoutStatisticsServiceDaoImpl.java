package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.report.dao.IReportInoutStatisticsServiceDao;
import com.java110.report.dao.IReportOrderStatisticsServiceDao;
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
@Service("reportInoutStatisticsServiceDaoImpl")
public class ReportInoutStatisticsServiceDaoImpl extends BaseServiceDao implements IReportInoutStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportInoutStatisticsServiceDaoImpl.class);


    @Override
    public long getCarInCount(Map info) {
        logger.debug("查询 getCarInCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.getCarInCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long getCarOutCount(Map info) {
        logger.debug("查询 getCarOutCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.getCarOutCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long getPersonInCount(Map info) {
        logger.debug("查询 getPersonInCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.getPersonInCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long getPersonFaceToMachineCount(Map info) {
        logger.debug("查询 getPersonFaceToMachineCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.getPersonFaceToMachineCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long purchaseInCount(Map info) {
        logger.debug("查询 purchaseInCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.purchaseInCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }
        Number num = Float.parseFloat(infos.get(0).get("count").toString());
        long value = Long.valueOf(num.intValue());
        return value;
    }

    @Override
    public long purchaseOutCount(Map info) {
        logger.debug("查询 purchaseOutCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.purchaseOutCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        Number num = Float.parseFloat(infos.get(0).get("count").toString());
        long value = Long.valueOf(num.intValue());
        return value;
    }

    @Override
    public double purchaseInAmount(Map info) {
        logger.debug("查询 purchaseInAmount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.purchaseInAmount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("amount").toString());
    }

    @Override
    public double purchaseOutAmount(Map info) {
        logger.debug("查询 purchaseOutAmount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.purchaseOutAmount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("amount").toString());
    }

    @Override
    public long allocationCount(Map info) {
        logger.debug("查询 allocationCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.allocationCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        Number num = Float.parseFloat(infos.get(0).get("count").toString());
        long value = Long.valueOf(num.intValue());
        return value;
    }

    @Override
    public long roomRenovationCount(Map info) {
        logger.debug("查询 roomRenovationCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.roomRenovationCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long itemReleaseCount(Map info) {
        logger.debug("查询 itemReleaseCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.itemReleaseCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long roomInCount(Map info) {
        logger.debug("查询 roomInCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.roomInCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long roomOutCount(Map info) {
        logger.debug("查询 roomOutCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.roomOutCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long ownerRegisterCount(Map info) {
        logger.debug("查询 ownerRegisterCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.ownerRegisterCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long noAttendanceCount(Map info) {
        logger.debug("查询 noAttendanceCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportInoutStatisticsServiceDaoImpl.noAttendanceCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }
}
