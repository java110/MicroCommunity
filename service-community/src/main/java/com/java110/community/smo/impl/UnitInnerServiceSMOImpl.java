package com.java110.community.smo.impl;


import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IUnitServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.UnitDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 楼单元内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UnitInnerServiceSMOImpl extends BaseServiceSMO implements IUnitInnerServiceSMO {

    @Autowired
    private IUnitServiceDao unitServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<UnitDto> queryUnits(@RequestBody UnitDto unitDto) {

        //校验是否传了 分页信息

        int page = unitDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            unitDto.setPage((page - 1) * unitDto.getRow());
        }


        List<UnitDto> units = BeanConvertUtil.covertBeanList(unitServiceDaoImpl.getUnitInfo(BeanConvertUtil.beanCovertMap(unitDto)), UnitDto.class);

        if (units == null || units.size() == 0) {
            return units;
        }

        String[] userIds = getUserIds(units);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (UnitDto unit : units) {
            refreshUnit(unit, users);
        }
        return units;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param unit  小区楼单元信息
     * @param users 用户列表
     */
    private void refreshUnit(UnitDto unit, List<UserDto> users) {
        for (UserDto user : users) {
            if (unit.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, unit);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param units 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<UnitDto> units) {
        List<String> userIds = new ArrayList<String>();
        for (UnitDto unit : units) {
            userIds.add(unit.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryUnitsCount(@RequestBody UnitDto unitDto) {
        return unitServiceDaoImpl.queryUnitsCount(BeanConvertUtil.beanCovertMap(unitDto));
    }

    @Override
    public List<UnitDto> queryUnitsByCommunityId(@RequestBody UnitDto unitDto) {
        List<UnitDto> units = BeanConvertUtil.covertBeanList(unitServiceDaoImpl.queryUnitsByCommunityId(BeanConvertUtil.beanCovertMap(unitDto)), UnitDto.class);
        return units;
    }

    @Override
    public List<FloorAndUnitDto> getFloorAndUnitInfo(@RequestBody FloorAndUnitDto floorAndUnitDto) {
        List<FloorAndUnitDto> floors = BeanConvertUtil.covertBeanList(unitServiceDaoImpl.getFloorAndUnitInfo(BeanConvertUtil.beanCovertMap(floorAndUnitDto)), FloorAndUnitDto.class);
        return floors;
    }


    public IUnitServiceDao getUnitServiceDaoImpl() {
        return unitServiceDaoImpl;
    }

    public void setUnitServiceDaoImpl(IUnitServiceDao unitServiceDaoImpl) {
        this.unitServiceDaoImpl = unitServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
