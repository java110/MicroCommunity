package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowStepServiceDao;
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
public abstract class AbstractWorkflowStepBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractWorkflowStepBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IWorkflowStepServiceDao getWorkflowStepServiceDaoImpl();

    /**
     * 刷新 businessWorkflowStepInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessWorkflowStepInfo
     */
    protected void flushBusinessWorkflowStepInfo(Map businessWorkflowStepInfo, String statusCd) {
        businessWorkflowStepInfo.put("newBId", businessWorkflowStepInfo.get("b_id"));
        businessWorkflowStepInfo.put("operate", businessWorkflowStepInfo.get("operate"));
        businessWorkflowStepInfo.put("stepId", businessWorkflowStepInfo.get("step_id"));
        businessWorkflowStepInfo.put("type", businessWorkflowStepInfo.get("type"));
        businessWorkflowStepInfo.put("communityId", businessWorkflowStepInfo.get("community_id"));
        businessWorkflowStepInfo.put("storeId", businessWorkflowStepInfo.get("store_id"));
        businessWorkflowStepInfo.put("flowId", businessWorkflowStepInfo.get("flow_id"));
        businessWorkflowStepInfo.put("seq", businessWorkflowStepInfo.get("seq"));
        businessWorkflowStepInfo.remove("bId");
        businessWorkflowStepInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessWorkflowStep 工作流节点信息
     */
    protected void autoSaveDelBusinessWorkflowStep(Business business, JSONObject businessWorkflowStep) {
//自动插入DEL
        Map info = new HashMap();
        info.put("stepId", businessWorkflowStep.getString("stepId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentWorkflowStepInfos = getWorkflowStepServiceDaoImpl().getWorkflowStepInfo(info);
        if (currentWorkflowStepInfos == null || currentWorkflowStepInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentWorkflowStepInfo = currentWorkflowStepInfos.get(0);

        currentWorkflowStepInfo.put("bId", business.getbId());

        currentWorkflowStepInfo.put("operate", currentWorkflowStepInfo.get("operate"));
        currentWorkflowStepInfo.put("stepId", currentWorkflowStepInfo.get("step_id"));
        currentWorkflowStepInfo.put("type", currentWorkflowStepInfo.get("type"));
        currentWorkflowStepInfo.put("communityId", currentWorkflowStepInfo.get("community_id"));
        currentWorkflowStepInfo.put("storeId", currentWorkflowStepInfo.get("store_id"));
        currentWorkflowStepInfo.put("flowId", currentWorkflowStepInfo.get("flow_id"));
        currentWorkflowStepInfo.put("seq", currentWorkflowStepInfo.get("seq"));


        currentWorkflowStepInfo.put("operate", StatusConstant.OPERATE_DEL);
        getWorkflowStepServiceDaoImpl().saveBusinessWorkflowStepInfo(currentWorkflowStepInfo);
        for (Object key : currentWorkflowStepInfo.keySet()) {
            if (businessWorkflowStep.get(key) == null) {
                businessWorkflowStep.put(key.toString(), currentWorkflowStepInfo.get(key));
            }
        }
    }


}
