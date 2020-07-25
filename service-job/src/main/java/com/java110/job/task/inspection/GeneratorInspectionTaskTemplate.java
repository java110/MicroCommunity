package com.java110.job.task.inspection;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspectionPlan.InspectionPlanDto;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.*;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.inspection.InspectionTaskDetailPo;
import com.java110.po.inspection.InspectionTaskPo;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Override
    protected void process(TaskDto taskDto) throws Exception {
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

    /**
     * 每日
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean dealDayInspectionPlan(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                          InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                          List<InspectionTaskDetailPo> inspectionTaskDetailPos) {

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskDto.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        inspectionTaskDto.setScopeTime(calendar.getTime());
        inspectionTaskDto.setCreateTime(new Date());
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) { // 已经生成过
            return true;
        }

        return false;

    }

    private void generatorStaffTask(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                    InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                    List<InspectionTaskDetailPo> inspectionTaskDetailPos) {

        //巡检方式
        String inspectionPlanPeriod = tmpInspectionPlanDto.getInspectionPlanPeriod();

        boolean hasInspection = false;
        switch (inspectionPlanPeriod) {
            case InspectionPlanDto.INSPECTION_PLAN_PERIOD_DAY:
                hasInspection = dealDayInspectionPlan(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
                break;
            case InspectionPlanDto.INSPECTION_PLAN_PERIOD_WEEK:
                hasInspection = dealWeekInspectionPlan(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
                break;
            case InspectionPlanDto.INSPECTION_PLAN_PERIOD_NEXT_DAY:
                hasInspection = dealNextDayInspectionPlan(tmpInspectionPlanDto, taskDto, communityDto, tmpInspectionPlanStaffDto, inspectionTaskPos, inspectionTaskDetailPos);
                break;
        }
        if (hasInspection) {
            return;
        }


        String nowTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " ";

        InspectionTaskPo inspectionTaskPo = new InspectionTaskPo();
        inspectionTaskPo.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskPo.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionTaskPo.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());
        inspectionTaskPo.setPlanInsTime(nowTime + tmpInspectionPlanStaffDto.getStartTime() + ":00");
        inspectionTaskPo.setPlanUserId(tmpInspectionPlanStaffDto.getStaffId());
        inspectionTaskPo.setPlanUserName(tmpInspectionPlanStaffDto.getStaffName());
        inspectionTaskPo.setSignType(tmpInspectionPlanDto.getSignType());
        inspectionTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));


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
            inspectionTaskDetailPo.setTaskDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskDetailId));
            inspectionTaskDetailPos.add(inspectionTaskDetailPo);
        }


        inspectionTaskPos.add(inspectionTaskPo);


    }

    /**
     * 隔天
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean dealNextDayInspectionPlan(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                              InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                              List<InspectionTaskDetailPo> inspectionTaskDetailPos) {
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskDto.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        inspectionTaskDto.setScopeTime(calendar.getTime());
        inspectionTaskDto.setCreateTime(new Date());
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) { // 已经生成过
            return true;
        }

        return false;
    }

    /**
     * 每周
     *
     * @param tmpInspectionPlanDto
     * @param taskDto
     * @param communityDto
     */
    private boolean dealWeekInspectionPlan(InspectionPlanDto tmpInspectionPlanDto, TaskDto taskDto, CommunityDto communityDto,
                                           InspectionPlanStaffDto tmpInspectionPlanStaffDto, List<InspectionTaskPo> inspectionTaskPos,
                                           List<InspectionTaskDetailPo> inspectionTaskDetailPos) {
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setCommunityId(tmpInspectionPlanDto.getCommunityId());
        inspectionTaskDto.setInspectionPlanId(tmpInspectionPlanDto.getInspectionPlanId());
        inspectionTaskDto.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        inspectionTaskDto.setScopeTime(calendar.getTime());
        inspectionTaskDto.setCreateTime(new Date());
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) { // 已经生成过
            return true;
        }

        return false;

    }


}
