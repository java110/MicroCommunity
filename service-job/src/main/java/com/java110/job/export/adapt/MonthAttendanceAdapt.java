package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportAttendanceInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("monthAttendance")
public class MonthAttendanceAdapt implements IExportDataAdapt {
    private static final int MAX_ROW = 50;

    @Autowired
    private IReportAttendanceInnerServiceSMO reportAttendanceInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet(reqJson.getIntValue("taskYear") + "年" + reqJson.getIntValue("taskMonth") + "月考勤表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("部门");
        row.createCell(1).setCellValue("员工");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, reqJson.getIntValue("taskYear"));
        calendar.set(Calendar.MONTH, reqJson.getIntValue("taskMonth") - 1);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDayOfMonth; day++) {
            row.createCell(day + 1).setCellValue(day + "日");
        }

        row.createCell(1 + maxDayOfMonth + 1).setCellValue("正常考勤");
        row.createCell(1 + maxDayOfMonth + 2).setCellValue("迟到");
        row.createCell(1 + maxDayOfMonth + 3).setCellValue("早退");
        row.createCell(1 + maxDayOfMonth + 4).setCellValue("旷工");
        row.createCell(1 + maxDayOfMonth + 5).setCellValue("免考勤");


        AttendanceClassesTaskDto attendanceClassesTaskDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesTaskDto.class);
        attendanceClassesTaskDto.setStoreId(reqJson.getString("storeId"));
        attendanceClassesTaskDto.setClassId(reqJson.getString("classesId"));
        //查询数据
        getAttendanceClassesTask(sheet, attendanceClassesTaskDto, reqJson, maxDayOfMonth);

        return workbook;

    }

    private void getAttendanceClassesTask(Sheet sheet, AttendanceClassesTaskDto attendanceClassesTaskDto, JSONObject reqJson, int maxDayOfMonth) {

        int count = reportAttendanceInnerServiceSMOImpl.getMonthAttendanceCount(attendanceClassesTaskDto);
        count = (int) Math.ceil((double) count / (double) MAX_ROW);
        if (count < 1) {
            return;
        }

        for (int page = 1; page <= count; page++) {
            attendanceClassesTaskDto.setPage(page);
            attendanceClassesTaskDto.setRow(MAX_ROW);
            List<AttendanceClassesTaskDto> attendanceClassesTaskDtos = reportAttendanceInnerServiceSMOImpl.getMonthAttendance(attendanceClassesTaskDto);
            //输入考勤明细
            refreshDetail(attendanceClassesTaskDtos, reqJson);
            appendData(attendanceClassesTaskDtos, sheet, (page - 1) * MAX_ROW, maxDayOfMonth, reqJson);
        }
    }

    private void appendData(List<AttendanceClassesTaskDto> attendanceClassesTaskDtos, Sheet sheet, int step, int maxDayOfMonth, JSONObject reqJson) {

        Row row = null;
        JSONObject dayObj = null;
        AttendanceClassesTaskDto attendanceClassesTaskDto = null;
        List<AttendanceClassesTaskDetailDto> detailDtos = null;
        String value = "";

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int taskYear = reqJson.getIntValue("taskYear");
        int taskMonth = reqJson.getIntValue("taskMonth");
        for (int roomIndex = 0; roomIndex < attendanceClassesTaskDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            attendanceClassesTaskDto = attendanceClassesTaskDtos.get(roomIndex);
            row.createCell(0).setCellValue(attendanceClassesTaskDto.getDepartmentName());
            row.createCell(1).setCellValue(attendanceClassesTaskDto.getStaffName());
            dayObj = attendanceClassesTaskDto.getDays();
            for (int day = 1; day <= maxDayOfMonth; day++) {

                if(taskYear == calendar.get(Calendar.YEAR) && taskMonth == (calendar.get(Calendar.MONTH)+1) && day > today){
                    row.createCell(day + 1).setCellValue("未到时间");
                    continue;
                }

                if (!dayObj.containsKey(day)) {
                    row.createCell(day + 1).setCellValue("无需考勤");
                    continue;
                }
                if (dayObj.get(day) == null) {
                    row.createCell(day + 1).setCellValue("无需考勤");
                    continue;
                }
                detailDtos = (List<AttendanceClassesTaskDetailDto>) dayObj.get(day);
                if (detailDtos == null || detailDtos.size() < 1) {
                    row.createCell(day + 1).setCellValue("无需考勤");
                    continue;
                }

                value = "";
                for (AttendanceClassesTaskDetailDto detailDto : detailDtos) {
                    if (AttendanceClassesTaskDetailDto.SPEC_CD_START.equals(detailDto.getSpecCd())) {
                        value += "上班：";
                    } else {
                        value += "下班：";
                    }

                    if (!AttendanceClassesTaskDetailDto.STATE_WAIT.equals(detailDto.getState())) {
                        value += (detailDto.getCheckTime() + "(" + detailDto.getStateName() + ");" + String.valueOf((char) 10));
                    } else {
                        value += (" - (" + detailDto.getStateName() + ");" + String.valueOf((char) 10));
                    }
                }
                row.createCell(day + 1).setCellValue(value);
            }

            row.createCell(1 + maxDayOfMonth + 1).setCellValue(attendanceClassesTaskDto.getClockIn());
            row.createCell(1 + maxDayOfMonth + 2).setCellValue(attendanceClassesTaskDto.getLate());
            row.createCell(1 + maxDayOfMonth + 3).setCellValue(attendanceClassesTaskDto.getEarly());
            row.createCell(1 + maxDayOfMonth + 4).setCellValue(attendanceClassesTaskDto.getNoClockIn());
            row.createCell(1 + maxDayOfMonth + 5).setCellValue(attendanceClassesTaskDto.getFree());
        }
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
