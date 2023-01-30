package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceLogServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClasses.AttendanceLogDto;
import com.java110.intf.common.IAttendanceLogInnerServiceSMO;
import com.java110.po.attendanceLog.AttendanceLogPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤日志内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttendanceLogInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceLogInnerServiceSMO {

    @Autowired
    private IAttendanceLogServiceDao attendanceLogServiceDaoImpl;


    @Override
    public int saveAttendanceLog(@RequestBody AttendanceLogPo attendanceLogPo) {
        int saveFlag = 1;
        attendanceLogServiceDaoImpl.saveAttendanceLogInfo(BeanConvertUtil.beanCovertMap(attendanceLogPo));
        return saveFlag;
    }

    @Override
    public int updateAttendanceLog(@RequestBody AttendanceLogPo attendanceLogPo) {
        int saveFlag = 1;
        attendanceLogServiceDaoImpl.updateAttendanceLogInfo(BeanConvertUtil.beanCovertMap(attendanceLogPo));
        return saveFlag;
    }

    @Override
    public int deleteAttendanceLog(@RequestBody AttendanceLogPo attendanceLogPo) {
        int saveFlag = 1;
        attendanceLogPo.setStatusCd("1");
        attendanceLogServiceDaoImpl.updateAttendanceLogInfo(BeanConvertUtil.beanCovertMap(attendanceLogPo));
        return saveFlag;
    }

    @Override
    public List<AttendanceLogDto> queryAttendanceLogs(@RequestBody AttendanceLogDto attendanceLogDto) {

        //校验是否传了 分页信息

        int page = attendanceLogDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceLogDto.setPage((page - 1) * attendanceLogDto.getRow());
        }

        List<AttendanceLogDto> attendanceLogs = BeanConvertUtil.covertBeanList(attendanceLogServiceDaoImpl.getAttendanceLogInfo(BeanConvertUtil.beanCovertMap(attendanceLogDto)), AttendanceLogDto.class);

        if (attendanceLogs == null || attendanceLogs.size() < 1) {
            return attendanceLogs;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        for (AttendanceLogDto tmpAttendanceLogDto : attendanceLogs) {
            tmpAttendanceLogDto.setFacePath(imgUrl + tmpAttendanceLogDto.getFacePath());
        }

        return attendanceLogs;
    }


    @Override
    public int queryAttendanceLogsCount(@RequestBody AttendanceLogDto attendanceLogDto) {
        return attendanceLogServiceDaoImpl.queryAttendanceLogsCount(BeanConvertUtil.beanCovertMap(attendanceLogDto));
    }

    public IAttendanceLogServiceDao getAttendanceLogServiceDaoImpl() {
        return attendanceLogServiceDaoImpl;
    }

    public void setAttendanceLogServiceDaoImpl(IAttendanceLogServiceDao attendanceLogServiceDaoImpl) {
        this.attendanceLogServiceDaoImpl = attendanceLogServiceDaoImpl;
    }
}
