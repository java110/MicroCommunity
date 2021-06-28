package com.java110.job.task.purchase;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspectionPlan.InspectionTaskDetailDto;
import com.java110.dto.inspectionPlan.InspectionTaskDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
                generatorTask(taskDto, communityDto);
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
}
