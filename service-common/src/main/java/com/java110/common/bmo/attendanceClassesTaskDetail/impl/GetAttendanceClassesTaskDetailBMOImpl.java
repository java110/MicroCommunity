package com.java110.common.bmo.attendanceClassesTaskDetail.impl;

import com.java110.common.bmo.attendanceClassesTaskDetail.IGetAttendanceClassesTaskDetailBMO;
import com.java110.dto.attendanceClassesTask.AttendanceClassesTaskDto;
import com.java110.dto.attendanceClassesTaskDetail.AttendanceClassesTaskDetailDto;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttendanceClassesTaskDetailBMOImpl")
public class GetAttendanceClassesTaskDetailBMOImpl implements IGetAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto) {


        int count = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetailsCount(attendanceClassesTaskDetailDto);

        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetailDtos = null;
        if (count > 0) {
            attendanceClassesTaskDetailDtos = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetails(attendanceClassesTaskDetailDto);
        } else {
            attendanceClassesTaskDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDetailDto.getRow()), count, attendanceClassesTaskDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> getMonthAttendance(AttendanceClassesTaskDto attendanceClassesTaskDto) {
        return null;
    }

}
