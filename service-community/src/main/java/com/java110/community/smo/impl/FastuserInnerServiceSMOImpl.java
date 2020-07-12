package com.java110.community.smo.impl;


import com.java110.community.dao.IFastuserServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IFastuserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fastuser.FastuserDto;
import com.java110.dto.user.UserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FastuserInnerServiceSMOImpl
 * @Description 活动内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FastuserInnerServiceSMOImpl extends BaseServiceSMO implements IFastuserInnerServiceSMO {


    @Autowired
    private IFastuserServiceDao fastuserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FastuserDto> queryFastuser(@RequestBody FastuserDto fastuserDto) {

        //校验是否传了 分页信息

        int page = fastuserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            fastuserDto.setPage((page - 1) * fastuserDto.getRow());
        }

        List<FastuserDto> fastusers = BeanConvertUtil.covertBeanList(fastuserServiceDaoImpl.getFastuserInfo(BeanConvertUtil.beanCovertMap(fastuserDto)), FastuserDto.class);

        if (fastusers == null || fastusers.size() == 0) {
            return fastusers;
        }

        String[] userIds = getUserIds(fastusers);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (FastuserDto fastuser : fastusers) {
            refreshFastuser(fastuser, users);
        }
        return fastusers;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param fastusers 小区活动信息
     * @param users      用户列表
     */
    private void refreshFastuser(FastuserDto fastusers,List<UserDto> users) {
        for (UserDto user : users) {
            if (fastusers.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, fastusers);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param fastuserDto 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<FastuserDto> fastuserDto) {
        List<String> userIds = new ArrayList<String>();
        for (FastuserDto fastuserDtos : fastuserDto) {
            userIds.add(fastuserDtos.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryFastuserCount(@RequestBody FastuserDto fastuserDto) {
        return fastuserServiceDaoImpl.queryFastuserCount(BeanConvertUtil.beanCovertMap(fastuserDto));
    }



    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public IFastuserServiceDao getFastuserServiceDaoImpl() {
        return fastuserServiceDaoImpl;
    }

    public void setFastuserServiceDaoImpl(IFastuserServiceDao fastuserServiceDaoImpl) {
        this.fastuserServiceDaoImpl = fastuserServiceDaoImpl;
    }
}
