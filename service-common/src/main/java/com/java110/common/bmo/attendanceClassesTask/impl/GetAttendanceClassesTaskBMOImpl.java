package com.java110.common.bmo.attendanceClassesTask.impl;

import com.java110.common.bmo.attendanceClassesTask.IGetAttendanceClassesTaskBMO;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAttendanceClassesTaskBMOImpl")
public class GetAttendanceClassesTaskBMOImpl implements IGetAttendanceClassesTaskBMO {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    /**
     * @param attendanceClassesTaskDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AttendanceClassesTaskDto attendanceClassesTaskDto) {


        int count = attendanceClassesTaskInnerServiceSMOImpl.queryAttendanceClassesTasksCount(attendanceClassesTaskDto);

        List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = null;
        if (count > 0) {
            attendanceClassesTaskDtos = attendanceClassesTaskInnerServiceSMOImpl.queryAttendanceClassesTasks(attendanceClassesTaskDto);
        } else {
            attendanceClassesTaskDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDto.getRow()), count, attendanceClassesTaskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
