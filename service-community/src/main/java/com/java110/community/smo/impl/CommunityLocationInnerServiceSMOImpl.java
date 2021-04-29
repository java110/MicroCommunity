package com.java110.community.smo.impl;


import com.java110.community.dao.ICommunityLocationServiceDao;
import com.java110.intf.community.ICommunityLocationInnerServiceSMO;
import com.java110.dto.community.CommunityLocationDto;
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
 * @Description 小区位置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommunityLocationInnerServiceSMOImpl extends BaseServiceSMO implements ICommunityLocationInnerServiceSMO {

    @Autowired
    private ICommunityLocationServiceDao communityLocationServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CommunityLocationDto> queryCommunityLocations(@RequestBody  CommunityLocationDto communityLocationDto) {

        //校验是否传了 分页信息

        int page = communityLocationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityLocationDto.setPage((page - 1) * communityLocationDto.getRow());
        }

        List<CommunityLocationDto> communityLocations = BeanConvertUtil.covertBeanList(communityLocationServiceDaoImpl.getCommunityLocationInfo(BeanConvertUtil.beanCovertMap(communityLocationDto)), CommunityLocationDto.class);

        if (communityLocations == null || communityLocations.size() == 0) {
            return communityLocations;
        }

        String[] userIds = getUserIds(communityLocations);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (CommunityLocationDto communityLocation : communityLocations) {
            refreshCommunityLocation(communityLocation, users);
        }
        return communityLocations;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param communityLocation 小区小区位置信息
     * @param users 用户列表
     */
    private void refreshCommunityLocation(CommunityLocationDto communityLocation, List<UserDto> users) {
        for (UserDto user : users) {
            if (communityLocation.getLocationId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, communityLocation);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param communityLocations 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<CommunityLocationDto> communityLocations) {
        List<String> userIds = new ArrayList<String>();
        for (CommunityLocationDto communityLocation : communityLocations) {
            userIds.add(communityLocation.getLocationId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommunityLocationsCount(@RequestBody CommunityLocationDto communityLocationDto) {
        return communityLocationServiceDaoImpl.queryCommunityLocationsCount(BeanConvertUtil.beanCovertMap(communityLocationDto));    }

    public ICommunityLocationServiceDao getCommunityLocationServiceDaoImpl() {
        return communityLocationServiceDaoImpl;
    }

    public void setCommunityLocationServiceDaoImpl(ICommunityLocationServiceDao communityLocationServiceDaoImpl) {
        this.communityLocationServiceDaoImpl = communityLocationServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
