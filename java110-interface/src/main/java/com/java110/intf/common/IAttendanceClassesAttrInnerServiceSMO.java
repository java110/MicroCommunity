package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceClassesAttrDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceClassesAttrInnerServiceSMO
 * @Description 考勤班组属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceClassesAttrApi")
public interface IAttendanceClassesAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceClassesAttrDto 数据对象分享
     * @return AttendanceClassesAttrDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceClassesAttrs", method = RequestMethod.POST)
    List<AttendanceClassesAttrDto> queryAttendanceClassesAttrs(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceClassesAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceClassesAttrsCount", method = RequestMethod.POST)
    int queryAttendanceClassesAttrsCount(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto);
}
