package com.java110.common.bmo.attendanceLog.impl;

import com.java110.common.bmo.attendanceLog.IGetAttendanceLogBMO;
import com.java110.dto.attendanceClasses.AttendanceLogDto;
import com.java110.intf.common.IAttendanceLogInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttendanceLogBMOImpl")
public class GetAttendanceLogBMOImpl implements IGetAttendanceLogBMO {

    @Autowired
    private IAttendanceLogInnerServiceSMO attendanceLogInnerServiceSMOImpl;

    /**
     * @param attendanceLogDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AttendanceLogDto attendanceLogDto) {


        int count = attendanceLogInnerServiceSMOImpl.queryAttendanceLogsCount(attendanceLogDto);

        List<AttendanceLogDto> attendanceLogDtos = null;
        if (count > 0) {
            attendanceLogDtos = attendanceLogInnerServiceSMOImpl.queryAttendanceLogs(attendanceLogDto);
        } else {
            attendanceLogDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceLogDto.getRow()), count, attendanceLogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
