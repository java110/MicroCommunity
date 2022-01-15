package com.java110.job.listener.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.task.TaskPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.job.dao.ITaskServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改定时任务信息 侦听
 *
 * 处理节点
 * 1、businessTask:{} 定时任务基本信息节点
 * 2、businessTaskAttr:[{}] 定时任务属性信息节点
 * 3、businessTaskPhoto:[{}] 定时任务照片信息节点
 * 4、businessTaskCerdentials:[{}] 定时任务证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateTaskInfoListener")
@Transactional
public class UpdateTaskInfoListener extends AbstractTaskBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateTaskInfoListener.class);
    @Autowired
    private ITaskServiceDao taskServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TASK;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");


            //处理 businessTask 节点
            if(data.containsKey(TaskPo.class.getSimpleName())){
                Object _obj = data.get(TaskPo.class.getSimpleName());
                JSONArray businessTasks = null;
                if(_obj instanceof JSONObject){
                    businessTasks = new JSONArray();
                    businessTasks.add(_obj);
                }else {
                    businessTasks = (JSONArray)_obj;
                }
                //JSONObject businessTask = data.getJSONObject(TaskPo.class.getSimpleName());
                for (int _taskIndex = 0; _taskIndex < businessTasks.size();_taskIndex++) {
                    JSONObject businessTask = businessTasks.getJSONObject(_taskIndex);
                    doBusinessTask(business, businessTask);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("taskId", businessTask.getString("taskId"));
                    }
                }
            }
    }


    /**
     * business to instance 过程
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
        List<Map> businessTaskInfos = taskServiceDaoImpl.getBusinessTaskInfo(info);
        if( businessTaskInfos != null && businessTaskInfos.size() >0) {
            for (int _taskIndex = 0; _taskIndex < businessTaskInfos.size();_taskIndex++) {
                Map businessTaskInfo = businessTaskInfos.get(_taskIndex);
                flushBusinessTaskInfo(businessTaskInfo,StatusConstant.STATUS_CD_VALID);
                taskServiceDaoImpl.updateTaskInfoInstance(businessTaskInfo);
                if(businessTaskInfo.size() == 1) {
                    dataFlowContext.addParamOut("taskId", businessTaskInfo.get("task_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //定时任务信息
        List<Map> taskInfo = taskServiceDaoImpl.getTaskInfo(info);
        if(taskInfo != null && taskInfo.size() > 0){

            //定时任务信息
            List<Map> businessTaskInfos = taskServiceDaoImpl.getBusinessTaskInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessTaskInfos == null || businessTaskInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（task），程序内部异常,请检查！ "+delInfo);
            }
            for (int _taskIndex = 0; _taskIndex < businessTaskInfos.size();_taskIndex++) {
                Map businessTaskInfo = businessTaskInfos.get(_taskIndex);
                flushBusinessTaskInfo(businessTaskInfo,StatusConstant.STATUS_CD_VALID);
                taskServiceDaoImpl.updateTaskInfoInstance(businessTaskInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"taskId 错误，不能自动生成（必须已经存在的taskId）"+businessTask);
        }
        //自动保存DEL
        autoSaveDelBusinessTask(business,businessTask);

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
