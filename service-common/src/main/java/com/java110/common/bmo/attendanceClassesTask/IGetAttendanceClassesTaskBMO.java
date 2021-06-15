package com.java110.common.bmo.attendanceClassesTask;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import org.springframework.http.ResponseEntity;
public interface IGetAttendanceClassesTaskBMO {


    /**
     * 查询考勤任务
     * add by wuxw
     * @param  attendanceClassesTaskDto
     * @return
     */
    ResponseEntity<String> get(AttendanceClassesTaskDto attendanceClassesTaskDto);


}
