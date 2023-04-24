package com.java110.api.bmo.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.task.ITaskAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.job.ITaskAttrInnerServiceSMO;
import com.java110.dto.task.TaskAttrDto;
import com.java110.po.taskAttr.TaskAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskAttrBMOImpl")
public class TaskAttrBMOImpl extends ApiBaseBMO implements ITaskAttrBMO {

    @Autowired
    private ITaskAttrInnerServiceSMO taskAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.insert(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TaskAttrDto taskAttrDto = new TaskAttrDto();
        taskAttrDto.setAttrId(paramInJson.getString("attrId"));
        taskAttrDto.setTaskId(paramInJson.getString("taskId"));
        List<TaskAttrDto> taskAttrDtos = taskAttrInnerServiceSMOImpl.queryTaskAttrs(taskAttrDto);

        Assert.listOnlyOne(taskAttrDtos, "未找到需要修改的活动 或多条数据");


        paramInJson.putAll(BeanConvertUtil.beanCovertMap(taskAttrDtos.get(0)));
        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.update(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TASK_ATTR);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.update(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TASK_ATTR);
    }

}
