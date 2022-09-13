package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceClassesServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.attendanceClasses.AttendanceClassesAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesAttrInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤班次内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttendanceClassesInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceClassesInnerServiceSMO {

    @Autowired
    private IAttendanceClassesServiceDao attendanceClassesServiceDaoImpl;

    @Autowired
    private IAttendanceClassesAttrInnerServiceSMO attendanceClassesAttrInnerServiceSMOImpl;


    @Override
    public List<AttendanceClassesDto> queryAttendanceClassess(@RequestBody AttendanceClassesDto attendanceClassesDto) {

        //校验是否传了 分页信息

        int page = attendanceClassesDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceClassesDto.setPage((page - 1) * attendanceClassesDto.getRow());
        }

        List<AttendanceClassesDto> attendanceClassess = BeanConvertUtil.covertBeanList(attendanceClassesServiceDaoImpl.getAttendanceClassesInfo(BeanConvertUtil.beanCovertMap(attendanceClassesDto)), AttendanceClassesDto.class);

        refreshAttrs(attendanceClassess);
        return attendanceClassess;
    }

    private void refreshAttrs(List<AttendanceClassesDto> attendanceClassess) {
        if (attendanceClassess == null || attendanceClassess.size() < 1 || attendanceClassess.size() > 20) {
            return;
        }

        List<String> classesIds = new ArrayList<>();
        for (AttendanceClassesDto attendanceClassesDto : attendanceClassess) {
            classesIds.add(attendanceClassesDto.getClassesId());
        }


        AttendanceClassesAttrDto attendanceClassesAttrDto = new AttendanceClassesAttrDto();
        attendanceClassesAttrDto.setClassesIds(classesIds.toArray(new String[classesIds.size()]));
        attendanceClassesAttrDto.setStoreId(attendanceClassess.get(0).getStoreId());
        List<AttendanceClassesAttrDto> attendanceClassesAttrDtos = attendanceClassesAttrInnerServiceSMOImpl.queryAttendanceClassesAttrs(attendanceClassesAttrDto);

        List<AttendanceClassesAttrDto> tmpAttendanceClassesAttrDto = null;
        for (AttendanceClassesDto attendanceClassesDto : attendanceClassess) {
            tmpAttendanceClassesAttrDto = new ArrayList<>();
            for (AttendanceClassesAttrDto attendanceClassesAttrDto1 : attendanceClassesAttrDtos) {
                if(attendanceClassesDto.getClassesId().equals(attendanceClassesAttrDto1.getClassesId())) {
                    tmpAttendanceClassesAttrDto.add(attendanceClassesAttrDto1);
                }
            }
            attendanceClassesDto.setAttrs(tmpAttendanceClassesAttrDto);
        }

    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param attendanceClasses 小区考勤班次信息
     * @param users             用户列表
     */
    private void refreshAttendanceClasses(AttendanceClassesDto attendanceClasses, List<UserDto> users) {
        for (UserDto user : users) {
            if (attendanceClasses.getClassesId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, attendanceClasses);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param attendanceClassess 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AttendanceClassesDto> attendanceClassess) {
        List<String> userIds = new ArrayList<String>();
        for (AttendanceClassesDto attendanceClasses : attendanceClassess) {
            userIds.add(attendanceClasses.getClassesId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAttendanceClassessCount(@RequestBody AttendanceClassesDto attendanceClassesDto) {
        return attendanceClassesServiceDaoImpl.queryAttendanceClassessCount(BeanConvertUtil.beanCovertMap(attendanceClassesDto));
    }

    public IAttendanceClassesServiceDao getAttendanceClassesServiceDaoImpl() {
        return attendanceClassesServiceDaoImpl;
    }

    public void setAttendanceClassesServiceDaoImpl(IAttendanceClassesServiceDao attendanceClassesServiceDaoImpl) {
        this.attendanceClassesServiceDaoImpl = attendanceClassesServiceDaoImpl;
    }

}
