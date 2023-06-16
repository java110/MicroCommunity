package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.classes.ScheduleClassesDto;
import com.java110.dto.classes.ScheduleClassesDayDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesTimeDto;
import com.java110.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 排班表导出
 */
@Service("reportStaffMonthScheduleClasses")
public class ReportStaffMonthScheduleClassesAdapt implements IExportDataAdapt {

    private static final int MAX_ROW = 200;

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesV1InnerServiceSMO scheduleClassesV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("排班表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("员工名称");

        JSONObject reqJson = exportDataDto.getReqJson();

        String curMonth = reqJson.getString("curDate");
        String curMonthDay = curMonth + "-01";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(curMonthDay));
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= maxDay; day++) {
            row.createCell(day).setCellValue(day + "");
        }

        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(reqJson.getString("staffId"));
        scheduleClassesStaffDto.setStaffNameLike(reqJson.getString("staffNameLike"));
        scheduleClassesStaffDto.setScheduleId(reqJson.getString("scheduleId"));
        scheduleClassesStaffDto.setStoreId(reqJson.getString("storeId"));

        getStaffMonthScheduleClasses(sheet, scheduleClassesStaffDto, reqJson);

        return workbook;
    }

    private void getStaffMonthScheduleClasses(Sheet sheet, ScheduleClassesStaffDto scheduleClassesStaffDto, JSONObject reqJson) {
        scheduleClassesStaffDto.setPage(1);
        scheduleClassesStaffDto.setRow(MAX_ROW);
        long count = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffsCount(scheduleClassesStaffDto);
        int record = (int) Math.ceil((double) count / (double) MAX_ROW);

        List<ScheduleClassesStaffDto> scheduleClassesStaffDtos = null;

        for (int page = 1; page <= record; page++) {
            scheduleClassesStaffDto.setPage(page);
            scheduleClassesStaffDto.setRow(MAX_ROW);
            scheduleClassesStaffDtos = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffs(scheduleClassesStaffDto);
            for (ScheduleClassesStaffDto tmpScheduleClassesStaffDto : scheduleClassesStaffDtos) {
                computeStaffCurMonthWorkday(tmpScheduleClassesStaffDto, reqJson);

            }
            appendData(scheduleClassesStaffDtos, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(List<ScheduleClassesStaffDto> scheduleClassesStaffDtos, Sheet sheet, int step) {

        Row row = null;
        ScheduleClassesStaffDto scheduleClassesStaffDto = null;
        String cellValue = "";
        ScheduleClassesDayDto day = null;
        for (int roomIndex = 0; roomIndex < scheduleClassesStaffDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            scheduleClassesStaffDto = scheduleClassesStaffDtos.get(roomIndex);
            row.createCell(0).setCellValue(scheduleClassesStaffDto.getStaffName());
            for (int dayIndex = 1; dayIndex <= scheduleClassesStaffDto.getDays().size(); dayIndex++) {
                day = scheduleClassesStaffDto.getDays().get(dayIndex-1);
                if (ScheduleClassesDayDto.WORKDAY_NO.equals(day.getWorkday())) {
                    cellValue = "休息";
                } else {
                    cellValue = day.getWorkdayName()+"\r\n";
                    for (ScheduleClassesTimeDto time : day.getTimes()) {
                        cellValue += (time.getStartTime() + "-" + time.getEndTime());
                    }
                }
                row.createCell(dayIndex).setCellValue(cellValue);
            }
        }
    }

    /**
     * 计算 员工 排班情况
     *
     * @param scheduleClassesStaffDto
     * @param reqJson
     */
    private void computeStaffCurMonthWorkday(ScheduleClassesStaffDto scheduleClassesStaffDto, JSONObject reqJson) {

        ScheduleClassesDto scheduleClassesDto = new ScheduleClassesDto();
        scheduleClassesDto.setScheduleId(scheduleClassesStaffDto.getScheduleId());
        List<ScheduleClassesDto> scheduleClassesDtos = scheduleClassesV1InnerServiceSMOImpl.queryScheduleClassess(scheduleClassesDto);
        //这里 如果没有员工排班 那么就认为 员工一直在上班
        if (scheduleClassesDtos == null || scheduleClassesDtos.size() < 1) {
            return;
        }

        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setScheduleId(scheduleClassesDtos.get(0).getScheduleId());
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDays(scheduleClassesDayDto);

        //设置问题 ，这里默认反馈在线
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            return;
        }

        String curMonth = reqJson.getString("curDate");
        String curMonthDay = curMonth + "-01";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(curMonthDay));
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (ScheduleClassesDto.SCHEDULE_TYPE_DAY.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doDay(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        } else if (ScheduleClassesDto.SCHEDULE_TYPE_WEEK.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doWeek(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        } else if (ScheduleClassesDto.SCHEDULE_TYPE_MONTH.equals(scheduleClassesDtos.get(0).getScheduleType())) {
            doMonth(scheduleClassesStaffDto, scheduleClassesDtos.get(0), scheduleClassesDayDtos, curMonth, maxDay);
        }


    }

    private void doDay(ScheduleClassesStaffDto scheduleClassesStaffDto,
                       ScheduleClassesDto scheduleClassesDto,
                       List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                       String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            String today = curMonth + "-" + day;

            int scheduleCycle = Integer.parseInt(scheduleClassesDto.getScheduleCycle());

            int allDay = DateUtil.daysBetween(scheduleClassesDto.getComputeTime(), today) + 1;
            curDay = allDay % scheduleCycle;
//
//            if (curDay == 0 && day == 1) {
//                curDay = 1;
//            }
//            if (curDay == 0 && day > 1) {
//                curDay = scheduleCycle;
//            }
            if (curDay == 0) {
                curDay = scheduleCycle;
            }


            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);

        }

        scheduleClassesStaffDto.setDays(days);
    }

    private void doWeek(ScheduleClassesStaffDto scheduleClassesStaffDto,
                        ScheduleClassesDto scheduleClassesDto,
                        List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                        String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            Calendar today = Calendar.getInstance();
            today.setTime(DateUtil.getDateFromStringB(curMonth + "-" + day));
            int week = today.get(Calendar.WEEK_OF_MONTH);
            curDay = today.get(Calendar.DAY_OF_WEEK);

            //一周第一天是否为星期天
            boolean isFirstSunday = (today.getFirstDayOfWeek() == Calendar.SUNDAY);
            //获取周几
            //若一周第一天为星期天，则-1
            if (isFirstSunday) {
                curDay = curDay - 1;
                if (curDay == 0) {
                    curDay = 7;
                }
            }

            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                if ((curDay + "").equals(scheduleClassesDayDto1.getDay()) && (week + "").equals(scheduleClassesDayDto1.getWeekFlag())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto == null) { // 没有设置周
                for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                    if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                        tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                    }
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);

        }

        scheduleClassesStaffDto.setDays(days);
    }


    private void doMonth(ScheduleClassesStaffDto scheduleClassesStaffDto,
                         ScheduleClassesDto scheduleClassesDto,
                         List<ScheduleClassesDayDto> scheduleClassesDayDtos,
                         String curMonth, int maxDay) {
        List<ScheduleClassesDayDto> days = new ArrayList<>();
        ScheduleClassesDayDto scDay = null;
        ScheduleClassesDayDto tmpScheduleClassesDayDto = null;
        int curDay = 1;
        for (int day = 1; day <= maxDay; day++) {
            scDay = new ScheduleClassesDayDto();
            curDay = day;
            scDay.setDay(day + "");
            //计算 排班
            for (ScheduleClassesDayDto scheduleClassesDayDto1 : scheduleClassesDayDtos) {
                if ((curDay + "").equals(scheduleClassesDayDto1.getDay())) {
                    tmpScheduleClassesDayDto = scheduleClassesDayDto1;
                }
            }
            if (tmpScheduleClassesDayDto != null) {
                scDay.setWorkday(tmpScheduleClassesDayDto.getWorkday());
                scDay.setTimes(tmpScheduleClassesDayDto.getTimes());
            }
            days.add(scDay);
        }

        scheduleClassesStaffDto.setDays(days);
    }


}
