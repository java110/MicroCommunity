package com.java110.common.bmo.attendanceLog;

import com.java110.po.attendanceLog.AttendanceLogPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAttendanceLogBMO {


    /**
     * 添加考勤日志
     * add by wuxw
     * @param attendanceLogPo
     * @return
     */
    ResponseEntity<String> save(AttendanceLogPo attendanceLogPo);


}
