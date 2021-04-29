package com.java110.common.bmo.attendanceLog.impl;

import com.java110.common.bmo.attendanceLog.IDeleteAttendanceLogBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAttendanceLogInnerServiceSMO;
import com.java110.po.attendanceLog.AttendanceLogPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAttendanceLogBMOImpl")
public class DeleteAttendanceLogBMOImpl implements IDeleteAttendanceLogBMO {

    @Autowired
    private IAttendanceLogInnerServiceSMO attendanceLogInnerServiceSMOImpl;

    /**
     * @param attendanceLogPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AttendanceLogPo attendanceLogPo) {

        int flag = attendanceLogInnerServiceSMOImpl.deleteAttendanceLog(attendanceLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
