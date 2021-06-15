package com.java110.common.smo.impl;


import com.java110.common.dao.ICarInoutServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 进出场内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CarInoutInnerServiceSMOImpl extends BaseServiceSMO implements ICarInoutInnerServiceSMO {

    @Autowired
    private ICarInoutServiceDao carInoutServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CarInoutDto> queryCarInouts(@RequestBody CarInoutDto carInoutDto) {

        //校验是否传了 分页信息

        int page = carInoutDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            carInoutDto.setPage((page - 1) * carInoutDto.getRow());
        }

        List<CarInoutDto> carInouts = BeanConvertUtil.covertBeanList(carInoutServiceDaoImpl.getCarInoutInfo(BeanConvertUtil.beanCovertMap(carInoutDto)), CarInoutDto.class);


        return carInouts;
    }


    @Override
    public int queryCarInoutsCount(@RequestBody CarInoutDto carInoutDto) {
        return carInoutServiceDaoImpl.queryCarInoutsCount(BeanConvertUtil.beanCovertMap(carInoutDto));
    }

    public ICarInoutServiceDao getCarInoutServiceDaoImpl() {
        return carInoutServiceDaoImpl;
    }

    public void setCarInoutServiceDaoImpl(ICarInoutServiceDao carInoutServiceDaoImpl) {
        this.carInoutServiceDaoImpl = carInoutServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
