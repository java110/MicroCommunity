package com.java110.community.smo.impl;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IAppServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IAppInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.app.AppDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 应用内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AppInnerServiceSMOImpl extends BaseServiceSMO implements IAppInnerServiceSMO {

    @Autowired
    private IAppServiceDao appServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    
    @Override
    public List<AppDto> queryApps(@RequestBody  AppDto appDto) {

        //校验是否传了 分页信息

        int page = appDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            appDto.setPage((page - 1) * appDto.getRow());
        }

        List<AppDto> apps = BeanConvertUtil.covertBeanList(appServiceDaoImpl.getAppInfo(BeanConvertUtil.beanCovertMap(appDto)), AppDto.class);


        return apps;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param app 小区应用信息
     * @param users 用户列表
     */
    private void refreshApp(AppDto app, List<UserDto> users) {
        for (UserDto user : users) {
            if (app.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, app);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param apps 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AppDto> apps) {
        List<String> userIds = new ArrayList<String>();
        for (AppDto app : apps) {
            userIds.add(app.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAppsCount(@RequestBody AppDto appDto) {
        return appServiceDaoImpl.queryAppsCount(BeanConvertUtil.beanCovertMap(appDto));    }

    @Override
    public int updateApp(@RequestBody AppDto appDto) {
        return appServiceDaoImpl.updateAppInfo(BeanConvertUtil.beanCovertMap(appDto));
    }

    @Override
    public int saveApp(@RequestBody AppDto appDto) {
        return appServiceDaoImpl.saveAppInfo(BeanConvertUtil.beanCovertMap(appDto));
    }

    @Override
    public int deleteApp(@RequestBody AppDto appDto) {
        appDto.setStatusCd("1");
        return appServiceDaoImpl.updateAppInfo(BeanConvertUtil.beanCovertMap(appDto));
    }

    public IAppServiceDao getAppServiceDaoImpl() {
        return appServiceDaoImpl;
    }

    public void setAppServiceDaoImpl(IAppServiceDao appServiceDaoImpl) {
        this.appServiceDaoImpl = appServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
