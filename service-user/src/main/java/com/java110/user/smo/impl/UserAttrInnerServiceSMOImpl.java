package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserAttrInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.user.dao.IUserAttrServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 用户属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserAttrInnerServiceSMOImpl extends BaseServiceSMO implements IUserAttrInnerServiceSMO {

    @Autowired
    private IUserAttrServiceDao userAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<UserAttrDto> queryUserAttrs(@RequestBody UserAttrDto userAttrDto) {

        //校验是否传了 分页信息

        int page = userAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userAttrDto.setPage((page - 1) * userAttrDto.getRow());
        }

        List<UserAttrDto> userAttrs = BeanConvertUtil.covertBeanList(userAttrServiceDaoImpl.getUserAttrInfo(BeanConvertUtil.beanCovertMap(userAttrDto)), UserAttrDto.class);

        if (userAttrs == null || userAttrs.size() == 0) {
            return userAttrs;
        }

        String[] userIds = getUserIds(userAttrs);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (UserAttrDto userAttr : userAttrs) {
            refreshUserAttr(userAttr, users);
        }
        return userAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param userAttr 小区用户属性信息
     * @param users    用户列表
     */
    private void refreshUserAttr(UserAttrDto userAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (userAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, userAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param userAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<UserAttrDto> userAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (UserAttrDto userAttr : userAttrs) {
            userIds.add(userAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryUserAttrsCount(@RequestBody UserAttrDto userAttrDto) {
        return userAttrServiceDaoImpl.queryUserAttrsCount(BeanConvertUtil.beanCovertMap(userAttrDto));
    }
    @Override
    public int deleteUserAttr(@RequestBody UserAttrDto userAttrDto) {
        userAttrDto.setStatusCd( "1" );
        return userAttrServiceDaoImpl.updateUserAttrInfoInstance(BeanConvertUtil.beanCovertMap(userAttrDto));
    }

    @Override
    public int saveUserAttr(@RequestBody UserAttrPo userAttrPo) {
        return userAttrServiceDaoImpl.saveUserAttr(BeanConvertUtil.beanCovertMap(userAttrPo));
    }
    @Override
    public int updateUserAttrInfoInstance(@RequestBody UserAttrPo userAttrPo) {
        return userAttrServiceDaoImpl.updateUserAttrInfoInstance(BeanConvertUtil.beanCovertMap(userAttrPo));
    }

    public IUserAttrServiceDao getUserAttrServiceDaoImpl() {
        return userAttrServiceDaoImpl;
    }

    public void setUserAttrServiceDaoImpl(IUserAttrServiceDao userAttrServiceDaoImpl) {
        this.userAttrServiceDaoImpl = userAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
