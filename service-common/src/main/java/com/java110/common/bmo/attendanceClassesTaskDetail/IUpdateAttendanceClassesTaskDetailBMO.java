package com.java110.common.bmo.attendanceClassesTaskDetail;

import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAttendanceClassesTaskDetailBMO {


    /**
     * 修改考勤任务明细
     * add by wuxw
     *
     * @param attendanceClassesTaskDetailPo
     * @return
     */
    ResponseEntity<String> update(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo,boolean finishAllTaskDetail);


}
