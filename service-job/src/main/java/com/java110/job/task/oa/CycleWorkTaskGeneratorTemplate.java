package com.java110.job.task.oa;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.workCycle.WorkCycleDto;
import com.java110.dto.workPool.WorkPoolDto;
import com.java110.dto.workPoolFile.WorkPoolFileDto;
import com.java110.dto.workTask.WorkTaskDto;
import com.java110.intf.oa.IWorkCycleV1InnerServiceSMO;
import com.java110.intf.oa.IWorkPoolFileV1InnerServiceSMO;
import com.java110.intf.oa.IWorkPoolV1InnerServiceSMO;
import com.java110.intf.oa.IWorkTaskV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.workPoolFile.WorkPoolFilePo;
import com.java110.po.workTask.WorkTaskPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CycleWorkTaskGeneratorTemplate extends TaskSystemQuartz {

    @Autowired
    private IWorkCycleV1InnerServiceSMO workCycleV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolFileV1InnerServiceSMO workPoolFileV1InnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行周期性工作单任务生成" + taskDto.toString());

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

    private void generatorTask(TaskDto taskDto, CommunityDto communityDto) {

        WorkCycleDto workCycleDto = new WorkCycleDto();
        workCycleDto.setCommunityId(communityDto.getCommunityId());
        workCycleDto.setWorkCycle(WorkPoolDto.WORK_CYCLE_CYCLE);
        workCycleDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<WorkCycleDto> workCycleDtos = workCycleV1InnerServiceSMOImpl.queryWorkCycles(workCycleDto);

        if(workCycleDtos == null){
            return ;
        }

        for(WorkCycleDto tmpWorkCycleDto : workCycleDtos){

            try{
                doGeneratorTask(tmpWorkCycleDto,taskDto,communityDto);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void doGeneratorTask(WorkCycleDto tmpWorkCycleDto,TaskDto taskDto, CommunityDto communityDto) {

        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(tmpWorkCycleDto.getWorkId());
        List<WorkPoolDto>workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        if(ListUtil.isNull(workPoolDtos)){
            return;
        }

        Date startTime = DateUtil.getDateFromStringA(workPoolDtos.get(0).getStartTime());
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTime(startTime);

        Calendar nowCal = Calendar.getInstance();
        nowCal.set(Calendar.HOUR,startTimeCal.get(Calendar.HOUR));
        nowCal.set(Calendar.MINUTE,startTimeCal.get(Calendar.MINUTE));
        nowCal.set(Calendar.SECOND,startTimeCal.get(Calendar.SECOND));

        Date nowTime = nowCal.getTime();

        boolean hasCondition = false;
        switch (tmpWorkCycleDto.getPeriod()) {
            case WorkCycleDto.PERIOD_MONTH_DAY:
                hasCondition = hasGeneratorTaskConditionByDay(tmpWorkCycleDto, taskDto, communityDto,nowTime);
                break;
            case WorkCycleDto.PERIOD_MONTH_WORKDAY:
                hasCondition = hasGeneratorTaskConditionByWeek(tmpWorkCycleDto, taskDto, communityDto,nowTime);
                break;
        }
        if (!hasCondition) {
            return;
        }




        WorkTaskPo workTaskPo = new WorkTaskPo();
        workTaskPo.setWorkId(tmpWorkCycleDto.getWorkId());
        workTaskPo.setState(WorkTaskDto.STATE_WAIT);
        workTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId("11"));
        workTaskPo.setStoreId(tmpWorkCycleDto.getStoreId());
        workTaskPo.setCommunityId(tmpWorkCycleDto.getCommunityId());
        workTaskPo.setStartTime(DateUtil.getFormatTimeStringA(nowTime));
        workTaskPo.setEndTime(DateUtil.getAddHoursStringA(nowTime,Integer.parseInt(tmpWorkCycleDto.getHours())));
        workTaskPo.setStaffId(tmpWorkCycleDto.getStaffId());
        workTaskPo.setStaffName(tmpWorkCycleDto.getStaffName());
        workTaskPo.setOrgStaffId(tmpWorkCycleDto.getStaffId());
        workTaskPo.setOrgStaffName(tmpWorkCycleDto.getStaffName());

        int flag = workTaskV1InnerServiceSMOImpl.saveWorkTask(workTaskPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        WorkPoolFileDto workPoolFileDto = new WorkPoolFileDto();
        workPoolFileDto.setWorkId(tmpWorkCycleDto.getWorkId());
        workPoolFileDto.setFileType(WorkPoolFileDto.FILE_TYPE_START);
        List<WorkPoolFileDto> workPoolFileDtos = workPoolFileV1InnerServiceSMOImpl.queryWorkPoolFiles(workPoolFileDto);

        if(ListUtil.isNull(workPoolFileDtos)){
            return;
        }
        WorkPoolFilePo workPoolFilePo = null;
        for(WorkPoolFileDto tmpWorkPoolFileDto : workPoolFileDtos){
            workPoolFilePo = new WorkPoolFilePo();
            workPoolFilePo.setCommunityId(workTaskPo.getCommunityId());
            workPoolFilePo.setFileType(WorkPoolFileDto.FILE_TYPE_START);
            workPoolFilePo.setFileId(GenerateCodeFactory.getGeneratorId("11"));
            workPoolFilePo.setWorkId(workTaskPo.getWorkId());
            workPoolFilePo.setTaskId(workTaskPo.getTaskId());
            workPoolFilePo.setPathUrl(tmpWorkPoolFileDto.getPathUrl());
            workPoolFilePo.setStoreId(workTaskPo.getStoreId());
            workPoolFileV1InnerServiceSMOImpl.saveWorkPoolFile(workPoolFilePo);
        }


    }

    private boolean hasGeneratorTaskConditionByWeek(WorkCycleDto tmpWorkCycleDto, TaskDto taskDto, CommunityDto communityDto,Date nowTime) {

        // 检查 今日是否 需要 生成巡检任务
        // 检查 今日是否 需要 生成巡检任务
        String[] workday = tmpWorkCycleDto.getPeriodWorkday().split(",");

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
        if(!Arrays.asList(workday).contains(day+"")){
            return false;
        }

        //当前时间是否 到了 巡检任务前30分钟
        int beforeTime = Integer.parseInt(tmpWorkCycleDto.getBeforeTime());

        String planTime = DateUtil.getFormatTimeStringA(nowTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE,beforeTime*(-1));
        if(DateUtil.getCurrentDate().before(calendar.getTime())){ // 还没到生成任务时间
            return false;
        }


        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpWorkCycleDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if(!scheduleClassesStaffDto.isWork()){//根据排班员工 休息
            return false;

        }

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setCommunityId(tmpWorkCycleDto.getCommunityId());
        workTaskDto.setWorkId(tmpWorkCycleDto.getWorkId());
        workTaskDto.setTaskInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        workTaskDto.setOrgStaffId(tmpWorkCycleDto.getStaffId());

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<WorkTaskDto> workTasksDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);
        if (workTasksDtos != null && !workTasksDtos.isEmpty()) { // 已经生成过
            return false;
        }

        return true;

    }

    private boolean hasGeneratorTaskConditionByDay(WorkCycleDto tmpWorkCycleDto, TaskDto taskDto, CommunityDto communityDto,Date nowTime) {

        // 检查 今日是否 需要 生成巡检任务
        String[] months = tmpWorkCycleDto.getPeriodMonth().split(",");

        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH) + 1;
        if (!Arrays.asList(months).contains(month + "")) {
            return false;
        }
        String[] days = tmpWorkCycleDto.getPeriodDay().split(",");
        int day = today.get(Calendar.DAY_OF_MONTH);
        if (!Arrays.asList(days).contains(day + "")) {
            return false;
        }

        //当前时间是否 到了 巡检任务前30分钟

        int beforeTime = Integer.parseInt(tmpWorkCycleDto.getBeforeTime());

        String planTime = DateUtil.getFormatTimeStringA(nowTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringA(planTime));
        calendar.add(Calendar.MINUTE, beforeTime * (-1));
        if (DateUtil.getCurrentDate().before(calendar.getTime())) { // 还没到生成任务时间
            return false;
        }

        // 判断 员工是否上班

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpWorkCycleDto.getStaffId());
        scheduleClassesStaffDto.setToday(DateUtil.getDateFromStringA(planTime));
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        if (!scheduleClassesStaffDto.isWork()) {//根据排班员工 休息
            return false;

        }

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setCommunityId(tmpWorkCycleDto.getCommunityId());
        workTaskDto.setWorkId(tmpWorkCycleDto.getWorkId());
        workTaskDto.setTaskInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        workTaskDto.setOrgStaffId(tmpWorkCycleDto.getStaffId());

        //目前逻辑修改 一个巡检 计划 对于一个员工只能生成一次巡检任务，所以 传 员工 巡检计划ID 时间即可
        List<WorkTaskDto> workTasksDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);
        if (workTasksDtos != null && !workTasksDtos.isEmpty()) { // 已经生成过
            return false;
        }

        return true;
    }
}
