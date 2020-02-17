package com.java110.community.listener.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检计划 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionPlanBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionPlanBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionPlanServiceDao getInspectionPlanServiceDaoImpl();

    /**
     * 刷新 businessInspectionPlanInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionPlanInfo
     */
    protected void flushBusinessInspectionPlanInfo(Map businessInspectionPlanInfo, String statusCd) {
        businessInspectionPlanInfo.put("newBId", businessInspectionPlanInfo.get("bId"));
        businessInspectionPlanInfo.put("inspectionPlanName", businessInspectionPlanInfo.get("inspectionPlanName"));
        businessInspectionPlanInfo.put("inspectionRouteId", businessInspectionPlanInfo.get("inspectionRouteId"));
        businessInspectionPlanInfo.put("inspectionPlanPeriod", businessInspectionPlanInfo.get("inspectionPlanPeriod"));
        businessInspectionPlanInfo.put("remark", businessInspectionPlanInfo.get("remark"));
        businessInspectionPlanInfo.put("endTime", businessInspectionPlanInfo.get("endTime"));
        businessInspectionPlanInfo.put("operate", businessInspectionPlanInfo.get("operate"));
        businessInspectionPlanInfo.put("staffName", businessInspectionPlanInfo.get("staffName"));
        businessInspectionPlanInfo.put("signType", businessInspectionPlanInfo.get("signType"));
        businessInspectionPlanInfo.put("startTime", businessInspectionPlanInfo.get("startTime"));
        businessInspectionPlanInfo.put("createUserId", businessInspectionPlanInfo.get("createUserId"));
        businessInspectionPlanInfo.put("createUserName", businessInspectionPlanInfo.get("createUserName"));
        businessInspectionPlanInfo.put("inspectionPlanId", businessInspectionPlanInfo.get("inspectionPlanId"));
        businessInspectionPlanInfo.put("state", businessInspectionPlanInfo.get("state"));
        businessInspectionPlanInfo.put("communityId", businessInspectionPlanInfo.get("communityId"));
        businessInspectionPlanInfo.put("staffId", businessInspectionPlanInfo.get("staffId"));
        businessInspectionPlanInfo.put("staffName", businessInspectionPlanInfo.get("staffName"));
        businessInspectionPlanInfo.remove("bId");
        businessInspectionPlanInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionPlan 巡检计划信息
     */
    protected void autoSaveDelBusinessInspectionPlan(Business business, JSONObject businessInspectionPlan) {
//自动插入DEL
        Map info = new HashMap();
        info.put("inspectionPlanId", businessInspectionPlan.getString("inspectionPlanId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionPlanInfos = getInspectionPlanServiceDaoImpl().getInspectionPlanInfo(info);
        if (currentInspectionPlanInfos == null || currentInspectionPlanInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionPlanInfo = currentInspectionPlanInfos.get(0);

        currentInspectionPlanInfo.put("bId", business.getbId());

        currentInspectionPlanInfo.put("inspectionPlanName", businessInspectionPlan.get("inspectionPlanName"));
        currentInspectionPlanInfo.put("inspectionRouteId", businessInspectionPlan.get("inspectionRouteId"));
        currentInspectionPlanInfo.put("inspectionPlanPeriod", businessInspectionPlan.get("inspectionPlanPeriod"));
        currentInspectionPlanInfo.put("remark", businessInspectionPlan.get("remark"));
        currentInspectionPlanInfo.put("endTime", businessInspectionPlan.get("endTime"));
        currentInspectionPlanInfo.put("operate", businessInspectionPlan.get("operate"));
        currentInspectionPlanInfo.put("staffName", businessInspectionPlan.get("staffName"));
        currentInspectionPlanInfo.put("signType", businessInspectionPlan.get("signType"));
        currentInspectionPlanInfo.put("startTime", businessInspectionPlan.get("startTime"));
        currentInspectionPlanInfo.put("createUserId", businessInspectionPlan.get("createUserId"));
        currentInspectionPlanInfo.put("createUserName", businessInspectionPlan.get("createUserName"));
        currentInspectionPlanInfo.put("inspectionPlanId", businessInspectionPlan.get("inspectionPlanId"));
        currentInspectionPlanInfo.put("state", businessInspectionPlan.get("state"));
        currentInspectionPlanInfo.put("communityId", businessInspectionPlan.get("communityId"));
        currentInspectionPlanInfo.put("staffId", businessInspectionPlan.get("staffId"));
        currentInspectionPlanInfo.put("staffName", businessInspectionPlan.get("staffName"));
        currentInspectionPlanInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionPlanServiceDaoImpl().saveBusinessInspectionPlanInfo(currentInspectionPlanInfo);
    }


}
