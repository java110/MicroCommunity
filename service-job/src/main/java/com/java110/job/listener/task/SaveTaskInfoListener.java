package com.java110.job.listener.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.task.TaskPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.job.dao.ITaskServiceDao;
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
 * 保存 定时任务信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveTaskInfoListener")
@Transactional
public class SaveTaskInfoListener extends AbstractTaskBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveTaskInfoListener.class);

    @Autowired
    private ITaskServiceDao taskServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK;
    }

    /**
     * 保存定时任务信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessTask 节点
        if(data.containsKey(TaskPo.class.getSimpleName())){
            Object bObj = data.get(TaskPo.class.getSimpleName());
            JSONArray businessTasks = null;
            if(bObj instanceof JSONObject){
                businessTasks = new JSONArray();
                businessTasks.add(bObj);
            }else {
                businessTasks = (JSONArray)bObj;
            }
            //JSONObject businessTask = data.getJSONObject(TaskPo.class.getSimpleName());
            for (int bTaskIndex = 0; bTaskIndex < businessTasks.size();bTaskIndex++) {
                JSONObject businessTask = businessTasks.getJSONObject(bTaskIndex);
                doBusinessTask(business, businessTask);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("taskId", businessTask.getString("taskId"));
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

        //定时任务信息
        List<Map> businessTaskInfo = taskServiceDaoImpl.getBusinessTaskInfo(info);
        if( businessTaskInfo != null && businessTaskInfo.size() >0) {
            reFreshShareColumn(info, businessTaskInfo.get(0));
            taskServiceDaoImpl.saveTaskInfoInstance(info);
            if(businessTaskInfo.size() == 1) {
                dataFlowContext.addParamOut("taskId", businessTaskInfo.get(0).get("task_id"));
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

        if (info.containsKey("task_id")) {
            return;
        }

        if (!businessInfo.containsKey("taskId")) {
            return;
        }

        info.put("task_id", businessInfo.get("taskId"));
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
        //定时任务信息
        List<Map> taskInfo = taskServiceDaoImpl.getTaskInfo(info);
        if(taskInfo != null && taskInfo.size() > 0){
            reFreshShareColumn(paramIn, taskInfo.get(0));
            taskServiceDaoImpl.updateTaskInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessTask 节点
     * @param business 总的数据节点
     * @param businessTask 定时任务节点
     */
    private void doBusinessTask(Business business,JSONObject businessTask){

        Assert.jsonObjectHaveKey(businessTask,"taskId","businessTask 节点下没有包含 taskId 节点");

        if(businessTask.getString("taskId").startsWith("-")){
            //刷新缓存
            //flushTaskId(business.getDatas());

            businessTask.put("taskId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));

        }

        businessTask.put("bId",business.getbId());
        businessTask.put("operate", StatusConstant.OPERATE_ADD);
        //保存定时任务信息
        taskServiceDaoImpl.saveBusinessTaskInfo(businessTask);

    }
    @Override
    public ITaskServiceDao getTaskServiceDaoImpl() {
        return taskServiceDaoImpl;
    }

    public void setTaskServiceDaoImpl(ITaskServiceDao taskServiceDaoImpl) {
        this.taskServiceDaoImpl = taskServiceDaoImpl;
    }
}
