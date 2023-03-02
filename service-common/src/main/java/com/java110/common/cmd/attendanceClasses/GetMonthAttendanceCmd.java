package com.java110.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.intf.report.IReportAttendanceInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询月考勤
 */
@Java110Cmd(serviceCode = "/attendanceClass/getMonthAttendance")
public class GetMonthAttendanceCmd extends Cmd {

    @Autowired
    private IReportAttendanceInnerServiceSMO reportAttendanceInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "classesId", "未包含考勤班次");
        Assert.hasKeyAndValue(reqJson, "taskYear", "未包含年");
        Assert.hasKeyAndValue(reqJson, "taskMonth", "未包含月");
        String storeId = context.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");

        AttendanceClassesTaskDto attendanceClassesTaskDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDto.class);
        attendanceClassesTaskDto.setStoreId(storeId);
        attendanceClassesTaskDto.setClassId(reqJson.getString("classesId"));
        int count = reportAttendanceInnerServiceSMOImpl.getMonthAttendanceCount(attendanceClassesTaskDto);

        List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = null;
        if (count > 0) {
            attendanceClassesTaskDtos = reportAttendanceInnerServiceSMOImpl.getMonthAttendance(attendanceClassesTaskDto);
            //输入考勤明细
            refreshDetail(attendanceClassesTaskDtos, reqJson);
        } else {
            attendanceClassesTaskDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) attendanceClassesTaskDto.getRow()), count, attendanceClassesTaskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshDetail(List<AttendanceClassesTaskDto> attendanceClassesTaskDtos, JSONObject reqJson) {

        if (attendanceClassesTaskDtos == null || attendanceClassesTaskDtos.size() < 1) {
            return;
        }

        List<String> staffIds = new ArrayList<>();
        for (AttendanceClassesTaskDto attendanceClassesTaskDto : attendanceClassesTaskDtos) {
            staffIds.add(attendanceClassesTaskDto.getStaffId());
        }

        AttendanceClassesTaskDto tmpAttendanceClassesTaskDto = new AttendanceClassesTaskDto();
        tmpAttendanceClassesTaskDto.setClassId(reqJson.getString("classesId"));
        tmpAttendanceClassesTaskDto.setTaskYear(reqJson.getString("taskYear"));
        tmpAttendanceClassesTaskDto.setTaskMonth(reqJson.getString("taskMonth"));
        tmpAttendanceClassesTaskDto.setStaffIds(staffIds.toArray(new String[staffIds.size()]));
        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetailDtos = reportAttendanceInnerServiceSMOImpl.getMonthAttendanceDetail(tmpAttendanceClassesTaskDto);

        JSONObject days = null;
        List<AttendanceClassesTaskDetailDto> tAttendanceClassesTaskDetailDto = null;
        for (AttendanceClassesTaskDto attendanceClassesTaskDto : attendanceClassesTaskDtos) {
            days = attendanceClassesTaskDto.getDays();
            if (days == null) {
                days = new JSONObject();
                attendanceClassesTaskDto.setDays(days);
            }
            for (AttendanceClassesTaskDetailDto tmpAttendanceClassesTaskDetailDto : attendanceClassesTaskDetailDtos) {
                if(!attendanceClassesTaskDto.getStaffId().equals(tmpAttendanceClassesTaskDetailDto.getStaffId())){
                    continue;
                }
                if (days.containsKey(tmpAttendanceClassesTaskDetailDto.getTaskDay())
                ) {
                    tAttendanceClassesTaskDetailDto = (List<AttendanceClassesTaskDetailDto>) days.get(tmpAttendanceClassesTaskDetailDto.getTaskDay());
                    tAttendanceClassesTaskDetailDto.add(tmpAttendanceClassesTaskDetailDto);
                } else {
                    tAttendanceClassesTaskDetailDto = new ArrayList<>();
                    tAttendanceClassesTaskDetailDto.add(tmpAttendanceClassesTaskDetailDto);
                    days.put(tmpAttendanceClassesTaskDetailDto.getTaskDay(), tAttendanceClassesTaskDetailDto);
                }
            }
        }

    }
}
