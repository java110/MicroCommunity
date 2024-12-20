package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceClassesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceClassesInnerServiceSMO
 * @Description 考勤班次接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceClassesApi")
public interface IAttendanceClassesInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceClassesDto 数据对象分享
     * @return AttendanceClassesDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceClassess", method = RequestMethod.POST)
    List<AttendanceClassesDto> queryAttendanceClassess(@RequestBody AttendanceClassesDto attendanceClassesDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceClassesDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceClassessCount", method = RequestMethod.POST)
    int queryAttendanceClassessCount(@RequestBody AttendanceClassesDto attendanceClassesDto);
}
