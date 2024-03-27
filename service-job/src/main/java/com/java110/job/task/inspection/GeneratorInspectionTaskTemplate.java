package com.java110.job.task.inspection;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspection.InspectionPlanDto;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.dto.inspection.InspectionRoutePointRelDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.*;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneratorInspectionTaskTemplate extends TaskSystemQuartz {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;


    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行巡检任务生成" + taskDto.toString());

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

    /**
     * 巡检任务
     *
     * @param taskDto
     * @param communityDto
     */
    private void generatorTask(TaskDto taskDto, CommunityDto communityDto) {
        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setCommunityId(communityDto.getCommunityId());
        inspectionPlanDto.setState(InspectionPlanDto.STATE_RUN);
        inspectionPlanDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);

        for (InspectionPlanDto tmpInspectionPlanDto : inspectionPlanDtos) {
            dealInspectionPlan(tmpInspectionPlanDto, taskDto, communityDto);
        }
    }

    /**
     * 处理巡检计划
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private void dealInspectionPlan(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto) {

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionPlanStaffDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);

        List<InspectionTaskPo> inspectionTaskPos = new ArrayList<>();
        List<InspectionTaskDetailPo> inspectionTaskDetailPos = new ArrayList<>();

        for (InspectionPlanStaffDto tmpInspectionPlanStaffDto : inspectionPlanStaffDtos) {
            generatorStaffTask(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
        }

        if (inspectionTaskPos.size() < 1) {
            return;
        }

        if (inspectionTaskDetailPos.size() < 1) {
            return;
        }
        inspectionTaskInnerServiceSMOImpl.saveInspectionTask(inspectionTaskPos);

        inspectionTaskInnerServiceSMOImpl.saveInspectionTaskDetail(inspectionTaskDetailPos);

    }


    private void generatorStaffTask(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                    InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                    List<InspectionTaskDetailPo> inspectionTaskDetailPos) {

        //巡检方式
        String inspectionPlanPeriod = tmpInspectionPlanDto.getInspectionPlanPeriod();

        boolean hasCondition = false;
        switch (inspectionPlanPeriod) {
            case InspectionPlanDto.INSPECTION_PLAN_PERIOD_DAY:
                hasCondition = hasGeneratorTaskConditionByDay(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
                break;
            case InspectionPlanDto.INSPECTION_PLAN_PERIOD_WEEK:
                hasCondition = hasGeneratorTaskConditionByWeek(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
                break;
        }
        if (!hasCondition) {
            return;
        }


        String nowTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " ";

        InspectionTaskPo inspectionTaskPo = new InspectionTaskPo();
        inspectionTaskPo.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskPo.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionTaskPo.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());
        inspectionTaskPo.setPlanInsTime(nowTime + tmpInspectionPlanStaffDto.getStartTime());
        inspectionTaskPo.setPlanEndTime(nowTime + tmpInspectionPlanStaffDto.getEndTime());
        inspectionTaskPo.setPlanUserId(tmpInspectionPlanStaffDto.getStaffId());
        inspectionTaskPo.setPlanUserName(tmpInspectionPlanStaffDto.getStaffName());
        inspectionTaskPo.setSignType(tmpInspectionPlanDto.getSignType());
        inspectionTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        inspectionTaskPo.setOriginalPlanUserId(tmpInspectionPlanStaffDto.getStaffId());
        inspectionTaskPo.setOriginalPlanUserName(tmpInspectionPlanStaffDto.getStaffName());
        inspectionTaskPo.setTaskType("1000");

        InspectionRoutePointRelDto inspectionRoutePointRelDto = new InspectionRoutePointRelDto();
        inspectionRoutePointRelDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionRoutePointRelDto.setInspectionRouteId(tmpInspectionPlanDto.getInspectionRouteId());
        List<InspectionRoutePointRelDto> inspectionRoutePointRelDtos = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto);
        if (inspectionRoutePointRelDtos == null || inspectionRoutePointRelDtos.size() < 1) {
            return;//巡检点为空时不生成 巡检任务
        }
        InspectionTaskDetailPo inspectionTaskDetailPo = null;
        for (InspectionRoutePointRelDto tmpInspectionRoutePointRelDto : inspectionRoutePointRelDtos) {
            inspectionTaskDetailPo = new InspectionTaskDetailPo();
            inspectionTaskDetailPo.setCommunityId(tmpInspectionPlanDto.getCommunityId());
            inspectionTaskDetailPo.setInspectionId(tmpInspectionRoutePointRelDto.getInspectionId());
            inspectionTaskDetailPo.setInspectionName(tmpInspectionRoutePointRelDto.getInspectionName());
            inspectionTaskDetailPo.setTaskId(inspectionTaskPo.getTaskId());
            inspectionTaskDetailPo.setPointStartTime(tmpInspectionRoutePointRelDto.getPointStartTime());
            inspectionTaskDetailPo.setPointEndTime(tmpInspectionRoutePointRelDto.getPointEndTime());
            inspectionTaskDetailPo.setSortNumber(tmpInspectionRoutePointRelDto.getSortNumber());
            inspectionTaskDetailPo.setTaskDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
            inspectionTaskDetailPos.add(inspectionTaskDetailPo);
        }
        inspectionTaskPos.add(inspectionTaskPo);
    }


    /**
     * 每日
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean hasGeneratorTaskConditionByDay(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                                   InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                                   List<InspectionTaskDetailPo> inspectionTaskDetailPos) {

        // 检查 今日是否 需要 生成巡检任务
        String[] months = tmpInspectionPlanDto.getInspectionMonth().split(",");

        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH) + 1;
        if (!Arrays.asList(months).contains(month + "")) {
            return false;
        }
        String[] days = tmpInspectionPlanDto.getInspectionDay().split(",");
        int day = today.get(Calendar.DAY_OF_MONTH);
        if (!Arrays.asList(days).contains(day + "")) {
            return false;
        }

        //当前时间是否 到了 巡检任务前30分钟

        int beforeTime = Integer.parseInt(tmpInspectionPlanDto.getBeforeTime());

        String planTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " " + tmpInspectionPlanDto.getStartTime()+":00";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE, beforeTime * (-1));
        if (DateUtil.getCurrentDate().before(calendar.getTime())) { // 还没到生成任务时间
            return false;
        }

        // 判断 员工是否上班

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpInspectionPlanStaffDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if (!scheduleClassesStaffDto.isWork()) {//根据排班员工 休息
            return false;

        }

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        //inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        //这里修改为用原始 巡检人查 以防 做了 转单
        inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskDto.setOriginalPlanUserId(tmpInspectionPlanStaffDto.getStaffId());
        inspectionTaskDto.setPlanInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) { // 已经生成过
            return false;
        }

        return true;

    }

    /**
     * 每周
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean hasGeneratorTaskConditionByWeek(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                                    InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                                    List<InspectionTaskDetailPo> inspectionTaskDetailPos) {

        // 检查 今日是否 需要 生成巡检任务
        String[] workday = tmpInspectionPlanDto.getInspectionWorkday().split(",");

        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_WEEK);

        //一周第一天是否为星期天
        boolean isFirstSunday = (today.getFirstDayOfWeek() == Calendar.SUNDAY);
        //获取周几
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            day = day - 1;
            if (day == 0) {
                day = 7;
            }
        }
        if (!Arrays.asList(workday).contains(day + "")) {
            return false;
        }

        //当前时间是否 到了 巡检任务前30分钟
        int beforeTime = Integer.parseInt(tmpInspectionPlanDto.getBeforeTime());

        String planTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " " + tmpInspectionPlanDto.getStartTime() + ":00";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE, beforeTime * (-1));
        if (DateUtil.getCurrentDate().before(calendar.getTime())) { // 还没到生成任务时间
            return false;
        }


        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpInspectionPlanStaffDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if (!scheduleClassesStaffDto.isWork()) {//根据排班员工 休息
            return false;

        }

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        //inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        //这里修改为用原始 巡检人查 以防 做了 转单
        inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskDto.setOriginalPlanUserId(tmpInspectionPlanStaffDto.getStaffId());
        inspectionTaskDto.setPlanInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) { // 已经生成过
            return false;
        }

        return true;

    }


}
