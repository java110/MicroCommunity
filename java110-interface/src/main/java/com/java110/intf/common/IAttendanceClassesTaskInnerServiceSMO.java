package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceClassesTaskInnerServiceSMO
 * @Description 考勤任务接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceClassesTaskApi")
public interface IAttendanceClassesTaskInnerServiceSMO {


    @RequestMapping(value = "/saveAttendanceClassesTask", method = RequestMethod.POST)
    public int saveAttendanceClassesTask(@RequestBody AttendanceClassesTaskPo attendanceClassesTaskPo);

    @RequestMapping(value = "/updateAttendanceClassesTask", method = RequestMethod.POST)
    public int updateAttendanceClassesTask(@RequestBody  AttendanceClassesTaskPo attendanceClassesTaskPo);

    @RequestMapping(value = "/deleteAttendanceClassesTask", method = RequestMethod.POST)
    public int deleteAttendanceClassesTask(@RequestBody  AttendanceClassesTaskPo attendanceClassesTaskPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceClassesTaskDto 数据对象分享
     * @return AttendanceClassesTaskDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceClassesTasks", method = RequestMethod.POST)
    List<AttendanceClassesTaskDto> queryAttendanceClassesTasks(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceClassesTaskDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceClassesTasksCount", method = RequestMethod.POST)
    int queryAttendanceClassesTasksCount(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto);
}
