package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowServiceDao;
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
 * 工作流 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractWorkflowBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractWorkflowBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IWorkflowServiceDao getWorkflowServiceDaoImpl();

    /**
     * 刷新 businessWorkflowInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessWorkflowInfo
     */
    protected void flushBusinessWorkflowInfo(Map businessWorkflowInfo, String statusCd) {
        businessWorkflowInfo.put("newBId", businessWorkflowInfo.get("b_id"));
        businessWorkflowInfo.put("skipLevel", businessWorkflowInfo.get("skip_level"));
        businessWorkflowInfo.put("operate", businessWorkflowInfo.get("operate"));
        businessWorkflowInfo.put("describle", businessWorkflowInfo.get("describle"));
        businessWorkflowInfo.put("communityId", businessWorkflowInfo.get("community_id"));
        businessWorkflowInfo.put("storeId", businessWorkflowInfo.get("store_id"));
        businessWorkflowInfo.put("flowId", businessWorkflowInfo.get("flow_id"));
        businessWorkflowInfo.put("flowName", businessWorkflowInfo.get("flow_name"));
        businessWorkflowInfo.put("flowType", businessWorkflowInfo.get("flow_type"));
        businessWorkflowInfo.remove("bId");
        businessWorkflowInfo.put("statusCd", statusCd);
        businessWorkflowInfo.put("processDefinitionKey", businessWorkflowInfo.get("process_definition_key"));

    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessWorkflow 工作流信息
     */
    protected void autoSaveDelBusinessWorkflow(Business business, JSONObject businessWorkflow) {
//自动插入DEL
        Map info = new HashMap();
        info.put("flowId", businessWorkflow.getString("flowId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentWorkflowInfos = getWorkflowServiceDaoImpl().getWorkflowInfo(info);
        if (currentWorkflowInfos == null || currentWorkflowInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentWorkflowInfo = currentWorkflowInfos.get(0);

        currentWorkflowInfo.put("bId", business.getbId());

        currentWorkflowInfo.put("skipLevel", currentWorkflowInfo.get("skip_level"));
        currentWorkflowInfo.put("operate", currentWorkflowInfo.get("operate"));
        currentWorkflowInfo.put("describle", currentWorkflowInfo.get("describle"));
        currentWorkflowInfo.put("communityId", currentWorkflowInfo.get("community_id"));
        currentWorkflowInfo.put("storeId", currentWorkflowInfo.get("store_id"));
        currentWorkflowInfo.put("flowId", currentWorkflowInfo.get("flow_id"));
        currentWorkflowInfo.put("flowName", currentWorkflowInfo.get("flow_name"));
        currentWorkflowInfo.put("flowType", currentWorkflowInfo.get("flow_type"));
        currentWorkflowInfo.put("processDefinitionKey", currentWorkflowInfo.get("process_definition_key"));


        currentWorkflowInfo.put("operate", StatusConstant.OPERATE_DEL);
        getWorkflowServiceDaoImpl().saveBusinessWorkflowInfo(currentWorkflowInfo);
        for (Object key : currentWorkflowInfo.keySet()) {
            if (businessWorkflow.get(key) == null) {
                businessWorkflow.put(key.toString(), currentWorkflowInfo.get(key));
            }
        }
    }


}
