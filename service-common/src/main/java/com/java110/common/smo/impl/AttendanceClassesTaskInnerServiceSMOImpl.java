package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceClassesTaskServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


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

        //输入员工名称
        refreshAttendanceClassesTasks(attendanceClassesTasks);
        return attendanceClassesTasks;
    }

    /**
     * 输入 员工 和 明细
     *
     * @param attendanceClassesTasks
     */
    private void refreshAttendanceClassesTasks(List<AttendanceClassesTaskDto> attendanceClassesTasks) {

        if (attendanceClassesTasks == null || attendanceClassesTasks.size() < 1) {
            return;
        }

        List<String> taskIds = new ArrayList<>();

        for (AttendanceClassesTaskDto attendanceClassesTaskDto : attendanceClassesTasks) {
            taskIds.add(attendanceClassesTaskDto.getTaskId());
        }

        AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto = new AttendanceClassesTaskDetailDto();
        attendanceClassesTaskDetailDto.setTaskIds(taskIds.toArray(new String[taskIds.size()]));
        attendanceClassesTaskDetailDto.setStoreId(attendanceClassesTasks.get(0).getStoreId());
        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetailDtos
                = attendanceClassesTaskDetailInnerServiceSMOImpl.queryAttendanceClassesTaskDetails(attendanceClassesTaskDetailDto);

        if (attendanceClassesTaskDetailDtos == null || attendanceClassesTaskDetailDtos.size() < 1) {
            return;
        }
        List<AttendanceClassesTaskDetailDto> tmpAttendanceClassesTaskDetailDtos = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
        for (AttendanceClassesTaskDetailDto tmpAttendanceClassesTaskDetailDto : attendanceClassesTaskDetailDtos) {
            tmpAttendanceClassesTaskDetailDto.setFacePath(imgUrl + tmpAttendanceClassesTaskDetailDto.getFacePath());
        }
        for (AttendanceClassesTaskDto attendanceClassesTaskDto : attendanceClassesTasks) {
            tmpAttendanceClassesTaskDetailDtos = new ArrayList<>();
            for (AttendanceClassesTaskDetailDto tmpAttendanceClassesTaskDetailDto : attendanceClassesTaskDetailDtos) {
                if (attendanceClassesTaskDto.getTaskId().equals(tmpAttendanceClassesTaskDetailDto.getTaskId())) {
                    tmpAttendanceClassesTaskDetailDtos.add(tmpAttendanceClassesTaskDetailDto);
                }
            }
            attendanceClassesTaskDto.setAttendanceClassesTaskDetails(tmpAttendanceClassesTaskDetailDtos);
        }
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
