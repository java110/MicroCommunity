package com.java110.report.dao;

import com.java110.dto.attendanceClassesTask.AttendanceClassesTaskDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.report.ReportFeeDetailDto;
import com.java110.dto.report.ReportFeeDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ICommunityServiceDao
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:10
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
public interface IReportAttendanceServiceDao {



     int getMonthAttendanceCount(Map info);


     List<Map> getMonthAttendance(Map info);
}
