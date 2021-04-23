package com.java110.common.smo.impl;


import com.java110.common.dao.IAttendanceClassesAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendanceClasses.AttendanceClassesAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesAttrInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤班组属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttendanceClassesAttrInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceClassesAttrInnerServiceSMO {

    @Autowired
    private IAttendanceClassesAttrServiceDao attendanceClassesAttrServiceDaoImpl;


    @Override
    public List<AttendanceClassesAttrDto> queryAttendanceClassesAttrs(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto) {

        //校验是否传了 分页信息

        int page = attendanceClassesAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceClassesAttrDto.setPage((page - 1) * attendanceClassesAttrDto.getRow());
        }

        List<AttendanceClassesAttrDto> attendanceClassesAttrs = BeanConvertUtil.covertBeanList(attendanceClassesAttrServiceDaoImpl.getAttendanceClassesAttrInfo(BeanConvertUtil.beanCovertMap(attendanceClassesAttrDto)), AttendanceClassesAttrDto.class);


        return attendanceClassesAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param attendanceClassesAttr 小区考勤班组属性信息
     * @param users                 用户列表
     */
    private void refreshAttendanceClassesAttr(AttendanceClassesAttrDto attendanceClassesAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (attendanceClassesAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, attendanceClassesAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param attendanceClassesAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AttendanceClassesAttrDto> attendanceClassesAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (AttendanceClassesAttrDto attendanceClassesAttr : attendanceClassesAttrs) {
            userIds.add(attendanceClassesAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAttendanceClassesAttrsCount(@RequestBody AttendanceClassesAttrDto attendanceClassesAttrDto) {
        return attendanceClassesAttrServiceDaoImpl.queryAttendanceClassesAttrsCount(BeanConvertUtil.beanCovertMap(attendanceClassesAttrDto));
    }

    public IAttendanceClassesAttrServiceDao getAttendanceClassesAttrServiceDaoImpl() {
        return attendanceClassesAttrServiceDaoImpl;
    }

    public void setAttendanceClassesAttrServiceDaoImpl(IAttendanceClassesAttrServiceDao attendanceClassesAttrServiceDaoImpl) {
        this.attendanceClassesAttrServiceDaoImpl = attendanceClassesAttrServiceDaoImpl;
    }

}
