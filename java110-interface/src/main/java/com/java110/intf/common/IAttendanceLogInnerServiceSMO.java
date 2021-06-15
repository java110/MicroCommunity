package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceLogDto;
import com.java110.po.attendanceLog.AttendanceLogPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceLogInnerServiceSMO
 * @Description 考勤日志接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceLogApi")
public interface IAttendanceLogInnerServiceSMO {


    @RequestMapping(value = "/saveAttendanceLog", method = RequestMethod.POST)
    public int saveAttendanceLog(@RequestBody AttendanceLogPo attendanceLogPo);

    @RequestMapping(value = "/updateAttendanceLog", method = RequestMethod.POST)
    public int updateAttendanceLog(@RequestBody  AttendanceLogPo attendanceLogPo);

    @RequestMapping(value = "/deleteAttendanceLog", method = RequestMethod.POST)
    public int deleteAttendanceLog(@RequestBody  AttendanceLogPo attendanceLogPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceLogDto 数据对象分享
     * @return AttendanceLogDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceLogs", method = RequestMethod.POST)
    List<AttendanceLogDto> queryAttendanceLogs(@RequestBody AttendanceLogDto attendanceLogDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceLogDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceLogsCount", method = RequestMethod.POST)
    int queryAttendanceLogsCount(@RequestBody AttendanceLogDto attendanceLogDto);
}
