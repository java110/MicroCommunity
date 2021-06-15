package com.java110.community.smo.impl;


import com.java110.community.dao.IActivitiesServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IActivitiesInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.activities.ActivitiesDto;
import com.java110.dto.user.UserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 活动内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ActivitiesInnerServiceSMOImpl extends BaseServiceSMO implements IActivitiesInnerServiceSMO {

    @Autowired
    private IActivitiesServiceDao activitiesServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ActivitiesDto> queryActivitiess(@RequestBody ActivitiesDto activitiesDto) {

        //校验是否传了 分页信息

        int page = activitiesDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            activitiesDto.setPage((page - 1) * activitiesDto.getRow());
        }

        List<ActivitiesDto> activitiess = BeanConvertUtil.covertBeanList(activitiesServiceDaoImpl.getActivitiesInfo(BeanConvertUtil.beanCovertMap(activitiesDto)), ActivitiesDto.class);

//        if (activitiess == null || activitiess.size() == 0) {
//            return activitiess;
//        }
//
//        String[] userIds = getUserIds(activitiess);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
//
//        for (ActivitiesDto activities : activitiess) {
//            refreshActivities(activities, users);
//        }
        return activitiess;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param activities 小区活动信息
     * @param users      用户列表
     */
    private void refreshActivities(ActivitiesDto activities, List<UserDto> users) {
        for (UserDto user : users) {
            if (activities.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, activities);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param activitiess 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ActivitiesDto> activitiess) {
        List<String> userIds = new ArrayList<String>();
        for (ActivitiesDto activities : activitiess) {
            userIds.add(activities.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryActivitiessCount(@RequestBody ActivitiesDto activitiesDto) {
        return activitiesServiceDaoImpl.queryActivitiessCount(BeanConvertUtil.beanCovertMap(activitiesDto));
    }

    public IActivitiesServiceDao getActivitiesServiceDaoImpl() {
        return activitiesServiceDaoImpl;
    }

    public void setActivitiesServiceDaoImpl(IActivitiesServiceDao activitiesServiceDaoImpl) {
        this.activitiesServiceDaoImpl = activitiesServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
