package com.java110.user.smo.impl;

import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.po.user.UserPo;
import com.java110.user.dao.IUserServiceDao;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
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


    @Override
    public int getUserCount(@RequestBody UserDto userDto) {

        return userServiceDaoImpl.getUserCount(BeanConvertUtil.beanCovertMap(userDto));
    }

    @Override
    public List<UserDto> getUsers(@RequestBody UserDto userDto) {
        //校验是否传了 分页信息

        int page = userDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userDto.setPage((page - 1) * userDto.getRow());
        }

        List<UserDto> staffs = BeanConvertUtil.covertBeanList(userServiceDaoImpl.getUsers(BeanConvertUtil.beanCovertMap(userDto)), UserDto.class);

        freshUserAttrs(staffs);
        return staffs;
    }

    @Override
    public List<UserDto> getUserHasPwd(@RequestBody UserDto userDto) {
        //校验是否传了 分页信息
        List<UserDto> staffs = BeanConvertUtil.covertBeanList(userServiceDaoImpl.getUserHasPwd(BeanConvertUtil.beanCovertMap(userDto)), UserDto.class);

        return staffs;

    }

    @Override
    public List<UserAttrDto> getUserAttrs(@RequestBody UserAttrDto userAttrDto) {
        List<UserAttrDto> userAttrDtos = BeanConvertUtil.covertBeanList(
                userServiceDaoImpl.queryUserInfoAttrs(BeanConvertUtil.beanCovertMap(userAttrDto)), UserAttrDto.class);
        return userAttrDtos;
    }

    @Override
    public int updateUser(@RequestBody UserPo userPo) {
        userPo.setStatusCd("0");
        userServiceDaoImpl.updateUserInfoInstance(BeanConvertUtil.beanCovertMap(userPo));
        return 1;
    }

    private void freshUserAttrs(List<UserDto> userDtos) {

        Map param = null;
        for (UserDto userDto : userDtos) {
            param = new HashMap();
            param.put("userId", userDto.getUserId());
            List<UserAttrDto> userAttrDtos = BeanConvertUtil.covertBeanList(userServiceDaoImpl.queryUserInfoAttrs(param), UserAttrDto.class);
            if (userAttrDtos == null || userAttrDtos.size() == 0) {
                continue;
            }
            userDto.setUserAttrs(userAttrDtos);
            for (UserAttrDto userAttrDto : userAttrDtos) {
                //openId 单独出来处理
                if ("100201911001".equals(userAttrDto.getSpecCd())) {
                    userDto.setOpenId(userAttrDto.getValue());
                }

                if (UserAttrDto.SPEC_MALL_OPEN_ID.equals(userAttrDto.getSpecCd())){
                    userDto.setMallOpenId(userAttrDto.getValue());
                }
            }


        }


    }


    public IUserServiceDao getUserServiceDaoImpl() {
        return userServiceDaoImpl;
    }

    public void setUserServiceDaoImpl(IUserServiceDao userServiceDaoImpl) {
        this.userServiceDaoImpl = userServiceDaoImpl;
    }
}
