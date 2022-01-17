package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 工作流信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveWorkflowInfoListener")
@Transactional
public class SaveWorkflowInfoListener extends AbstractWorkflowBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveWorkflowInfoListener.class);

    @Autowired
    private IWorkflowServiceDao workflowServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW;
    }

    /**
     * 保存工作流信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessWorkflow 节点
        if (data.containsKey(WorkflowPo.class.getSimpleName())) {
            Object bObj = data.get(WorkflowPo.class.getSimpleName());
            JSONArray businessWorkflows = null;
            if (bObj instanceof JSONObject) {
                businessWorkflows = new JSONArray();
                businessWorkflows.add(bObj);
            } else {
                businessWorkflows = (JSONArray) bObj;
            }
            //JSONObject businessWorkflow = data.getJSONObject(WorkflowPo.class.getSimpleName());
            for (int bWorkflowIndex = 0; bWorkflowIndex < businessWorkflows.size(); bWorkflowIndex++) {
                JSONObject businessWorkflow = businessWorkflows.getJSONObject(bWorkflowIndex);
                doBusinessWorkflow(business, businessWorkflow);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("flowId", businessWorkflow.getString("flowId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //工作流信息
        List<Map> businessWorkflowInfo = workflowServiceDaoImpl.getBusinessWorkflowInfo(info);
        if (businessWorkflowInfo != null && businessWorkflowInfo.size() > 0) {
            reFreshShareColumn(info, businessWorkflowInfo.get(0));
            workflowServiceDaoImpl.saveWorkflowInfoInstance(info);
            if (businessWorkflowInfo.size() == 1) {
                dataFlowContext.addParamOut("flowId", businessWorkflowInfo.get(0).get("flow_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //工作流信息
        List<Map> workflowInfo = workflowServiceDaoImpl.getWorkflowInfo(info);
        if (workflowInfo != null && workflowInfo.size() > 0) {
            reFreshShareColumn(paramIn, workflowInfo.get(0));
            workflowServiceDaoImpl.updateWorkflowInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessWorkflow 节点
     *
     * @param business         总的数据节点
     * @param businessWorkflow 工作流节点
     */
    private void doBusinessWorkflow(Business business, JSONObject businessWorkflow) {

        Assert.jsonObjectHaveKey(businessWorkflow, "flowId", "businessWorkflow 节点下没有包含 flowId 节点");

        if (businessWorkflow.getString("flowId").startsWith("-")) {
            //刷新缓存
            //flushWorkflowId(business.getDatas());

            businessWorkflow.put("flowId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));

        }

        businessWorkflow.put("bId", business.getbId());
        businessWorkflow.put("operate", StatusConstant.OPERATE_ADD);
        //保存工作流信息
        workflowServiceDaoImpl.saveBusinessWorkflowInfo(businessWorkflow);

    }

    @Override
    public IWorkflowServiceDao getWorkflowServiceDaoImpl() {
        return workflowServiceDaoImpl;
    }

    public void setWorkflowServiceDaoImpl(IWorkflowServiceDao workflowServiceDaoImpl) {
        this.workflowServiceDaoImpl = workflowServiceDaoImpl;
    }
}
