package com.java110.common.bmo.attendanceLog;
import com.java110.dto.attendanceClasses.AttendanceLogDto;
import org.springframework.http.ResponseEntity;
public interface IGetAttendanceLogBMO {


    /**
     * 查询考勤日志
     * add by wuxw
     * @param  attendanceLogDto
     * @return
     */
    ResponseEntity<String> get(AttendanceLogDto attendanceLogDto);


}
