package com.java110.common.bmo.attendanceClassesTask;

import com.java110.po.attendance.AttendanceClassesTaskPo;
import com.java110.po.attendance.AttendanceClassesTaskDetailPo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaveAttendanceClassesTaskBMO {


    /**
     * 添加考勤任务
     * add by wuxw
     * @param attendanceClassesTaskPo
     * @return
     */
    ResponseEntity<String> save(AttendanceClassesTaskPo attendanceClassesTaskPo,
                                List<AttendanceClassesTaskDetailPo> attendanceClassesTaskDetailPos);


}
