package com.java110.common.bmo.attendanceClassesTask.impl;

import com.java110.common.bmo.attendanceClassesTask.IDeleteAttendanceClassesTaskBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttendanceClassesTaskBMOImpl")
public class DeleteAttendanceClassesTaskBMOImpl implements IDeleteAttendanceClassesTaskBMO {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AttendanceClassesTaskPo attendanceClassesTaskPo) {

        int flag = attendanceClassesTaskInnerServiceSMOImpl.deleteAttendanceClassesTask(attendanceClassesTaskPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
