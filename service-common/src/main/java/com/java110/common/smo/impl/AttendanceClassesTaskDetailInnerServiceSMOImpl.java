package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceClassesTaskDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤任务明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttendanceClassesTaskDetailInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceClassesTaskDetailInnerServiceSMO {

    @Autowired
    private IAttendanceClassesTaskDetailServiceDao attendanceClassesTaskDetailServiceDaoImpl;


    @Override
    public int saveAttendanceClassesTaskDetail(@RequestBody AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {
        int saveFlag = 1;
        attendanceClassesTaskDetailServiceDaoImpl.saveAttendanceClassesTaskDetailInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailPo));
        return saveFlag;
    }

    @Override
    public int updateAttendanceClassesTaskDetail(@RequestBody AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {
        int saveFlag = 1;
        attendanceClassesTaskDetailServiceDaoImpl.updateAttendanceClassesTaskDetailInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteAttendanceClassesTaskDetail(@RequestBody AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {
        int saveFlag = 1;
        attendanceClassesTaskDetailPo.setStatusCd("1");
        attendanceClassesTaskDetailServiceDaoImpl.updateAttendanceClassesTaskDetailInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailPo));
        return saveFlag;
    }

    @Override
    public List<AttendanceClassesTaskDetailDto> queryAttendanceClassesTaskDetails(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto) {

        //校验是否传了 分页信息

        int page = attendanceClassesTaskDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceClassesTaskDetailDto.setPage((page - 1) * attendanceClassesTaskDetailDto.getRow());
        }

        List<AttendanceClassesTaskDetailDto> attendanceClassesTaskDetails = BeanConvertUtil.covertBeanList(attendanceClassesTaskDetailServiceDaoImpl.getAttendanceClassesTaskDetailInfo(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailDto)), AttendanceClassesTaskDetailDto.class);

        return attendanceClassesTaskDetails;
    }


    @Override
    public int queryAttendanceClassesTaskDetailsCount(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto) {
        return attendanceClassesTaskDetailServiceDaoImpl.queryAttendanceClassesTaskDetailsCount(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailDto));
    }

    @Override
    public List<AttendanceClassesTaskDetailDto> queryWaitSendMsgDetail(@RequestBody AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto) {
        return BeanConvertUtil.covertBeanList(attendanceClassesTaskDetailServiceDaoImpl.queryWaitSendMsgDetail(BeanConvertUtil.beanCovertMap(attendanceClassesTaskDetailDto)), AttendanceClassesTaskDetailDto.class);
    }

    public IAttendanceClassesTaskDetailServiceDao getAttendanceClassesTaskDetailServiceDaoImpl() {
        return attendanceClassesTaskDetailServiceDaoImpl;
    }

    public void setAttendanceClassesTaskDetailServiceDaoImpl(IAttendanceClassesTaskDetailServiceDao attendanceClassesTaskDetailServiceDaoImpl) {
        this.attendanceClassesTaskDetailServiceDaoImpl = attendanceClassesTaskDetailServiceDaoImpl;
    }
}
