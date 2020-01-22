package com.java110.community.smo.impl;


import com.java110.community.dao.IParkingAreaServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingAreaInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.user.UserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 停车场内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ParkingAreaInnerServiceSMOImpl extends BaseServiceSMO implements IParkingAreaInnerServiceSMO {

    @Autowired
    private IParkingAreaServiceDao parkingAreaServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ParkingAreaDto> queryParkingAreas(@RequestBody ParkingAreaDto parkingAreaDto) {

        //校验是否传了 分页信息

        int page = parkingAreaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingAreaDto.setPage((page - 1) * parkingAreaDto.getRow());
        }

        List<ParkingAreaDto> parkingAreas = BeanConvertUtil.covertBeanList(parkingAreaServiceDaoImpl.getParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaDto)), ParkingAreaDto.class);

        return parkingAreas;
    }




    @Override
    public int queryParkingAreasCount(@RequestBody ParkingAreaDto parkingAreaDto) {
        return parkingAreaServiceDaoImpl.queryParkingAreasCount(BeanConvertUtil.beanCovertMap(parkingAreaDto));
    }

    public IParkingAreaServiceDao getParkingAreaServiceDaoImpl() {
        return parkingAreaServiceDaoImpl;
    }

    public void setParkingAreaServiceDaoImpl(IParkingAreaServiceDao parkingAreaServiceDaoImpl) {
        this.parkingAreaServiceDaoImpl = parkingAreaServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
