package com.java110.community.cmd.inspectionTaskDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailV1InnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskV1InnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionTaskDetail.updateInspectionTaskDetail")
public class UpdateInspectionTaskDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateInspectionTaskDetailCmd.class);

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskDetailV1InnerServiceSMO inspectionTaskDetailV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskV1InnerServiceSMO inspectionTaskV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "taskDetailId", "请求报文中未包含taskDetailId");
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "请求报文中未包含inspectionId");
        Assert.hasKeyAndValue(reqJson, "photos", "请求报文中未包含照片");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("photos")) {
            dealPhotos(reqJson);
        }

        //添加单元信息
        reqJson.put("state", "20200407");//巡检点完成
        try {
            updateInspectionTaskDetail(reqJson);
        } catch (ParseException e) {
            logger.error("更新明细失败", e);
            throw new CmdException(e.getMessage());
        }

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(reqJson.getString("taskId"));
        inspectionTaskDto.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDto.setState("20200405");
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) {
            reqJson.put("state", "20200406");
            updateInspectionTask(reqJson);
        }

        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDetailDto.setTaskId(reqJson.getString("taskId"));
        inspectionTaskDetailDto.setState("20200405");
        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);

        if (count > 0) {//说明还没有巡检完
            return;
        }
        reqJson.put("state", "20200407");//巡检完成
        updateInspectionTask(reqJson);
    }


    private void dealPhotos(JSONObject reqJson) {
        JSONArray photos = reqJson.getJSONArray("photos");
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            Object photo = photos.get(photoIndex);
            reqJson.put("fileName", photo.toString());
            addPhoto(reqJson);
        }
    }

    /**
     * 添加图片
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "90000");
        businessUnit.put("saveWay", "ftp");
        businessUnit.put("objId", paramInJson.getString("taskDetailId"));
        businessUnit.put("fileRealName", paramInJson.getString("fileName"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileName"));
        FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
        int flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        if (flag < 1) {
            throw new CmdException("保存图片失败");
        }
    }

    public void updateInspectionTaskDetail(JSONObject paramInJson) throws ParseException {
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
        String currentDate = sdf1.format(date);

        //获取巡检点的巡检点时间限制
        String inspectionStartTime = "";
        String inspectionEndTime = "";
        long planStartTime;
        long planFinishTime;
        String pointStartTime = inspectionTaskDetailDtos.get(0).getPointStartTime();
        String pointEndTime = inspectionTaskDetailDtos.get(0).getPointEndTime();
        if (!StringUtil.isEmpty(pointStartTime)) {
            inspectionStartTime = currentDate + " " + pointStartTime + ":00";
            Date startTime = format.parse(inspectionStartTime);
            planStartTime = startTime.getTime();
        } else {
            //获取计划巡检开始时间
            String planInsTime = inspectionTaskDtos.get(0).getPlanInsTime();
            Date startTime = format.parse(planInsTime);
            planStartTime = startTime.getTime();
        }
        if (!StringUtil.isEmpty(pointEndTime)) {
            inspectionEndTime = currentDate + " " + pointEndTime + ":00";
            Date endTime = format.parse(inspectionEndTime);
            planFinishTime = endTime.getTime();
        } else {
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

        int flag = inspectionTaskDetailV1InnerServiceSMOImpl.updateInspectionTaskDetail(inspectionTaskDetailPoPo);

        if (flag < 1) {
            throw new CmdException("更新任务明细失败");
        }
        //巡检完成后更改巡检任务表状态
//        InspectionTaskDto inspectionTask= new InspectionTaskDto();
//        inspectionTask.setTaskId(inspectionTaskDetailPoPo.getTaskId());
//        inspectionTask.setState(inspectionTaskDetailPoPo.getState());
//        inspectionTaskInnerServiceSMOImpl.updateInspectionTask(inspectionTask);
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionTask(JSONObject paramInJson) {

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(paramInJson.getString("taskId"));
        inspectionTaskDto.setCommunityId(paramInJson.getString("communityId"));
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        Assert.listOnlyOne(inspectionTaskDtos, "未找到需要修改的巡检任务 或多条数据");

        JSONObject businessInspectionTask = new JSONObject();
        businessInspectionTask.putAll(BeanConvertUtil.beanCovertMap(inspectionTaskDtos.get(0)));

        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(businessInspectionTask, InspectionTaskPo.class);
        inspectionTaskPo.setActInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        inspectionTaskPo.setActUserId(paramInJson.getString("userId"));
        inspectionTaskPo.setActUserName(paramInJson.getString("userName"));
        inspectionTaskPo.setState(paramInJson.getString("state"));
        if (!StringUtil.isEmpty(paramInJson.getString("taskType")) && paramInJson.getString("taskType").equals("2000")) {
//            inspectionTaskPo.setOriginalPlanUserId(inspectionTaskPo.getPlanUserId());
//            inspectionTaskPo.setOriginalPlanUserName(inspectionTaskPo.getPlanUserName());
            inspectionTaskPo.setPlanUserId(paramInJson.getString("staffId"));
            inspectionTaskPo.setPlanUserName(paramInJson.getString("staffName"));
            inspectionTaskPo.setTaskType(paramInJson.getString("taskType"));
            inspectionTaskPo.setTransferDesc(paramInJson.getString("transferDesc"));
        }
        int flag = inspectionTaskV1InnerServiceSMOImpl.updateInspectionTask(inspectionTaskPo);
        if (flag < 1) {
            throw new CmdException("修改任务失败");
        }
    }
}
