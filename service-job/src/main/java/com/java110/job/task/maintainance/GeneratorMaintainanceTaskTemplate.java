package com.java110.job.task.maintainance;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.maintainance.MaintainancePlanDto;
import com.java110.dto.maintainance.MaintainancePlanMachineDto;
import com.java110.dto.maintainance.MaintainancePlanStaffDto;
import com.java110.dto.maintainance.MaintainanceTaskDto;
import com.java110.dto.maintainance.MaintainanceTaskDetailDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.*;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.maintainance.MaintainanceTaskPo;
import com.java110.po.maintainance.MaintainanceTaskDetailPo;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 保养任务生成 任务
 */
@Component
public class GeneratorMaintainanceTaskTemplate extends TaskSystemQuartz {

    @Autowired
    private IMaintainancePlanV1InnerServiceSMO maintainancePlanV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanStaffV1InnerServiceSMO maintainancePlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanMachineV1InnerServiceSMO maintainancePlanMachineV1InnerServiceSMOImpl;


    @Autowired
    private IMaintainanceTaskV1InnerServiceSMO maintainanceTaskV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainanceTaskDetailV1InnerServiceSMO maintainanceTaskDetailV1InnerServiceSMOImpl;

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
        MaintainancePlanDto maintainancePlanDto = new MaintainancePlanDto();
        maintainancePlanDto.setCommunityId(communityDto.getCommunityId());
        maintainancePlanDto.setState(MaintainancePlanDto.STATE_RUN);
        maintainancePlanDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<MaintainancePlanDto> maintainancePlanDtos = maintainancePlanV1InnerServiceSMOImpl.queryMaintainancePlans(maintainancePlanDto);

