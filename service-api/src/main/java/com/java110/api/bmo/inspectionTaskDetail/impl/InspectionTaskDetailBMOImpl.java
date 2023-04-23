package com.java110.api.bmo.inspectionTaskDetail.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspectionTaskDetail.IInspectionTaskDetailBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("inspectionTaskDetailBMOImpl")
public class InspectionTaskDetailBMOImpl extends ApiBaseBMO implements IInspectionTaskDetailBMO {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("taskDetailId", "-1");
        InspectionTaskDetailPo inspectionTaskPo = BeanConvertUtil.covertBean(paramInJson, InspectionTaskDetailPo.class);
        super.insert(dataFlowContext, inspectionTaskPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_TASK_DETAIL);
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) throws ParseException {
        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setTaskDetailId(paramInJson.getString("taskDetailId"));
        inspectionTaskDetailDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);
        Assert.listOnlyOne(inspectionTaskDetailDtos, "未找到需要修改的活动 或多条数据");
        JSONObject businessInspectionTaskDetail = new JSONObject();
        businessInspectionTaskDetail.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDetailDtos.get(0)));
        businessInspectionTaskDetail.putAll(paramInJson);
        InspectionTaskDetailPo inspectionTaskDetailPoPo = BeanConvertUtil.covertBean(businessInspectionTaskDetail, InspectionTaskDetailPo.class);
        inspectionTaskDetailPoPo.setInspectionTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        inspectionTaskDetailPoPo.setActUserId(paramInJson.getString("userId"));
        inspectionTaskDetailPoPo.setActUserName(paramInJson.getString("userName"));
        //获取任务id
        String taskId = paramInJson.getString("taskId");
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(taskId);
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        Assert.listOnlyOne(inspectionTaskDtos, "查询巡检任务数据错误！");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate=sdf1.format(date);
        System.out.println(currentDate);


        //获取巡检点的巡检点时间限制
        String inspectionStartTime="";
        String inspectionEndTime="";
        long planStartTime;
        long planFinishTime;
        String pointStartTime = inspectionTaskDetailDtos.get(0).getPointStartTime();
        String pointEndTime = inspectionTaskDetailDtos.get(0).getPointEndTime();
        if(!StringUtil.isEmpty(pointStartTime)){
            inspectionStartTime=currentDate+" "+pointStartTime+":00";
            Date startTime = format.parse(inspectionStartTime);
            planStartTime = startTime.getTime();
        }else{
            //获取计划巡检开始时间
            String planInsTime = inspectionTaskDtos.get(0).getPlanInsTime();
            Date startTime = format.parse(planInsTime);
            planStartTime = startTime.getTime();
        }
        if(!StringUtil.isEmpty(pointEndTime)){
            inspectionEndTime=currentDate+" "+pointEndTime+":00";
            Date endTime = format.parse(inspectionEndTime);
            planFinishTime = endTime.getTime();
        }else{
            //获取计划巡检结束时间
            String planEndTime = inspectionTaskDtos.get(0).getPlanEndTime();
            Date endTime = format.parse(planEndTime);
            planFinishTime = endTime.getTime();
        }

        //获取计划巡检开始时间
//        String planInsTime = inspectionTaskDtos.get(0).getPlanInsTime();
//        Date startTime = format.parse(planInsTime);
//        long planStartTime = startTime.getTime();
//        //获取计划巡检结束时间
//        String planEndTime = inspectionTaskDtos.get(0).getPlanEndTime();
//        Date endTime = format.parse(planEndTime);
//        long planFinishTime = endTime.getTime();
        //获取巡检点签到时间
        String inspectionTime = inspectionTaskDetailPoPo.getInspectionTime();
        Date inspectionDetailTime = format.parse(inspectionTime);
        long detailTime = inspectionDetailTime.getTime();
        if (detailTime < planStartTime) {  //如果巡检点签到时间小于巡检计划开始时间，签到状态就是早到
            inspectionTaskDetailPoPo.setInspectionState("40000");
        } else if (detailTime > planFinishTime) {  //如果巡检点签到时间大于巡检计划结束时间，签到状态就是迟到
            inspectionTaskDetailPoPo.setInspectionState("50000");
        } else {  //如果巡检点签到时间在巡检计划开始时间和巡检计划结束时间之间，签到状态就是正常
            inspectionTaskDetailPoPo.setInspectionState("60000");
        }
        super.insert(dataFlowContext, inspectionTaskDetailPoPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_TASK_DETAIL);
    }
}
