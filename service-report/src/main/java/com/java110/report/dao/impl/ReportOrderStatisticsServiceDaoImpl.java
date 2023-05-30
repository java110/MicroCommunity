package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.report.dao.IReportFeeStatisticsServiceDao;
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
@Service("reportOrderStatisticsServiceDaoImpl")
public class ReportOrderStatisticsServiceDaoImpl extends BaseServiceDao implements IReportOrderStatisticsServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportOrderStatisticsServiceDaoImpl.class);

    /**
     * 查询投诉工单数
     *
     * @param info
     * @return
     */
    @Override
    public double getComplaintOrderCount(Map info) {
        logger.debug("查询 getComplaintOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getComplaintOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("complaintCount").toString());
    }

    @Override
    public double getUndoComplaintOrderCount(Map info) {
        logger.debug("查询 getUndoComplaintOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getUndoComplaintOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("complaintCount").toString());
    }

    @Override
    public double getFinishComplaintOrderCount(Map info) {
        logger.debug("查询 getFinishComplaintOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getFinishComplaintOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("complaintCount").toString());
    }

    @Override
    public double getRepairOrderCount(Map info) {
        logger.debug("查询 getRepairOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getRepairOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("repairCount").toString());
    }

    @Override
    public double getUndoRepairOrderCount(Map info) {
        logger.debug("查询 getUndoRepairOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getUndoRepairOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("repairCount").toString());
    }

    @Override
    public double getFinishRepairOrderCount(Map info) {
        logger.debug("查询 getFinishRepairOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getFinishRepairOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("repairCount").toString());
    }

    @Override
    public double getInspectionOrderCount(Map info) {
        logger.debug("查询 getInspectionOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getInspectionOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("inspectionCount").toString());
    }

    @Override
    public double getUndoInspectionOrderCount(Map info) {
        logger.debug("查询 getUndoInspectionOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getUndoInspectionOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("inspectionCount").toString());
    }

    @Override
    public double getFinishInspectionOrderCount(Map info) {
        logger.debug("查询 getFinishInspectionOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getFinishInspectionOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("inspectionCount").toString());
    }

    @Override
    public double getMaintainanceOrderCount(Map info) {
        logger.debug("查询 getMaintainanceOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getMaintainanceOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("maintainanceCount").toString());
    }

    @Override
    public double getUndoMaintainanceOrderCount(Map info) {
        logger.debug("查询 getUndoMaintainanceOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getUndoMaintainanceOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("maintainanceCount").toString());
    }

    @Override
    public double getFinishMaintainanceOrderCount(Map info) {
        logger.debug("查询 getFinishMaintainanceOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getFinishMaintainanceOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("maintainanceCount").toString());
    }

    @Override
    public double getNotepadOrderCount(Map info) {
        logger.debug("查询 getNotepadOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getNotepadOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("notepadCount").toString());
    }

    @Override
    public double getChargeMachineOrderCount(Map info) {
        logger.debug("查询 getChargeMachineOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getChargeMachineOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("chargeMachineCount").toString());
    }

    @Override
    public double getChargeMonthOrderCount(Map info) {
        logger.debug("查询 getChargeMonthOrderCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getChargeMonthOrderCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Double.parseDouble(infos.get(0).get("chargeMonthMoney").toString());
    }

    @Override
    public int getOwnerReserveGoodsCount(Map info) {
        logger.debug("查询 getOwnerReserveGoodsCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getOwnerReserveGoodsCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getOwnerReserveGoods(Map info) {
        logger.debug("查询 getOwnerReserveGoods 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getOwnerReserveGoods", info);

        return infos;
    }

    @Override
    public int getOwnerDiningCount(Map info) {
        logger.debug("查询 getOwnerDiningCount 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getOwnerDiningCount", info);

        if (infos == null || infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getOwnerDinings(Map info) {
        logger.debug("查询 getOwnerDinings 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> infos = sqlSessionTemplate.selectList("reportOrderStatisticsServiceDaoImpl.getOwnerDinings", info);

        return infos;
    }

}
