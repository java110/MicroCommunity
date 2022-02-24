package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
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
 * 工作流节点 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractWorkflowStepStaffBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractWorkflowStepStaffBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IWorkflowStepStaffServiceDao getWorkflowStepStaffServiceDaoImpl();

    /**
     * 刷新 businessWorkflowStepStaffInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessWorkflowStepStaffInfo
     */
    protected void flushBusinessWorkflowStepStaffInfo(Map businessWorkflowStepStaffInfo, String statusCd) {
        businessWorkflowStepStaffInfo.put("newBId", businessWorkflowStepStaffInfo.get("b_id"));
        businessWorkflowStepStaffInfo.put("wssId", businessWorkflowStepStaffInfo.get("wss_id"));
        businessWorkflowStepStaffInfo.put("operate", businessWorkflowStepStaffInfo.get("operate"));
        businessWorkflowStepStaffInfo.put("stepId", businessWorkflowStepStaffInfo.get("step_id"));
        businessWorkflowStepStaffInfo.put("staffName", businessWorkflowStepStaffInfo.get("staff_name"));
        businessWorkflowStepStaffInfo.put("communityId", businessWorkflowStepStaffInfo.get("community_id"));
        businessWorkflowStepStaffInfo.put("staffId", businessWorkflowStepStaffInfo.get("staff_id"));
        businessWorkflowStepStaffInfo.put("staffRole", businessWorkflowStepStaffInfo.get("staff_role"));
        businessWorkflowStepStaffInfo.put("flowType", businessWorkflowStepStaffInfo.get("flow_type"));
        businessWorkflowStepStaffInfo.remove("bId");
        businessWorkflowStepStaffInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessWorkflowStepStaff 工作流节点信息
     */
    protected void autoSaveDelBusinessWorkflowStepStaff(Business business, JSONObject businessWorkflowStepStaff) {
//自动插入DEL
        Map info = new HashMap();
        info.put("wssId", businessWorkflowStepStaff.getString("wssId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentWorkflowStepStaffInfos = getWorkflowStepStaffServiceDaoImpl().getWorkflowStepStaffInfo(info);
        if (currentWorkflowStepStaffInfos == null || currentWorkflowStepStaffInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentWorkflowStepStaffInfo = currentWorkflowStepStaffInfos.get(0);

        currentWorkflowStepStaffInfo.put("bId", business.getbId());

        currentWorkflowStepStaffInfo.put("wssId", currentWorkflowStepStaffInfo.get("wss_id"));
        currentWorkflowStepStaffInfo.put("operate", currentWorkflowStepStaffInfo.get("operate"));
        currentWorkflowStepStaffInfo.put("stepId", currentWorkflowStepStaffInfo.get("step_id"));
        currentWorkflowStepStaffInfo.put("staffName", currentWorkflowStepStaffInfo.get("staff_name"));
        currentWorkflowStepStaffInfo.put("communityId", currentWorkflowStepStaffInfo.get("community_id"));
        currentWorkflowStepStaffInfo.put("staffId", currentWorkflowStepStaffInfo.get("staff_id"));
        currentWorkflowStepStaffInfo.put("staffRole", currentWorkflowStepStaffInfo.get("staff_role"));
        currentWorkflowStepStaffInfo.put("flowType", currentWorkflowStepStaffInfo.get("flow_type"));


        currentWorkflowStepStaffInfo.put("operate", StatusConstant.OPERATE_DEL);
        getWorkflowStepStaffServiceDaoImpl().saveBusinessWorkflowStepStaffInfo(currentWorkflowStepStaffInfo);
        for (Object key : currentWorkflowStepStaffInfo.keySet()) {
            if (businessWorkflowStepStaff.get(key) == null) {
                businessWorkflowStepStaff.put(key.toString(), currentWorkflowStepStaffInfo.get(key));
            }
        }
    }


}
