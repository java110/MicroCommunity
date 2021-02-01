package com.java110.common.bmo.attendanceClassesTaskDetail;
import com.java110.dto.attendanceClassesTaskDetail.AttendanceClassesTaskDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetAttendanceClassesTaskDetailBMO {


    /**
     * 查询考勤任务明细
     * add by wuxw
     * @param  attendanceClassesTaskDetailDto
     * @return
     */
    ResponseEntity<String> get(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);


}
