package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceClassesTaskServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClassesTask.AttendanceClassesTaskDto;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤任务内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttendanceClassesTaskInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceClassesTaskInnerServiceSMO {

    @Autowired
    private IAttendanceClassesTaskServiceDao attendanceClassesTaskServiceDaoImpl;


    @Override
    public int saveAttendanceClassesTask(@RequestBody AttendanceClassesTaskPo attendanceClassesTaskPo) {
        int saveFlag = 1;
        attendanceClassesTaskServiceDaoImpl.saveAttendanceClassesTaskInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskPo));
        return saveFlag;
    }

    @Override
    public int updateAttendanceClassesTask(@RequestBody AttendanceClassesTaskPo attendanceClassesTaskPo) {
        int saveFlag = 1;
        attendanceClassesTaskServiceDaoImpl.updateAttendanceClassesTaskInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskPo));
        return saveFlag;
    }

    @Override
    public int deleteAttendanceClassesTask(@RequestBody AttendanceClassesTaskPo attendanceClassesTaskPo) {
        int saveFlag = 1;
        attendanceClassesTaskPo.setStatusCd("1");
        attendanceClassesTaskServiceDaoImpl.updateAttendanceClassesTaskInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskPo));
        return saveFlag;
    }

    @Override
    public List<AttendanceClassesTaskDto> queryAttendanceClassesTasks(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto) {

        //校验是否传了 分页信息

        int page = attendanceClassesTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceClassesTaskDto.setPage((page - 1) * attendanceClassesTaskDto.getRow());
        }

        List<AttendanceClassesTaskDto> attendanceClassesTasks = BeanConvertUtil.covertBeanList(attendanceClassesTaskServiceDaoImpl.getAttendanceClassesTaskInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDto)), AttendanceClassesTaskDto.class);

        return attendanceClassesTasks;
    }


    @Override
    public int queryAttendanceClassesTasksCount(@RequestBody AttendanceClassesTaskDto attendanceClassesTaskDto) {
        return attendanceClassesTaskServiceDaoImpl.queryAttendanceClassesTasksCount(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDto));
    }

    public IAttendanceClassesTaskServiceDao getAttendanceClassesTaskServiceDaoImpl() {
        return attendanceClassesTaskServiceDaoImpl;
    }

    public void setAttendanceClassesTaskServiceDaoImpl(IAttendanceClassesTaskServiceDao attendanceClassesTaskServiceDaoImpl) {
        this.attendanceClassesTaskServiceDaoImpl = attendanceClassesTaskServiceDaoImpl;
    }
}
