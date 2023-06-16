package com.java110.common.bmo.attendanceClassesTask;

import com.java110.po.attendance.AttendanceClassesTaskPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttendanceClassesTaskBMO {


    /**
     * 修改考勤任务
     * add by wuxw
     *
     * @param attendanceClassesTaskPo
     * @return
     */
    ResponseEntity<String> update(AttendanceClassesTaskPo attendanceClassesTaskPo);


}