        for (MaintainancePlanDto tmpMaintainancePlanDto : maintainancePlanDtos) {
            dealMaintainancePlan(tmpMaintainancePlanDto, taskDto, communityDto);
        }
    }

    /**
     * 处理巡检计划
     *
     * @param tmpMaintainancePlanDto
     * @param taskDto
     * @param communityDto
     */
    private void dealMaintainancePlan(MaintainancePlanDto tmpMaintainancePlanDto, TaskDto taskDto, CommunityDto communityDto) {

        MaintainancePlanStaffDto maintainancePlanStaffDto = new MaintainancePlanStaffDto();
        maintainancePlanStaffDto.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
        maintainancePlanStaffDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        List<MaintainancePlanStaffDto> maintainancePlanStaffDtos = maintainancePlanStaffV1InnerServiceSMOImpl.queryMaintainancePlanStaffs(maintainancePlanStaffDto);

        List<MaintainanceTaskPo> maintainanceTaskPos = new ArrayList<>();
        List<MaintainanceTaskDetailPo> maintainanceTaskDetailPos = new ArrayList<>();

        for (MaintainancePlanStaffDto tmpMaintainancePlanStaffDto : maintainancePlanStaffDtos) {
            generatorStaffTask(tmpMaintainancePlanDto, taskDto, communityDto, tmpMaintainancePlanStaffDto, maintainanceTaskPos, maintainanceTaskDetailPos);
        }

        if (maintainanceTaskPos.size() < 1) {
            return;
        }

        if (maintainanceTaskDetailPos.size() < 1) {
            return;
        }
        maintainanceTaskV1InnerServiceSMOImpl.saveMaintainanceTasks(maintainanceTaskPos);

        maintainanceTaskV1InnerServiceSMOImpl.saveMaintainanceTaskDetail(maintainanceTaskDetailPos);

    }


    private void generatorStaffTask(MaintainancePlanDto tmpMaintainancePlanDto, TaskDto taskDto, CommunityDto communityDto,
                                    MaintainancePlanStaffDto tmpMaintainancePlanStaffDto, List<MaintainanceTaskPo> maintainanceTaskPos,
                                    List<MaintainanceTaskDetailPo> maintainanceTaskDetailPos) {

        //巡检方式
        String maintainancePlanPeriod = tmpMaintainancePlanDto.getPlanPeriod();

        boolean hasCondition = false;
        switch (maintainancePlanPeriod) {
            case MaintainancePlanDto.INSPECTION_PLAN_PERIOD_DAY:
                hasCondition = hasGeneratorTaskConditionByDay(tmpMaintainancePlanDto, taskDto, communityDto, tmpMaintainancePlanStaffDto, maintainanceTaskPos, maintainanceTaskDetailPos);
                break;
            case MaintainancePlanDto.INSPECTION_PLAN_PERIOD_NEXT_DAY:
                hasCondition = hasGeneratorTaskConditionByWeek(tmpMaintainancePlanDto, taskDto, communityDto, tmpMaintainancePlanStaffDto, maintainanceTaskPos, maintainanceTaskDetailPos);
                break;
        }
        if (!hasCondition) {
            return;
        }


        String nowTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " ";

        MaintainanceTaskPo maintainanceTaskPo = new MaintainanceTaskPo();
        maintainanceTaskPo.setPlanId(tmpMaintainancePlanDto.getPlanId());
        maintainanceTaskPo.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
        maintainanceTaskPo.setMpsId(tmpMaintainancePlanStaffDto.getMpsId());
        maintainanceTaskPo.setPlanInsTime(nowTime + "09:00:00");
        maintainanceTaskPo.setPlanEndTime(nowTime + "18:00:00");
        maintainanceTaskPo.setPlanUserId(tmpMaintainancePlanStaffDto.getStaffId());
        maintainanceTaskPo.setPlanUserName(tmpMaintainancePlanStaffDto.getStaffName());
        maintainanceTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        maintainanceTaskPo.setOriginalPlanUserId(tmpMaintainancePlanStaffDto.getStaffId());
        maintainanceTaskPo.setOriginalPlanUserName(tmpMaintainancePlanStaffDto.getStaffName());
        maintainanceTaskPo.setTaskType("1000");
        maintainanceTaskPo.setState(MaintainanceTaskDto.STATE_WAIT);

        MaintainancePlanMachineDto maintainancePlanMachineDto = new MaintainancePlanMachineDto();
        maintainancePlanMachineDto.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
        maintainancePlanMachineDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        List<MaintainancePlanMachineDto> maintainancePlanMachineDtos = maintainancePlanMachineV1InnerServiceSMOImpl.queryMaintainancePlanMachines(maintainancePlanMachineDto);
        if (maintainancePlanMachineDtos == null || maintainancePlanMachineDtos.size() < 1) {
            return;//巡检点为空时不生成 巡检任务
        }
        MaintainanceTaskDetailPo maintainanceTaskDetailPo = null;
        for (MaintainancePlanMachineDto tmpMaintainanceRoutePointRelDto : maintainancePlanMachineDtos) {
            maintainanceTaskDetailPo = new MaintainanceTaskDetailPo();
            maintainanceTaskDetailPo.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
            maintainanceTaskDetailPo.setMachineId(tmpMaintainanceRoutePointRelDto.getMachineId());
            maintainanceTaskDetailPo.setMachineName(tmpMaintainanceRoutePointRelDto.getMachineName());
            maintainanceTaskDetailPo.setTaskId(maintainanceTaskPo.getTaskId());
            maintainanceTaskDetailPo.setPointStartTime(nowTime + "09:00:00");
            maintainanceTaskDetailPo.setPointEndTime(nowTime + "18:00:00");
            maintainanceTaskDetailPo.setSortNumber("1");
            maintainanceTaskDetailPo.setTaskDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
            maintainanceTaskDetailPo.setState(MaintainanceTaskDetailDto.STATE_WAIT);
            maintainanceTaskDetailPos.add(maintainanceTaskDetailPo);
        }
        maintainanceTaskPos.add(maintainanceTaskPo);
    }


    /**
     * 每日
     *
     * @param tmpMaintainancePlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean hasGeneratorTaskConditionByDay(MaintainancePlanDto tmpMaintainancePlanDto, TaskDto taskDto, CommunityDto communityDto,
                                                   MaintainancePlanStaffDto tmpMaintainancePlanStaffDto, List<MaintainanceTaskPo> maintainanceTaskPos,
                                                   List<MaintainanceTaskDetailPo> maintainanceTaskDetailPos) {

        // 检查 今日是否 需要 生成巡检任务
        String[] months = tmpMaintainancePlanDto.getMaintainanceMonth().split(",");

        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH) + 1;
        if (!Arrays.asList(months).contains(month + "")) {
            return false;
        }
        String[] days = tmpMaintainancePlanDto.getMaintainanceDay().split(",");
        int day = today.get(Calendar.DAY_OF_MONTH);
        if (!Arrays.asList(days).contains(day + "")) {
            return false;
        }

        //当前时间是否 到了 巡检任务前30分钟

        int beforeTime = 30;

        String planTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " 09:00:00";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE, beforeTime * (-1));
        if (DateUtil.getCurrentDate().before(calendar.getTime())) { // 还没到生成任务时间
            return false;
        }

        // 判断 员工是否上班

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpMaintainancePlanStaffDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if (!scheduleClassesStaffDto.isWork()) {//根据排班员工 休息
            return false;

        }

        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
        //maintainanceTaskDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        //这里修改为用原始 巡检人查 以防 做了 转单
        maintainanceTaskDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        maintainanceTaskDto.setOriginalPlanUserId(tmpMaintainancePlanStaffDto.getStaffId());
        maintainanceTaskDto.setPlanInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);
        if (maintainanceTaskDtos != null && maintainanceTaskDtos.size() > 0) { // 已经生成过
            return false;
        }

        return true;

    }

    /**
     * 每周
     *
     * @param tmpMaintainancePlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean hasGeneratorTaskConditionByWeek(MaintainancePlanDto tmpMaintainancePlanDto, TaskDto taskDto, CommunityDto communityDto,
                                                    MaintainancePlanStaffDto tmpMaintainancePlanStaffDto, List<MaintainanceTaskPo> maintainanceTaskPos,
                                                    List<MaintainanceTaskDetailPo> maintainanceTaskDetailPos) {

        // 检查 今日是否 需要 生成巡检任务
        int day = Integer.parseInt(tmpMaintainancePlanDto.getMaintainanceEveryday());


        int subDay = DateUtil.daysBetween(DateUtil.getCurrentDate(), DateUtil.getDateFromStringB(tmpMaintainancePlanDto.getStartDate()));

        if (subDay % day != 0) {
            return false;
        }


        //当前时间是否 到了 巡检任务前30分钟
        int beforeTime = 30;

        String planTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " " + "09:00:00";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE, beforeTime * (-1));
        if (DateUtil.getCurrentDate().before(calendar.getTime())) { // 还没到生成任务时间
            return false;
        }


        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpMaintainancePlanStaffDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if (!scheduleClassesStaffDto.isWork()) {//根据排班员工 休息
            return false;

        }

        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setCommunityId(tmpMaintainancePlanDto.getCommunityId());
        //maintainanceTaskDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        //这里修改为用原始 巡检人查 以防 做了 转单
        maintainanceTaskDto.setPlanId(tmpMaintainancePlanDto.getPlanId());
        maintainanceTaskDto.setOriginalPlanUserId(tmpMaintainancePlanStaffDto.getStaffId());
        maintainanceTaskDto.setPlanInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);
        if (maintainanceTaskDtos != null && maintainanceTaskDtos.size() > 0) { // 已经生成过
            return false;
        }

        return true;

    }


}
