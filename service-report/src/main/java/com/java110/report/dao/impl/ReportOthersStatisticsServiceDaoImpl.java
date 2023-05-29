package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.report.dao.IReportOrderStatisticsServiceDao;
import com.java110.report.dao.IReportOthersStatisticsServiceDao;
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
@Service("reportOthersStatisticsServiceDaoImpl")
public class ReportOthersStatisticsServiceDaoImpl extends BaseServiceDao implements IReportOthersStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportOthersStatisticsServiceDaoImpl.class);

    public long venueReservationCount(Map info) {
        logger.debug("查询 venueReservationCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.venueReservationCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long contractCount(Map info) {
        logger.debug("查询 contractCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.contractCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long contractChangeCount(Map info) {
        logger.debug("查询 contractChangeCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.contractChangeCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long leaseChangeCount(Map info) {
        logger.debug("查询 leaseChangeCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.leaseChangeCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long mainChange(Map info) {
        logger.debug("查询 mainChange 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.mainChange", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }
        Number num = Float.parseFloat(infos.get(0).get("count").toString());
        long value = Long.valueOf(num.intValue());
        return value;
    }

    @Override
    public long expirationContract(Map info) {
        logger.debug("查询 expirationContract 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.expirationContract", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        Number num = Float.parseFloat(infos.get(0).get("count").toString());
        long value = Long.valueOf(num.intValue());
        return value;
    }

    @Override
    public long carCount(Map info) {
        logger.debug("查询 carCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.carCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public long carApplyCount(Map info) {
        logger.debug("查询 carApplyCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.carApplyCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public double buyParkingCouponCount(Map info) {
        logger.debug("查询 buyParkingCouponCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.buyParkingCouponCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("amount").toString());
    }

    @Override
    public long writeOffParkingCouponCount(Map info) {
        logger.debug("查询 writeOffParkingCouponCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.writeOffParkingCouponCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public double sendCouponCount(Map info) {
        logger.debug("查询 sendCouponCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.sendCouponCount", info);

        if (infos == null || infos.size() < 1) {
            return 0D;
        }

        return Double.parseDouble(infos.get(0).get("count").toString());
    }

    @Override
    public long writeOffCouponCount(Map info) {
        logger.debug("查询 writeOffCouponCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.writeOffCouponCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Long.parseLong(infos.get(0).get("count").toString());
    }

    @Override
    public double sendIntegralCount(Map info) {
        logger.debug("查询 sendIntegralCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.sendIntegralCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("count").toString());
    }

    @Override
    public double writeOffIntegralCount(Map info) {
        logger.debug("查询 writeOffIntegralCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOthersStatisticsServiceDaoImpl.writeOffIntegralCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("count").toString());
    }


}
