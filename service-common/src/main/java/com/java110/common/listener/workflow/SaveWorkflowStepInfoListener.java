package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.workflow.WorkflowStepPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 工作流节点信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveWorkflowStepInfoListener")
@Transactional
public class SaveWorkflowStepInfoListener extends AbstractWorkflowStepBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveWorkflowStepInfoListener.class);

    @Autowired
    private IWorkflowStepServiceDao workflowStepServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP;
    }

    /**
     * 保存工作流节点信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessWorkflowStep 节点
        if(data.containsKey(WorkflowStepPo.class.getSimpleName())){
            Object bObj = data.get(WorkflowStepPo.class.getSimpleName());
            JSONArray businessWorkflowSteps = null;
            if(bObj instanceof JSONObject){
                businessWorkflowSteps = new JSONArray();
                businessWorkflowSteps.add(bObj);
            }else {
                businessWorkflowSteps = (JSONArray)bObj;
            }
            //JSONObject businessWorkflowStep = data.getJSONObject(WorkflowStepPo.class.getSimpleName());
            for (int bWorkflowStepIndex = 0; bWorkflowStepIndex < businessWorkflowSteps.size();bWorkflowStepIndex++) {
                JSONObject businessWorkflowStep = businessWorkflowSteps.getJSONObject(bWorkflowStepIndex);
                doBusinessWorkflowStep(business, businessWorkflowStep);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("flowId", businessWorkflowStep.getString("flowId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //工作流节点信息
        List<Map> businessWorkflowStepInfo = workflowStepServiceDaoImpl.getBusinessWorkflowStepInfo(info);
        if( businessWorkflowStepInfo != null && businessWorkflowStepInfo.size() >0) {
            reFreshShareColumn(info, businessWorkflowStepInfo.get(0));
            workflowStepServiceDaoImpl.saveWorkflowStepInfoInstance(info);
            if(businessWorkflowStepInfo.size() == 1) {
                dataFlowContext.addParamOut("flowId", businessWorkflowStepInfo.get(0).get("flow_id"));
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
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //工作流节点信息
        List<Map> workflowStepInfo = workflowStepServiceDaoImpl.getWorkflowStepInfo(info);
        if(workflowStepInfo != null && workflowStepInfo.size() > 0){
            reFreshShareColumn(paramIn, workflowStepInfo.get(0));
            workflowStepServiceDaoImpl.updateWorkflowStepInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessWorkflowStep 节点
     * @param business 总的数据节点
     * @param businessWorkflowStep 工作流节点节点
     */
    private void doBusinessWorkflowStep(Business business,JSONObject businessWorkflowStep){

        Assert.jsonObjectHaveKey(businessWorkflowStep,"stepId","businessWorkflowStep 节点下没有包含 flowId 节点");

        if(businessWorkflowStep.getString("stepId").startsWith("-")){
            //刷新缓存
            //flushWorkflowStepId(business.getDatas());

            businessWorkflowStep.put("stepId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));

        }

        businessWorkflowStep.put("bId",business.getbId());
        businessWorkflowStep.put("operate", StatusConstant.OPERATE_ADD);
        //保存工作流节点信息
        workflowStepServiceDaoImpl.saveBusinessWorkflowStepInfo(businessWorkflowStep);

    }
    @Override
    public IWorkflowStepServiceDao getWorkflowStepServiceDaoImpl() {
        return workflowStepServiceDaoImpl;
    }

    public void setWorkflowStepServiceDaoImpl(IWorkflowStepServiceDao workflowStepServiceDaoImpl) {
        this.workflowStepServiceDaoImpl = workflowStepServiceDaoImpl;
    }
}
