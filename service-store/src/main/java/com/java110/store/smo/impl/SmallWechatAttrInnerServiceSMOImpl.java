package com.java110.store.smo.impl;


import com.java110.store.dao.ISmallWechatAttrServiceDao;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 微信属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class SmallWechatAttrInnerServiceSMOImpl extends BaseServiceSMO implements ISmallWechatAttrInnerServiceSMO {

    @Autowired
    private ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<SmallWechatAttrDto> querySmallWechatAttrs(@RequestBody  SmallWechatAttrDto smallWechatAttrDto) {

        //校验是否传了 分页信息

        int page = smallWechatAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            smallWechatAttrDto.setPage((page - 1) * smallWechatAttrDto.getRow());
        }

        List<SmallWechatAttrDto> smallWechatAttrs = BeanConvertUtil.covertBeanList(smallWechatAttrServiceDaoImpl.getSmallWechatAttrInfo(BeanConvertUtil.beanCovertMap(smallWechatAttrDto)), SmallWechatAttrDto.class);

        if (smallWechatAttrs == null || smallWechatAttrs.size() == 0) {
            return smallWechatAttrs;
        }

//        String[] userIds = getUserIds(smallWechatAttrs);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
//
//        for (SmallWechatAttrDto smallWechatAttr : smallWechatAttrs) {
//            refreshSmallWechatAttr(smallWechatAttr, users);
//        }
        return smallWechatAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param smallWechatAttr 小区微信属性信息
     * @param users 用户列表
     */
    private void refreshSmallWechatAttr(SmallWechatAttrDto smallWechatAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (smallWechatAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, smallWechatAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param smallWechatAttrs 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<SmallWechatAttrDto> smallWechatAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (SmallWechatAttrDto smallWechatAttr : smallWechatAttrs) {
            userIds.add(smallWechatAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int querySmallWechatAttrsCount(@RequestBody SmallWechatAttrDto smallWechatAttrDto) {
        return smallWechatAttrServiceDaoImpl.querySmallWechatAttrsCount(BeanConvertUtil.beanCovertMap(smallWechatAttrDto));    }

    public ISmallWechatAttrServiceDao getSmallWechatAttrServiceDaoImpl() {
        return smallWechatAttrServiceDaoImpl;
    }

    public void setSmallWechatAttrServiceDaoImpl(ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl) {
        this.smallWechatAttrServiceDaoImpl = smallWechatAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
