package com.java110.store.smo.impl.demo;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.demo.IDemoInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.demo.DemoDto;
import com.java110.store.dao.IDemoServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description demo内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class DemoInnerServiceSMOImpl extends BaseServiceSMO implements IDemoInnerServiceSMO {

    @Autowired
    private IDemoServiceDao demoServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<DemoDto> queryDemos(@RequestBody  DemoDto demoDto) {

        //校验是否传了 分页信息

        int page = demoDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            demoDto.setPage((page - 1) * demoDto.getRow());
        }

        List<DemoDto> demos = BeanConvertUtil.covertBeanList(demoServiceDaoImpl.getDemoInfo(BeanConvertUtil.beanCovertMap(demoDto)), DemoDto.class);

        if (demos == null || demos.size() == 0) {
            return demos;
        }

        String[] userIds = getUserIds(demos);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (DemoDto demo : demos) {
            refreshDemo(demo, users);
        }
        return demos;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param demo 小区demo信息
     * @param users 用户列表
     */
    private void refreshDemo(DemoDto demo, List<UserDto> users) {
        for (UserDto user : users) {
            if (demo.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, demo);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param demos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<DemoDto> demos) {
        List<String> userIds = new ArrayList<String>();
        for (DemoDto demo : demos) {
            userIds.add(demo.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryDemosCount(@RequestBody DemoDto demoDto) {
        return demoServiceDaoImpl.queryDemosCount(BeanConvertUtil.beanCovertMap(demoDto));    }

    public IDemoServiceDao getDemoServiceDaoImpl() {
        return demoServiceDaoImpl;
    }

    public void setDemoServiceDaoImpl(IDemoServiceDao demoServiceDaoImpl) {
        this.demoServiceDaoImpl = demoServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
