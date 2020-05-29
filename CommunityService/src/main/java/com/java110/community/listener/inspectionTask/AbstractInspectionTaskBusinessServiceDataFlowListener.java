package com.java110.community.listener.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionTaskServiceDao;
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
 * 活动 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractInspectionTaskBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractInspectionTaskBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IInspectionTaskServiceDao getInspectionTaskServiceDaoImpl();

    /**
     * 刷新 businessInspectionTaskInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessInspectionTaskInfo
     */
    protected void flushBusinessInspectionTaskInfo(Map businessInspectionTaskInfo, String statusCd) {
        businessInspectionTaskInfo.put("newBId", businessInspectionTaskInfo.get("b_id"));
        businessInspectionTaskInfo.put("planUserId", businessInspectionTaskInfo.get("plan_user_id"));
        businessInspectionTaskInfo.put("actInsTime", businessInspectionTaskInfo.get("act_ins_time"));
        businessInspectionTaskInfo.put("planInsTime", businessInspectionTaskInfo.get("plan_ins_time"));
        businessInspectionTaskInfo.put("actUserName", businessInspectionTaskInfo.get("act_user_name"));
        businessInspectionTaskInfo.put("operate", businessInspectionTaskInfo.get("operate"));
        businessInspectionTaskInfo.put("signType", businessInspectionTaskInfo.get("sign_type"));
        businessInspectionTaskInfo.put("inspectionPlanId", businessInspectionTaskInfo.get("inspection_plan_id"));
        businessInspectionTaskInfo.put("planUserName", businessInspectionTaskInfo.get("plan_user_name"));
        businessInspectionTaskInfo.put("communityId", businessInspectionTaskInfo.get("community_id"));
        businessInspectionTaskInfo.put("actUserId", businessInspectionTaskInfo.get("act_user_id"));
        businessInspectionTaskInfo.put("taskId", businessInspectionTaskInfo.get("task_id"));
        businessInspectionTaskInfo.remove("bId");
        businessInspectionTaskInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessInspectionTask 活动信息
     */
    protected void autoSaveDelBusinessInspectionTask(Business business, JSONObject businessInspectionTask) {
//自动插入DEL
        Map info = new HashMap();
        info.put("taskId", businessInspectionTask.getString("taskId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentInspectionTaskInfos = getInspectionTaskServiceDaoImpl().getInspectionTaskInfo(info);
        if (currentInspectionTaskInfos == null || currentInspectionTaskInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentInspectionTaskInfo = currentInspectionTaskInfos.get(0);

        currentInspectionTaskInfo.put("bId", business.getbId());

        currentInspectionTaskInfo.put("planUserId", currentInspectionTaskInfo.get("plan_user_id"));
        currentInspectionTaskInfo.put("actInsTime", currentInspectionTaskInfo.get("act_ins_time"));
        currentInspectionTaskInfo.put("planInsTime", currentInspectionTaskInfo.get("plan_ins_time"));
        currentInspectionTaskInfo.put("actUserName", currentInspectionTaskInfo.get("act_user_name"));
        currentInspectionTaskInfo.put("operate", currentInspectionTaskInfo.get("operate"));
        currentInspectionTaskInfo.put("signType", currentInspectionTaskInfo.get("sign_type"));
        currentInspectionTaskInfo.put("inspectionPlanId", currentInspectionTaskInfo.get("inspection_plan_id"));
        currentInspectionTaskInfo.put("planUserName", currentInspectionTaskInfo.get("plan_user_name"));
        currentInspectionTaskInfo.put("communityId", currentInspectionTaskInfo.get("community_id"));
        currentInspectionTaskInfo.put("actUserId", currentInspectionTaskInfo.get("act_user_id"));
        currentInspectionTaskInfo.put("taskId", currentInspectionTaskInfo.get("task_id"));


        currentInspectionTaskInfo.put("operate", StatusConstant.OPERATE_DEL);
        getInspectionTaskServiceDaoImpl().saveBusinessInspectionTaskInfo(currentInspectionTaskInfo);

        for (Object key : currentInspectionTaskInfo.keySet()) {
            if (businessInspectionTask.get(key) == null) {
                businessInspectionTask.put(key.toString(), currentInspectionTaskInfo.get(key));
            }
        }
    }


}
