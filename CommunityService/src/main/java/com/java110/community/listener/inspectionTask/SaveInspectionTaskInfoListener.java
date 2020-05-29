package com.java110.community.listener.inspectionTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.IInspectionTaskServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 活动信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveInspectionTaskInfoListener")
@Transactional
public class SaveInspectionTaskInfoListener extends AbstractInspectionTaskBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveInspectionTaskInfoListener.class);

    @Autowired
    private IInspectionTaskServiceDao inspectionTaskServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK;
    }

    /**
     * 保存活动信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionTask 节点
        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK)) {
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK);
            JSONArray businessInspectionTasks = null;
            if (bObj instanceof JSONObject) {
                businessInspectionTasks = new JSONArray();
                businessInspectionTasks.add(bObj);
            } else {
                businessInspectionTasks = (JSONArray) bObj;
            }
            //JSONObject businessInspectionTask = data.getJSONObject("businessInspectionTask");
            for (int bInspectionTaskIndex = 0; bInspectionTaskIndex < businessInspectionTasks.size(); bInspectionTaskIndex++) {
                JSONObject businessInspectionTask = businessInspectionTasks.getJSONObject(bInspectionTaskIndex);
                doBusinessInspectionTask(business, businessInspectionTask);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("taskId", businessInspectionTask.getString("taskId"));
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

        //活动信息
        List<Map> businessInspectionTaskInfo = inspectionTaskServiceDaoImpl.getBusinessInspectionTaskInfo(info);
        if (businessInspectionTaskInfo != null && businessInspectionTaskInfo.size() > 0) {
            reFreshShareColumn(info, businessInspectionTaskInfo.get(0));
            inspectionTaskServiceDaoImpl.saveInspectionTaskInfoInstance(info);
            if (businessInspectionTaskInfo.size() == 1) {
                dataFlowContext.addParamOut("taskId", businessInspectionTaskInfo.get(0).get("task_id"));
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
        //活动信息
        List<Map> inspectionTaskInfo = inspectionTaskServiceDaoImpl.getInspectionTaskInfo(info);
        if (inspectionTaskInfo != null && inspectionTaskInfo.size() > 0) {
            reFreshShareColumn(paramIn, inspectionTaskInfo.get(0));
            inspectionTaskServiceDaoImpl.updateInspectionTaskInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessInspectionTask 节点
     *
     * @param business               总的数据节点
     * @param businessInspectionTask 活动节点
     */
    private void doBusinessInspectionTask(Business business, JSONObject businessInspectionTask) {

        Assert.jsonObjectHaveKey(businessInspectionTask, "taskId", "businessInspectionTask 节点下没有包含 taskId 节点");

        if (businessInspectionTask.getString("taskId").startsWith("-")) {
            //刷新缓存
            //flushInspectionTaskId(business.getDatas());

            businessInspectionTask.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));

        }

        businessInspectionTask.put("bId", business.getbId());
        businessInspectionTask.put("operate", StatusConstant.OPERATE_ADD);
        //保存活动信息
        inspectionTaskServiceDaoImpl.saveBusinessInspectionTaskInfo(businessInspectionTask);

    }

    public IInspectionTaskServiceDao getInspectionTaskServiceDaoImpl() {
        return inspectionTaskServiceDaoImpl;
    }

    public void setInspectionTaskServiceDaoImpl(IInspectionTaskServiceDao inspectionTaskServiceDaoImpl) {
        this.inspectionTaskServiceDaoImpl = inspectionTaskServiceDaoImpl;
    }
}
