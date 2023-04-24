package com.java110.job.task.purchase;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 巡检签到状态修改定时任务
 *
 * @author fqz
 * @date 2021-06-11 9:12
 */
@Component
public class InspectionTaskDetailTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(InspectionTaskDetailTemplate.class);

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO iInspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO iInspectionTaskInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());
        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();
        for (CommunityDto communityDto : communityDtos) {
            try {
                //签到状态修改
                generatorTask(taskDto, communityDto);
                //当天巡检任务状态修改
                changeToadyTask(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    //签到状态修改
    private void generatorTask(TaskDto taskDto, CommunityDto communityDto) {
        //取出今天的日期
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String nowTime = today + " 00:00:00";
        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setCommunityId(communityDto.getCommunityId());
        inspectionTaskDetailDto.setNowTime(nowTime);
        inspectionTaskDetailDto.setInspectionTimeFlag("1");
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = iInspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);
        if (inspectionTaskDetailDtos != null && inspectionTaskDetailDtos.size() > 0) {
            String taskId = "";
            List<InspectionTaskDetailDto> taskDetailDtos = new ArrayList<>();
            for (InspectionTaskDetailDto inspectionTaskDetail : inspectionTaskDetailDtos) {
                //巡检任务明细表签到状态改为未到
                inspectionTaskDetail.setInspectionState("70000");
                if (!taskId.equals(inspectionTaskDetail.getTaskId())) {
                    //获取巡检任务id
                    taskId = inspectionTaskDetail.getTaskId();
                    InspectionTaskDetailDto taskDetail = new InspectionTaskDetailDto();
                    taskDetail.setTaskId(taskId);
                    taskDetail.setInspectionTimeFlag("2");
                    taskDetailDtos = iInspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(taskDetail);
                }
                iInspectionTaskDetailInnerServiceSMOImpl.updateInspectionTaskDetail(inspectionTaskDetail);
                if (taskDetailDtos != null && taskDetailDtos.size() > 0) { //如果查询结果不为空且有数据，说明该任务id下的巡检计划有过打卡的数据，该条巡检任务状态就变为缺勤
                    InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
                    inspectionTaskDto.setTaskId(taskId);
                    //巡检任务表巡检状态变为缺勤
                    inspectionTaskDto.setState("20200409");
                    iInspectionTaskInnerServiceSMOImpl.updateInspectionTask(inspectionTaskDto);
                } else { //如果查询数据为空，说明该任务id下的巡检计划没有打卡的数据，该条巡检任务状态就变为已超时
                    InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
                    inspectionTaskDto.setTaskId(taskId);
                    //巡检任务表巡检状态变为已超时
                    inspectionTaskDto.setState("20200408");
                    iInspectionTaskInnerServiceSMOImpl.updateInspectionTask(inspectionTaskDto);
                }
            }
        }
    }

    private void changeToadyTask(TaskDto taskDto, CommunityDto communityDto) throws ParseException {
        //取出今天的日期
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String toadyStartTime = today + " 00:00:00";
        String todayEndTime = today + " 23:59:59";
        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setCommunityId(communityDto.getCommunityId());
        inspectionTaskDetailDto.setQrCodeTime(toadyStartTime);
        inspectionTaskDetailDto.setNowTime(todayEndTime);
        inspectionTaskDetailDto.setInspectionTimeFlag("1");
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = iInspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);
        if (inspectionTaskDetailDtos != null && inspectionTaskDetailDtos.size() > 0) {
            String taskId = "";
            for (InspectionTaskDetailDto inspectionTaskDetail : inspectionTaskDetailDtos) {
                InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
                if (!StringUtil.isEmpty(inspectionTaskDetail.getState()) && !inspectionTaskDetail.getState().equals("20200407")) { //不是巡检完成状态
                    //获取当前时间
                    Date date = new Date();
                    if (!StringUtil.isEmpty(inspectionTaskDetail.getPlanInsTime()) && !StringUtil.isEmpty(inspectionTaskDetail.getPlanEndTime())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //获取巡检计划开始时间
                        Date planInsTime = sdf.parse(inspectionTaskDetail.getPlanInsTime());
                        //获取巡检计划结束时间
                        Date planEndTime = sdf.parse(inspectionTaskDetail.getPlanEndTime());
                        if (date.before(planInsTime)) { //如果当前时间在计划开始时间之前，任务状态就为未开始
                            inspectionTaskDetail.setState("20200405"); // 20200405 未开始  20200406 巡检中  20200407 巡检完成  20200408 巡检未完成
                            inspectionTaskDto.setState("20200405"); //20200405 未开始  20200406 巡检中  20200407 巡检完成  20200408 已超时  20200409 缺勤
                        } else if (date.after(planEndTime)) { //如果当前时间在计划结束时间之后，任务状态为巡检未完成
                            inspectionTaskDetail.setState("20200408");
                            inspectionTaskDto.setState("20200408");
                        } else {
                            inspectionTaskDetail.setState("20200406");
                            inspectionTaskDto.setState("20200406");
                        }
                    } else {
                        inspectionTaskDetail.setState("20200405");
                        inspectionTaskDto.setState("20200405");
                    }
                    iInspectionTaskDetailInnerServiceSMOImpl.updateInspectionTaskDetail(inspectionTaskDetail);
                }
                if (!taskId.equals(inspectionTaskDetail.getTaskId())) {
                    //获取巡检任务id
                    taskId = inspectionTaskDetail.getTaskId();
                    inspectionTaskDto.setTaskId(taskId);
                    iInspectionTaskInnerServiceSMOImpl.updateInspectionTask(inspectionTaskDto);
                }
            }
        }
    }
}
