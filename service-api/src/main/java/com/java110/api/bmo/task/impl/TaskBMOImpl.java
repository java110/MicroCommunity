package com.java110.api.bmo.task.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.task.ITaskBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.job.ITaskInnerServiceSMO;
import com.java110.po.task.TaskPo;
import com.java110.po.taskAttr.TaskAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskBMOImpl")
public class TaskBMOImpl extends ApiBaseBMO implements ITaskBMO {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        // paramInJson.put("taskId", "-1");
        TaskPo taskPo = BeanConvertUtil.covertBean(paramInJson, TaskPo.class);
        taskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        taskPo.setState("001");
        super.insert(dataFlowContext, taskPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK);

        if (!paramInJson.containsKey("templateSpecs")) {
            return;
        }

        JSONArray templateSpecs = paramInJson.getJSONArray("templateSpecs");
        TaskAttrPo taskAttrPo = null;
        JSONObject tmpSpecObj = null;
        for (int specIndex = 0; specIndex < templateSpecs.size(); specIndex++) {
            taskAttrPo = new TaskAttrPo();
            tmpSpecObj = templateSpecs.getJSONObject(specIndex);
            taskAttrPo.setAttrId("-" + (specIndex + 1));
            taskAttrPo.setSpecCd(tmpSpecObj.getString("specCd"));
            taskAttrPo.setTaskId(taskPo.getTaskId());
            taskAttrPo.setValue(tmpSpecObj.getString("value"));
            super.insert(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK_ATTR);
        }
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {

//        TaskDto taskDto = new TaskDto();
//        taskDto.setTaskId(paramInJson.getString("taskId"));
//        //taskDto.setJobId(paramInJson.getString("jobId"));
//        List<TaskDto> taskDtos = taskInnerServiceSMOImpl.queryTasks(taskDto);
//
//        Assert.listOnlyOne(taskDtos, "未找到需要修改的活动 或多条数据");
//        paramInJson.putAll(BeanConvertUtil.beanCovertMap(taskDtos.get(0)));
        TaskPo taskPo = BeanConvertUtil.covertBean(paramInJson, TaskPo.class);
        super.update(dataFlowContext, taskPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TASK);

        if (!paramInJson.containsKey("templateSpecs")) {
            return;
        }

        JSONArray templateSpecs = paramInJson.getJSONArray("templateSpecs");
        JSONObject specObj = null;
        TaskAttrPo taskAttrPo = null;
        for (int specIndex = 0; specIndex < templateSpecs.size(); specIndex++) {
            specObj = templateSpecs.getJSONObject(specIndex);
            if (specObj.containsKey("attrId") && !"-1".equals(specObj.getString("attrId"))) {
                taskAttrPo = new TaskAttrPo();
                taskAttrPo.setAttrId(specObj.getString("attrId"));
                taskAttrPo.setTaskId(taskPo.getTaskId());
                taskAttrPo.setValue(specObj.getString("value"));
                taskAttrPo.setSpecCd(specObj.getString("specCd"));
                super.update(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TASK_ATTR);

                continue;
            }
            taskAttrPo = new TaskAttrPo();
            taskAttrPo.setAttrId("-" + (specIndex + 1));
            taskAttrPo.setTaskId(taskPo.getTaskId());
            taskAttrPo.setValue(specObj.getString("value"));
            taskAttrPo.setSpecCd(specObj.getString("specCd"));
            super.insert(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK_ATTR);
        }
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TaskPo taskPo = BeanConvertUtil.covertBean(paramInJson, TaskPo.class);
        super.update(dataFlowContext, taskPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TASK);
    }

}
