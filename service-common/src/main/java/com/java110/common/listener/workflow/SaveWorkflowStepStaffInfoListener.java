package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.workflow.WorkflowStepStaffPo;
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
 * 保存 工作流节点信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveWorkflowStepStaffInfoListener")
@Transactional
public class SaveWorkflowStepStaffInfoListener extends AbstractWorkflowStepStaffBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveWorkflowStepStaffInfoListener.class);

    @Autowired
    private IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF;
    }

    /**
     * 保存工作流节点信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessWorkflowStepStaff 节点
        if (data.containsKey(WorkflowStepStaffPo.class.getSimpleName())) {
            Object bObj = data.get(WorkflowStepStaffPo.class.getSimpleName());
            JSONArray businessWorkflowStepStaffs = null;
            if (bObj instanceof JSONObject) {
                businessWorkflowStepStaffs = new JSONArray();
                businessWorkflowStepStaffs.add(bObj);
            } else {
                businessWorkflowStepStaffs = (JSONArray) bObj;
            }
            //JSONObject businessWorkflowStepStaff = data.getJSONObject(WorkflowStepStaffPo.class.getSimpleName());
            for (int bWorkflowStepStaffIndex = 0; bWorkflowStepStaffIndex < businessWorkflowStepStaffs.size(); bWorkflowStepStaffIndex++) {
                JSONObject businessWorkflowStepStaff = businessWorkflowStepStaffs.getJSONObject(bWorkflowStepStaffIndex);
                doBusinessWorkflowStepStaff(business, businessWorkflowStepStaff);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("wssId", businessWorkflowStepStaff.getString("wssId"));
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

        //工作流节点信息
        List<Map> businessWorkflowStepStaffInfo = workflowStepStaffServiceDaoImpl.getBusinessWorkflowStepStaffInfo(info);
        if (businessWorkflowStepStaffInfo != null && businessWorkflowStepStaffInfo.size() > 0) {
            reFreshShareColumn(info, businessWorkflowStepStaffInfo.get(0));
            workflowStepStaffServiceDaoImpl.saveWorkflowStepStaffInfoInstance(info);
            if (businessWorkflowStepStaffInfo.size() == 1) {
                dataFlowContext.addParamOut("wssId", businessWorkflowStepStaffInfo.get(0).get("wss_id"));
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
        //工作流节点信息
        List<Map> workflowStepStaffInfo = workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo(info);
        if (workflowStepStaffInfo != null && workflowStepStaffInfo.size() > 0) {
            reFreshShareColumn(paramIn, workflowStepStaffInfo.get(0));
            workflowStepStaffServiceDaoImpl.updateWorkflowStepStaffInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessWorkflowStepStaff 节点
     *
     * @param business                  总的数据节点
     * @param businessWorkflowStepStaff 工作流节点节点
     */
    private void doBusinessWorkflowStepStaff(Business business, JSONObject businessWorkflowStepStaff) {

        Assert.jsonObjectHaveKey(businessWorkflowStepStaff, "wssId", "businessWorkflowStepStaff 节点下没有包含 wssId 节点");

        if (businessWorkflowStepStaff.getString("wssId").startsWith("-")) {
            //刷新缓存
            //flushWorkflowStepStaffId(business.getDatas());

            businessWorkflowStepStaff.put("wssId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wssId));

        }

        businessWorkflowStepStaff.put("bId", business.getbId());
        businessWorkflowStepStaff.put("operate", StatusConstant.OPERATE_ADD);
        //保存工作流节点信息
        workflowStepStaffServiceDaoImpl.saveBusinessWorkflowStepStaffInfo(businessWorkflowStepStaff);

    }

    @Override
    public IWorkflowStepStaffServiceDao getWorkflowStepStaffServiceDaoImpl() {
        return workflowStepStaffServiceDaoImpl;
    }

    public void setWorkflowStepStaffServiceDaoImpl(IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl) {
        this.workflowStepStaffServiceDaoImpl = workflowStepStaffServiceDaoImpl;
    }
}
