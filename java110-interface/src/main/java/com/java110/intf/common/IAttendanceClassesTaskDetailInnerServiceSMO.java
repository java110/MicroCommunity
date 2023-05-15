package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceClassesTaskDetailInnerServiceSMO
 * @Description 考勤任务明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceClassesTaskDetailApi")
public interface IAttendanceClassesTaskDetailInnerServiceSMO {


    @RequestMapping(value = "/saveAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public int saveAttendanceClassesTaskDetail(@RequestBody AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo);

    @RequestMapping(value = "/updateAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public int updateAttendanceClassesTaskDetail(@RequestBody  AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo);

    @RequestMapping(value = "/deleteAttendanceClassesTaskDetail", method = RequestMethod.POST)
    public int deleteAttendanceClassesTaskDetail(@RequestBody  AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param attendanceClassesTaskDetailDto 数据对象分享
     * @return AttendanceClassesTaskDetailDto 对象数据
     */
    @RequestMapping(value = "/queryAttendanceClassesTaskDetails", method = RequestMethod.POST)
    List<AttendanceClassesTaskDetailDto> queryAttendanceClassesTaskDetails(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param attendanceClassesTaskDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAttendanceClassesTaskDetailsCount", method = RequestMethod.POST)
    int queryAttendanceClassesTaskDetailsCount(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);

    @RequestMapping(value = "/queryWaitSendMsgDetail", method = RequestMethod.POST)
    List<AttendanceClassesTaskDetailDto> queryWaitSendMsgDetail(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto);
}
