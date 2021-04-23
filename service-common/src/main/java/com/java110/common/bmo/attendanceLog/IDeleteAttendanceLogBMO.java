package com.java110.common.bmo.attendanceLog;
import com.java110.po.attendanceLog.AttendanceLogPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAttendanceLogBMO {


    /**
     * 修改考勤日志
     * add by wuxw
     * @param attendanceLogPo
     * @return
     */
    ResponseEntity<String> delete(AttendanceLogPo attendanceLogPo);


}
