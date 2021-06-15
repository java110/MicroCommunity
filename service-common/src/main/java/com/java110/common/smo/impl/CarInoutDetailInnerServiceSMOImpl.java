package com.java110.common.smo.impl;


import com.java110.common.dao.ICarInoutDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.ICarInoutDetailInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 进出场详情内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CarInoutDetailInnerServiceSMOImpl extends BaseServiceSMO implements ICarInoutDetailInnerServiceSMO {

    @Autowired
    private ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CarInoutDetailDto> queryCarInoutDetails(@RequestBody CarInoutDetailDto carInoutDetailDto) {

        //校验是否传了 分页信息

        int page = carInoutDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            carInoutDetailDto.setPage((page - 1) * carInoutDetailDto.getRow());
        }

        List<CarInoutDetailDto> carInoutDetails = BeanConvertUtil.covertBeanList(carInoutDetailServiceDaoImpl.getCarInoutDetailInfo(BeanConvertUtil.beanCovertMap(carInoutDetailDto)), CarInoutDetailDto.class);

        return carInoutDetails;
    }


    @Override
    public int queryCarInoutDetailsCount(@RequestBody CarInoutDetailDto carInoutDetailDto) {
        return carInoutDetailServiceDaoImpl.queryCarInoutDetailsCount(BeanConvertUtil.beanCovertMap(carInoutDetailDto));
    }

    public ICarInoutDetailServiceDao getCarInoutDetailServiceDaoImpl() {
        return carInoutDetailServiceDaoImpl;
    }

    public void setCarInoutDetailServiceDaoImpl(ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl) {
        this.carInoutDetailServiceDaoImpl = carInoutDetailServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
