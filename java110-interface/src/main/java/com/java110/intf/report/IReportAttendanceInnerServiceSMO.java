package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportFeeMonthStatisticsInnerServiceSMO
 * @Description 费用月统计接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportAttendanceInnerServiceApi")
public interface IReportAttendanceInnerServiceSMO {



    /**
     * <p>查询月考勤数量</p>
     *
     *
     * @param attendanceClassesTaskDto 数据对象分享
     * @return FeeConfigDto 对象数据
     */
    @RequestMapping(value = "/getMonthAttendanceCount", method = RequestMethod.POST)
    int getMonthAttendanceCount(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto);

    /**
     * <p>查询月考勤数量</p>
     *
     *
     * @param attendanceClassesTaskDto 数据对象分享
     * @return FeeConfigDto 对象数据
     */
    @RequestMapping(value = "/getMonthAttendance", method = RequestMethod.POST)
    List<AttendanceClassesTaskDto> getMonthAttendance(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto);

    /**
     * <p>查询月考勤数量</p>
     *
     *
     * @param attendanceClassesTaskDto 数据对象分享
     * @return FeeConfigDto 对象数据
     */
    @RequestMapping(value = "/getMonthAttendanceDetail", method = RequestMethod.POST)
    List<AttendanceClassesTaskDetailDto> getMonthAttendanceDetail(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto);


}
