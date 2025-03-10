package com.java110.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.attendance.AttendanceClassesTaskDto;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "attendanceClasses.queryAdminAttendanceClasses")
public class QueryAdminAttendanceClassesTaskCmd extends Cmd {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        AttendanceClassesTaskDto attendanceClassesTaskDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDto.class);

        attendanceClassesTaskDto.setStoreId("");
        String date = reqJson.getString("date");
        if (!StringUtil.isEmpty(date)) {
            String[] dates = date.split("-");
            attendanceClassesTaskDto.setTaskYear(dates[0] + "");
            attendanceClassesTaskDto.setTaskMonth(dates[1] + "");
            if (dates.length == 3) {
                attendanceClassesTaskDto.setTaskDay(dates[2] + "");
            }
        }


        int count = attendanceClassesTaskInnerServiceSMOImpl.queryAttendanceClassesTasksCount(attendanceClassesTaskDto);

        List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = null;
        if (count > 0) {
            attendanceClassesTaskDtos = attendanceClassesTaskInnerServiceSMOImpl.queryAttendanceClassesTasks(attendanceClassesTaskDto);
        } else {
            attendanceClassesTaskDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDto.getRow()), count, attendanceClassesTaskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
