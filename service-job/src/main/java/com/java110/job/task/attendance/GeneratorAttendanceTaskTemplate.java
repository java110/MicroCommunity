package com.java110.job.task.attendance;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.attendance.AttendanceClassesDto;
import com.java110.dto.attendance.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendance.AttendanceClassesTaskDto;
import com.java110.dto.attendance.AttendanceClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesTimeDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.attendance.AttendanceClassesTaskPo;
import com.java110.po.attendance.AttendanceClassesTaskDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 考勤任务生成
 */
@Component
public class GeneratorAttendanceTaskTemplate extends TaskSystemQuartz {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        if (storeDtos == null || storeDtos.size() < 1) {
            return;
        }

        for (StoreDto tmpStoreDto : storeDtos) {
            try {
                generatorAttendance(tmpStoreDto);
            } catch (Exception e) {
                logger.error("生成考勤任务失败", e);
            }
        }

    }

    /**
     * 考勤任务
     *
     * @param tmpStoreDto
     */
    private void generatorAttendance(StoreDto tmpStoreDto) {

        AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
        attendanceClassesDto.setStoreId(tmpStoreDto.getStoreId());
        List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);

        if (attendanceClassesDtos == null || attendanceClassesDtos.size() < 1) {
            return;
        }

        for (AttendanceClassesDto tmpAttendanceClassesDto : attendanceClassesDtos) {
            try {
                doGeneratorAttendance(tmpStoreDto, tmpAttendanceClassesDto);
            } catch (Exception e) {
                logger.error("生成考勤任务失败", e);
            }
        }

    }

    /**
     * 生成巡检任务
     *
     * @param tmpStoreDto
     * @param tmpAttendanceClassesDto
     */
    private void doGeneratorAttendance(StoreDto tmpStoreDto, AttendanceClassesDto tmpAttendanceClassesDto) {

        //查询组织 关联的员工

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setClassesId(tmpAttendanceClassesDto.getClassesId());
        attendanceClassesStaffDto.setStoreId(tmpStoreDto.getStoreId());
        List<AttendanceClassesStaffDto> attendanceClassesStaffs = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
        if (attendanceClassesStaffs == null || attendanceClassesStaffs.size() < 1) {
            return;
        }

        for (AttendanceClassesStaffDto tmpAttendanceClassesStaffDto : attendanceClassesStaffs) {
            try {
                doGeneratorStaffAttendance(tmpStoreDto, tmpAttendanceClassesDto, tmpAttendanceClassesStaffDto);
            } catch (Exception e) {
                logger.error("员工生成考勤任务失败" + tmpAttendanceClassesStaffDto.getStaffId(), e);
            }
        }
    }

    /**
     * 生成员工 考勤
     *
     * @param tmpStoreDto
     * @param tmpAttendanceClassesDto
     * @param tmpAttendanceClassesStaffDto
     */
    private void doGeneratorStaffAttendance(StoreDto tmpStoreDto, AttendanceClassesDto tmpAttendanceClassesDto, AttendanceClassesStaffDto tmpAttendanceClassesStaffDto) {

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(tmpAttendanceClassesStaffDto.getStaffId());
        scheduleClassesStaffDto.setToday(new Date());
        scheduleClassesStaffDto = scheduleClassesStaffV1InnerServiceSMOImpl.staffIsWork(scheduleClassesStaffDto);

        List<ScheduleClassesTimeDto> times = scheduleClassesStaffDto.getTimes();

        if (times == null || times.size() < 1) {
            return;
        }


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        AttendanceClassesTaskDto attendanceClassesTaskDto = new AttendanceClassesTaskDto();
        attendanceClassesTaskDto.setStaffId(tmpAttendanceClassesStaffDto.getStaffId());
        attendanceClassesTaskDto.setClassId(tmpAttendanceClassesDto.getClassesId());
        attendanceClassesTaskDto.setTaskYear(year + "");
        attendanceClassesTaskDto.setTaskMonth(month + "");
        attendanceClassesTaskDto.setTaskDay(day + "");
        List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = attendanceClassesTaskInnerServiceSMOImpl.queryAttendanceClassesTasks(attendanceClassesTaskDto);

        //已经生成过考勤任务
        if (attendanceClassesTaskDtos != null && attendanceClassesTaskDtos.size() > 0) {
            return;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(tmpAttendanceClassesStaffDto.getStaffId());
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "员工不存在");


        AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
        attendanceClassesTaskPo.setStaffId(tmpAttendanceClassesStaffDto.getStaffId());
        attendanceClassesTaskPo.setClassId(tmpAttendanceClassesDto.getClassesId());
        attendanceClassesTaskPo.setTaskYear(year + "");
        attendanceClassesTaskPo.setTaskMonth(month + "");
        attendanceClassesTaskPo.setTaskDay(day + "");
        attendanceClassesTaskPo.setState(AttendanceClassesTaskDto.STATE_WAIT);
        attendanceClassesTaskPo.setStaffName(userDtos.get(0).getName());
        attendanceClassesTaskPo.setStoreId(tmpStoreDto.getStoreId());
        attendanceClassesTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId("11"));

        int flag = attendanceClassesTaskInnerServiceSMOImpl.saveAttendanceClassesTask(attendanceClassesTaskPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存考勤任务失败");
        }
        ScheduleClassesTimeDto tmpScheduleClassesTimeDto = null;
        boolean isLast = false;
        for (int timeIndex = 0; timeIndex < times.size(); timeIndex++) {
            tmpScheduleClassesTimeDto = times.get(timeIndex);
            if (timeIndex == (times.size() - 1)) {
                isLast = true;
            }
            doGeneratorStaffAttendanceTime(tmpStoreDto, tmpAttendanceClassesDto, tmpAttendanceClassesStaffDto, tmpScheduleClassesTimeDto, attendanceClassesTaskPo, isLast);
        }

    }

    /**
     * 生成员工考勤任务
     *
     * @param tmpStoreDto
     * @param tmpAttendanceClassesDto
     * @param tmpAttendanceClassesStaffDto
     * @param tmpScheduleClassesTimeDto
     */
    private void doGeneratorStaffAttendanceTime(StoreDto tmpStoreDto, AttendanceClassesDto tmpAttendanceClassesDto, AttendanceClassesStaffDto tmpAttendanceClassesStaffDto,
                                                ScheduleClassesTimeDto tmpScheduleClassesTimeDto, AttendanceClassesTaskPo attendanceClassesTaskPo, boolean islast) {

        String curDate = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);

        String startTimeStr = tmpScheduleClassesTimeDto.getStartTime();
        String endTimeStr = tmpScheduleClassesTimeDto.getEndTime();


        //
        //alter table attendance_classes_task_detail add COLUMN leave_value varchar(12) not null comment '正常或者早退时间，spec_cd 1001 是正常考勤 2002 是早退时间';
        //
        //alter table attendance_classes_task_detail add COLUMN late_value varchar(12) not null comment '正常或者早退时间，spec_cd 1001 是迟到 2002 是正常时间';

        String value = curDate + " " + startTimeStr + ":00";
        Date startValueDate = DateUtil.getDateFromStringA(value);

        int timeOffset = Integer.parseInt(tmpAttendanceClassesDto.getTimeOffset());
        int maxLastOffset = Integer.parseInt(tmpAttendanceClassesDto.getMaxLastOffset());

        AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo = new AttendanceClassesTaskDetailPo();
        attendanceClassesTaskDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("12"));
        attendanceClassesTaskDetailPo.setTaskId(attendanceClassesTaskPo.getTaskId());
        attendanceClassesTaskDetailPo.setSpecCd(AttendanceClassesTaskDetailDto.SPEC_CD_START);
        attendanceClassesTaskDetailPo.setValue(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startValueDate);
        calendar.add(Calendar.MINUTE, timeOffset * -1);
        attendanceClassesTaskDetailPo.setLeaveValue(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));

        int lateOffset = Integer.parseInt(tmpAttendanceClassesDto.getLateOffset());
        calendar = Calendar.getInstance();
        calendar.setTime(startValueDate);
        calendar.add(Calendar.MINUTE, lateOffset);
        attendanceClassesTaskDetailPo.setLateValue(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        attendanceClassesTaskDetailPo.setState(AttendanceClassesTaskDetailDto.STATE_WAIT);
        attendanceClassesTaskDetailPo.setStoreId(tmpStoreDto.getStoreId());

        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存考勤任务失败");
        }

        value = curDate + " " + endTimeStr + ":00";
        Date endValueDate = DateUtil.getDateFromStringA(value);

        if (endValueDate.getTime() < startValueDate.getTime()) {
            Calendar endDateCal = Calendar.getInstance();
            endDateCal.setTime(endValueDate);
            endDateCal.add(Calendar.DAY_OF_MONTH, 1);
            endValueDate = endDateCal.getTime();
            value = DateUtil.getFormatTimeString(endValueDate, DateUtil.DATE_FORMATE_STRING_A);
        }

        int leaveOffset = Integer.parseInt(tmpAttendanceClassesDto.getLeaveOffset());

        attendanceClassesTaskDetailPo = new AttendanceClassesTaskDetailPo();
        attendanceClassesTaskDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("12"));
        attendanceClassesTaskDetailPo.setTaskId(attendanceClassesTaskPo.getTaskId());
        attendanceClassesTaskDetailPo.setSpecCd(AttendanceClassesTaskDetailDto.SPEC_CD_END);
        attendanceClassesTaskDetailPo.setValue(value);

        calendar = Calendar.getInstance();
        calendar.setTime(endValueDate);
        calendar.add(Calendar.MINUTE, leaveOffset * -1);
        attendanceClassesTaskDetailPo.setLeaveValue(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        calendar = Calendar.getInstance();
        calendar.setTime(endValueDate);
        if (!islast) {
            calendar.add(Calendar.MINUTE, timeOffset);
        } else {
            calendar.add(Calendar.MINUTE, maxLastOffset);
        }
        attendanceClassesTaskDetailPo.setLateValue(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        attendanceClassesTaskDetailPo.setState(AttendanceClassesTaskDetailDto.STATE_WAIT);
        attendanceClassesTaskDetailPo.setStoreId(tmpStoreDto.getStoreId());

        flag = attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存考勤任务失败");
        }


    }
}
