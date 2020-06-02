package com.java110.api.bmo.inspectionTask.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionTask.IInspectionTaskBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionTask.IInspectionTaskInnerServiceSMO;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.po.file.FileRelPo;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service("inspectionTaskBMOImpl")
public class InspectionTaskBMOImpl extends ApiBaseBMO implements IInspectionTaskBMO {

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addInspectionTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("taskId", "-1");
        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(paramInJson, InspectionTaskPo.class);

        super.insert(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(paramInJson.getString("taskId"));
        inspectionTaskDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        Assert.listOnlyOne(inspectionTaskDtos, "未找到需要修改的巡检任务 或多条数据");

        JSONObject businessInspectionTask = new JSONObject();
        businessInspectionTask.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDtos.get(0)));

        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(businessInspectionTask, InspectionTaskPo.class);

        super.update(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_TASK);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteInspectionTask(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(paramInJson, InspectionTaskPo.class);

        super.update(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_TASK);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "90000");
        businessUnit.put("saveWay", "ftp");
        businessUnit.put("objId", paramInJson.getString("taskDetailId"));
        businessUnit.put("fileRealName", paramInJson.getString("photoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        super.insert(dataFlowContext,fileRelPo,BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);
    }
}
