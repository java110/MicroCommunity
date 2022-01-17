package com.java110.common.listener.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAttendanceClassesServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤班次 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAttendanceClassesBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAttendanceClassesBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAttendanceClassesServiceDao getAttendanceClassesServiceDaoImpl();

    /**
     * 刷新 businessAttendanceClassesInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAttendanceClassesInfo
     */
    protected void flushBusinessAttendanceClassesInfo(Map businessAttendanceClassesInfo, String statusCd) {
        businessAttendanceClassesInfo.put("newBId", businessAttendanceClassesInfo.get("b_id"));
        businessAttendanceClassesInfo.put("timeOffset", businessAttendanceClassesInfo.get("time_offset"));
        businessAttendanceClassesInfo.put("clockCount", businessAttendanceClassesInfo.get("clock_count"));
        businessAttendanceClassesInfo.put("classesObjType", businessAttendanceClassesInfo.get("classes_obj_type"));
        businessAttendanceClassesInfo.put("storeId", businessAttendanceClassesInfo.get("store_id"));
        businessAttendanceClassesInfo.put("clockType", businessAttendanceClassesInfo.get("clock_type"));
        businessAttendanceClassesInfo.put("classesObjId", businessAttendanceClassesInfo.get("classes_obj_id"));
        businessAttendanceClassesInfo.put("classesObjName", businessAttendanceClassesInfo.get("classes_obj_name"));
        businessAttendanceClassesInfo.put("classesName", businessAttendanceClassesInfo.get("classes_name"));
        businessAttendanceClassesInfo.put("classesId", businessAttendanceClassesInfo.get("classes_id"));
        businessAttendanceClassesInfo.put("operate", businessAttendanceClassesInfo.get("operate"));
        businessAttendanceClassesInfo.put("leaveOffset", businessAttendanceClassesInfo.get("leave_offset"));
        businessAttendanceClassesInfo.put("lateOffset", businessAttendanceClassesInfo.get("late_offset"));
        businessAttendanceClassesInfo.put("clockTypeValue", businessAttendanceClassesInfo.get("clock_type_value"));
        businessAttendanceClassesInfo.remove("bId");
        businessAttendanceClassesInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAttendanceClasses 考勤班次信息
     */
    protected void autoSaveDelBusinessAttendanceClasses(Business business, JSONObject businessAttendanceClasses) {
//自动插入DEL
        Map info = new HashMap();
        info.put("classesId", businessAttendanceClasses.getString("classesId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAttendanceClassesInfos = getAttendanceClassesServiceDaoImpl().getAttendanceClassesInfo(info);
        if (currentAttendanceClassesInfos == null || currentAttendanceClassesInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAttendanceClassesInfo = currentAttendanceClassesInfos.get(0);

        currentAttendanceClassesInfo.put("bId", business.getbId());

        currentAttendanceClassesInfo.put("timeOffset", currentAttendanceClassesInfo.get("time_offset"));
        currentAttendanceClassesInfo.put("clockCount", currentAttendanceClassesInfo.get("clock_count"));
        currentAttendanceClassesInfo.put("classesObjType", currentAttendanceClassesInfo.get("classes_obj_type"));
        currentAttendanceClassesInfo.put("storeId", currentAttendanceClassesInfo.get("store_id"));
        currentAttendanceClassesInfo.put("clockType", currentAttendanceClassesInfo.get("clock_type"));
        currentAttendanceClassesInfo.put("classesObjId", currentAttendanceClassesInfo.get("classes_obj_id"));
        currentAttendanceClassesInfo.put("classesName", currentAttendanceClassesInfo.get("classes_name"));
        currentAttendanceClassesInfo.put("classesId", currentAttendanceClassesInfo.get("classes_id"));
        currentAttendanceClassesInfo.put("operate", currentAttendanceClassesInfo.get("operate"));
        currentAttendanceClassesInfo.put("leaveOffset", currentAttendanceClassesInfo.get("leave_offset"));
        currentAttendanceClassesInfo.put("lateOffset", currentAttendanceClassesInfo.get("late_offset"));
        currentAttendanceClassesInfo.put("clockTypeValue", currentAttendanceClassesInfo.get("clock_type_value"));
        currentAttendanceClassesInfo.put("classesObjName", currentAttendanceClassesInfo.get("classes_obj_name"));


        currentAttendanceClassesInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAttendanceClassesServiceDaoImpl().saveBusinessAttendanceClassesInfo(currentAttendanceClassesInfo);
        for (Object key : currentAttendanceClassesInfo.keySet()) {
            if (businessAttendanceClasses.get(key) == null) {
                businessAttendanceClasses.put(key.toString(), currentAttendanceClassesInfo.get(key));
            }
        }
    }


}
