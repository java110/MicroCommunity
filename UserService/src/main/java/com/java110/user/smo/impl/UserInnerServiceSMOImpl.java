package com.java110.user.smo.impl;

import com.java110.dto.PageDto;
import com.java110.dto.org.OrgDto;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.UserDto;
import com.java110.user.dao.IUserServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@RestController
public class UserInnerServiceSMOImpl implements IUserInnerServiceSMO {

    @Autowired
    private IUserServiceDao userServiceDaoImpl;

    @Override
    public String getUserServiceVersion(@RequestParam("code") String code) {
        return code + " 0.0.6";
    }

    /**
     * 查询用户信息
     *
     * @param userIds 用户ID
     *                支持 多个查询
     * @return
     */
    @Override
    public List<UserDto> getUserInfo(String[] userIds) {
        Map userInfo = new HashMap();
        userInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        userInfo.put("userIds", userIds);
        return BeanConvertUtil.covertBeanList(userServiceDaoImpl.queryUsersInfo(userInfo), UserDto.class);
    }

    @Override
    public int getStaffCount(@RequestBody UserDto userDto) {

        return userServiceDaoImpl.getStaffCount(BeanConvertUtil.beanCovertMap(userDto));
    }

    @Override
    public List<UserDto> getStaffs(@RequestBody UserDto userDto) {
        //校验是否传了 分页信息

        int page = userDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userDto.setPage((page - 1) * userDto.getRow());
        }

        List<UserDto> staffs = BeanConvertUtil.covertBeanList(userServiceDaoImpl.getStaffs(BeanConvertUtil.beanCovertMap(userDto)), UserDto.class);


        return staffs;
    }


    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
