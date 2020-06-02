package com.java110.user.smo.impl;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.user.dao.IOwnerCarServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车辆管理内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerCarInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerCarInnerServiceSMO {

    @Autowired
    private IOwnerCarServiceDao ownerCarServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerCarDto> queryOwnerCars(@RequestBody OwnerCarDto ownerCarDto) {

        //校验是否传了 分页信息

        int page = ownerCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerCarDto.setPage((page - 1) * ownerCarDto.getRow());
        }

        List<OwnerCarDto> ownerCars = BeanConvertUtil.covertBeanList(ownerCarServiceDaoImpl.getOwnerCarInfo(BeanConvertUtil.beanCovertMap(ownerCarDto)), OwnerCarDto.class);

        if (ownerCars == null || ownerCars.size() == 0) {
            return ownerCars;
        }

        String[] userIds = getUserIds(ownerCars);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerCarDto ownerCar : ownerCars) {
            refreshOwnerCar(ownerCar, users);
        }
        return ownerCars;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerCar 小区车辆管理信息
     * @param users    用户列表
     */
    private void refreshOwnerCar(OwnerCarDto ownerCar, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerCar.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerCar);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerCars 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerCarDto> ownerCars) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerCarDto ownerCar : ownerCars) {
            userIds.add(ownerCar.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerCarsCount(@RequestBody OwnerCarDto ownerCarDto) {
        return ownerCarServiceDaoImpl.queryOwnerCarsCount(BeanConvertUtil.beanCovertMap(ownerCarDto));
    }

    public IOwnerCarServiceDao getOwnerCarServiceDaoImpl() {
        return ownerCarServiceDaoImpl;
    }

    public void setOwnerCarServiceDaoImpl(IOwnerCarServiceDao ownerCarServiceDaoImpl) {
        this.ownerCarServiceDaoImpl = ownerCarServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
