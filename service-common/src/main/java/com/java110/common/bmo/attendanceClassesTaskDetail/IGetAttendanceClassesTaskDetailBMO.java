package com.java110.common.bmo.attendanceClassesTaskDetail;

import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import org.springframework.http.ResponseEntity;

public interface IGetAttendanceClassesTaskDetailBMO {


    /**
     * 查询考勤任务明细
     * add by wuxw
     *
     * @param attendanceClassesTaskDetailDto
     * @return
     */
    ResponseEntity<String> get(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);


    /**
     * 查询月考勤
     * add by wuxw
     *
     * @param attendanceClassesTaskDto
     * @return
     */
    ResponseEntity<String> getMonthAttendance(AttendanceClassesTaskDto attendanceClassesTaskDto);
}
