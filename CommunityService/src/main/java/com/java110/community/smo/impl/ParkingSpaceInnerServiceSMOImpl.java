package com.java110.community.smo.impl;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IParkingSpaceServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.ParkingSpaceDto;
import com.java110.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 停车位内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ParkingSpaceInnerServiceSMOImpl extends BaseServiceSMO implements IParkingSpaceInnerServiceSMO {

    @Autowired
    private IParkingSpaceServiceDao parkingSpaceServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ParkingSpaceDto> queryParkingSpaces(@RequestBody  ParkingSpaceDto parkingSpaceDto) {

        //校验是否传了 分页信息

        int page = parkingSpaceDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingSpaceDto.setPage((page - 1) * parkingSpaceDto.getRow());
        }

        List<ParkingSpaceDto> parkingSpaces = BeanConvertUtil.covertBeanList(parkingSpaceServiceDaoImpl.getParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpaceDto)), ParkingSpaceDto.class);

        if (parkingSpaces == null || parkingSpaces.size() == 0) {
            return parkingSpaces;
        }

        String[] userIds = getUserIds(parkingSpaces);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ParkingSpaceDto parkingSpace : parkingSpaces) {
            refreshParkingSpace(parkingSpace, users);
        }
        return parkingSpaces;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param parkingSpace 小区停车位信息
     * @param users 用户列表
     */
    private void refreshParkingSpace(ParkingSpaceDto parkingSpace, List<UserDto> users) {
        for (UserDto user : users) {
            if (parkingSpace.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, parkingSpace);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param parkingSpaces 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ParkingSpaceDto> parkingSpaces) {
        List<String> userIds = new ArrayList<String>();
        for (ParkingSpaceDto parkingSpace : parkingSpaces) {
            userIds.add(parkingSpace.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryParkingSpacesCount(@RequestBody ParkingSpaceDto parkingSpaceDto) {
        return parkingSpaceServiceDaoImpl.queryParkingSpacesCount(BeanConvertUtil.beanCovertMap(parkingSpaceDto));    }

    public IParkingSpaceServiceDao getParkingSpaceServiceDaoImpl() {
        return parkingSpaceServiceDaoImpl;
    }

    public void setParkingSpaceServiceDaoImpl(IParkingSpaceServiceDao parkingSpaceServiceDaoImpl) {
        this.parkingSpaceServiceDaoImpl = parkingSpaceServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
