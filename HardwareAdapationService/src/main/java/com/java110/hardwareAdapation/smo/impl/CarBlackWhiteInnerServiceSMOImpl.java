package com.java110.hardwareAdapation.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.hardwareAdapation.ICarBlackWhiteInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.hardwareAdapation.CarBlackWhiteDto;
import com.java110.dto.user.UserDto;
import com.java110.hardwareAdapation.dao.ICarBlackWhiteServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 黑白名单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CarBlackWhiteInnerServiceSMOImpl extends BaseServiceSMO implements ICarBlackWhiteInnerServiceSMO {

    @Autowired
    private ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CarBlackWhiteDto> queryCarBlackWhites(@RequestBody CarBlackWhiteDto carBlackWhiteDto) {

        //校验是否传了 分页信息

        int page = carBlackWhiteDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            carBlackWhiteDto.setPage((page - 1) * carBlackWhiteDto.getRow());
        }

        List<CarBlackWhiteDto> carBlackWhites = BeanConvertUtil.covertBeanList(carBlackWhiteServiceDaoImpl.getCarBlackWhiteInfo(BeanConvertUtil.beanCovertMap(carBlackWhiteDto)), CarBlackWhiteDto.class);


        return carBlackWhites;
    }


    @Override
    public int queryCarBlackWhitesCount(@RequestBody CarBlackWhiteDto carBlackWhiteDto) {
        return carBlackWhiteServiceDaoImpl.queryCarBlackWhitesCount(BeanConvertUtil.beanCovertMap(carBlackWhiteDto));
    }

    public ICarBlackWhiteServiceDao getCarBlackWhiteServiceDaoImpl() {
        return carBlackWhiteServiceDaoImpl;
    }

    public void setCarBlackWhiteServiceDaoImpl(ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl) {
        this.carBlackWhiteServiceDaoImpl = carBlackWhiteServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
