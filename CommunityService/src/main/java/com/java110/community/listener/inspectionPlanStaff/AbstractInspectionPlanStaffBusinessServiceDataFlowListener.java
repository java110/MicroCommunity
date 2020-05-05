package com.java110.community.listener.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanStaffServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行计划人 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionPlanStaffBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionPlanStaffBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionPlanStaffServiceDao getInspectionPlanStaffServiceDaoImpl();

    /**
     * 刷新 businessInspectionPlanStaffInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionPlanStaffInfo
     */
    protected void flushBusinessInspectionPlanStaffInfo(Map businessInspectionPlanStaffInfo, String statusCd) {
        businessInspectionPlanStaffInfo.put("newBId", businessInspectionPlanStaffInfo.get("b_id"));
        businessInspectionPlanStaffInfo.put("operate", businessInspectionPlanStaffInfo.get("operate"));
        businessInspectionPlanStaffInfo.put("createTime", businessInspectionPlanStaffInfo.get("create_time"));
        businessInspectionPlanStaffInfo.put("ipStaffId", businessInspectionPlanStaffInfo.get("ip_staff_id"));
        businessInspectionPlanStaffInfo.put("staffName", businessInspectionPlanStaffInfo.get("staff_name"));
        businessInspectionPlanStaffInfo.put("startTime", businessInspectionPlanStaffInfo.get("start_time"));
        businessInspectionPlanStaffInfo.put("inspectionPlanId", businessInspectionPlanStaffInfo.get("inspection_plan_id"));
        businessInspectionPlanStaffInfo.put("endTime", businessInspectionPlanStaffInfo.get("end_time"));
        businessInspectionPlanStaffInfo.put("communityId", businessInspectionPlanStaffInfo.get("community_id"));
        businessInspectionPlanStaffInfo.put("staffId", businessInspectionPlanStaffInfo.get("staff_id"));
        businessInspectionPlanStaffInfo.remove("bId");
        businessInspectionPlanStaffInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionPlanStaff 执行计划人信息
     */
    protected void autoSaveDelBusinessInspectionPlanStaff(Business business, JSONObject businessInspectionPlanStaff) {
//自动插入DEL
        Map info = new HashMap();
        info.put("ipStaffId", businessInspectionPlanStaff.getString("ipStaffId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionPlanStaffInfos = getInspectionPlanStaffServiceDaoImpl().getInspectionPlanStaffInfo(info);
        if (currentInspectionPlanStaffInfos == null || currentInspectionPlanStaffInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionPlanStaffInfo = currentInspectionPlanStaffInfos.get(0);

        currentInspectionPlanStaffInfo.put("bId", business.getbId());

        currentInspectionPlanStaffInfo.put("operate", currentInspectionPlanStaffInfo.get("operate"));
        currentInspectionPlanStaffInfo.put("createTime", currentInspectionPlanStaffInfo.get("create_time"));
        currentInspectionPlanStaffInfo.put("ipStaffId", currentInspectionPlanStaffInfo.get("ip_staff_id"));
        currentInspectionPlanStaffInfo.put("staffName", currentInspectionPlanStaffInfo.get("staff_name"));
        currentInspectionPlanStaffInfo.put("startTime", currentInspectionPlanStaffInfo.get("start_time"));
        currentInspectionPlanStaffInfo.put("inspectionPlanId", currentInspectionPlanStaffInfo.get("inspection_plan_id"));
        currentInspectionPlanStaffInfo.put("endTime", currentInspectionPlanStaffInfo.get("end_time"));
        currentInspectionPlanStaffInfo.put("communityId", currentInspectionPlanStaffInfo.get("community_id"));
        currentInspectionPlanStaffInfo.put("staffId", currentInspectionPlanStaffInfo.get("staff_id"));


        currentInspectionPlanStaffInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionPlanStaffServiceDaoImpl().saveBusinessInspectionPlanStaffInfo(currentInspectionPlanStaffInfo);
    }


}
