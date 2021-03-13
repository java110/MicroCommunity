package com.java110.community.smo.impl;


import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IParkingSpaceServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingSpaceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ParkingSpaceDto> queryParkingSpaces(@RequestBody ParkingSpaceDto parkingSpaceDto) {

        //校验是否传了 分页信息

        int page = parkingSpaceDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingSpaceDto.setPage((page - 1) * parkingSpaceDto.getRow());
        }

        List<ParkingSpaceDto> parkingSpaces = BeanConvertUtil.covertBeanList(parkingSpaceServiceDaoImpl.getParkingSpaceInfo(BeanConvertUtil.beanCovertMap(parkingSpaceDto)), ParkingSpaceDto.class);

        return parkingSpaces;
    }

    @Override
    public int queryParkingSpacesCount(@RequestBody ParkingSpaceDto parkingSpaceDto) {
        return parkingSpaceServiceDaoImpl.queryParkingSpacesCount(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
    }

    @Override
    public int saveParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo) {
        return parkingSpaceServiceDaoImpl.saveParkingSpace(BeanConvertUtil.beanCovertMap(parkingSpacePo));
    }

    @Override
    public void updateParkingSpace(@RequestBody ParkingSpacePo parkingSpacePo) {
        parkingSpaceServiceDaoImpl.updateParkingSpaceInfoInstance(BeanConvertUtil.beanCovertMap(parkingSpacePo));
    }

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
