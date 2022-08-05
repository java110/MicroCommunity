package com.java110.common.bmo.attendanceClassesTaskDetail.impl;

import com.java110.common.bmo.attendanceClassesTaskDetail.IUpdateAttendanceClassesTaskDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAttendanceClassesTaskDetailBMOImpl")
public class UpdateAttendanceClassesTaskDetailBMOImpl implements IUpdateAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;


    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo, boolean finishAllTaskDetail) {

        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.updateAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        if (finishAllTaskDetail) {
            AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
            attendanceClassesTaskPo.setTaskId(attendanceClassesTaskDetailPo.getTaskId());
            attendanceClassesTaskPo.setState("30000");
            attendanceClassesTaskPo.setStatusCd("0");
            attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);
        }else{
            AttendanceClassesTaskPo attendanceClassesTaskPo = new AttendanceClassesTaskPo();
            attendanceClassesTaskPo.setTaskId(attendanceClassesTaskDetailPo.getTaskId());
            attendanceClassesTaskPo.setState("20000");
            attendanceClassesTaskPo.setStatusCd("0");
            attendanceClassesTaskInnerServiceSMOImpl.updateAttendanceClassesTask(attendanceClassesTaskPo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
