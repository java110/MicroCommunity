package com.java110.community.smo.impl;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IServiceServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IServiceInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.service.ServiceDto;
import com.java110.dto.service.ServiceProvideDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 服务内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ServiceInnerServiceSMOImpl extends BaseServiceSMO implements IServiceInnerServiceSMO {

    @Autowired
    private IServiceServiceDao serviceServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ServiceDto> queryServices(@RequestBody ServiceDto serviceDto) {

        //校验是否传了 分页信息

        int page = serviceDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            serviceDto.setPage((page - 1) * serviceDto.getRow());
        }

        List<ServiceDto> services = BeanConvertUtil.covertBeanList(serviceServiceDaoImpl.getServiceInfo(BeanConvertUtil.beanCovertMap(serviceDto)), ServiceDto.class);


        return services;
    }



    @Override
    public int queryServicesCount(@RequestBody ServiceDto serviceDto) {
        return serviceServiceDaoImpl.queryServicesCount(BeanConvertUtil.beanCovertMap(serviceDto));
    }


    @Override
    public int updateService(@RequestBody ServiceDto serviceDto) {
        return serviceServiceDaoImpl.updateServiceInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    @Override
    public int saveService(@RequestBody ServiceDto serviceDto) {
        return serviceServiceDaoImpl.saveServiceInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    @Override
    public int deleteService(@RequestBody ServiceDto serviceDto) {
        serviceDto.setStatusCd("1");
        return serviceServiceDaoImpl.updateServiceInfo(BeanConvertUtil.beanCovertMap(serviceDto));
    }

    public IServiceServiceDao getServiceServiceDaoImpl() {
        return serviceServiceDaoImpl;
    }

    public void setServiceServiceDaoImpl(IServiceServiceDao serviceServiceDaoImpl) {
        this.serviceServiceDaoImpl = serviceServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }


    @Override
    public List<ServiceProvideDto> queryServiceProvides(@RequestBody ServiceProvideDto serviceProvideDto) {

        //校验是否传了 分页信息

        int page = serviceProvideDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            serviceProvideDto.setPage((page - 1) * serviceProvideDto.getRow());
        }

        List<ServiceProvideDto> services = BeanConvertUtil.covertBeanList(serviceServiceDaoImpl.getServiceProvideInfo(BeanConvertUtil.beanCovertMap(serviceProvideDto)), ServiceProvideDto.class);


        return services;
    }



    @Override
    public int queryServiceProvidesCount(@RequestBody ServiceProvideDto serviceProvideDto) {
        return serviceServiceDaoImpl.queryServiceProvidesCount(BeanConvertUtil.beanCovertMap(serviceProvideDto));
    }


    @Override
    public int updateServiceProvide(@RequestBody ServiceProvideDto serviceProvideDto) {
        return serviceServiceDaoImpl.updateServiceProvideInfo(BeanConvertUtil.beanCovertMap(serviceProvideDto));
    }

    @Override
    public int saveServiceProvide(@RequestBody ServiceProvideDto serviceProvideDto) {
        return serviceServiceDaoImpl.saveServiceProvideInfo(BeanConvertUtil.beanCovertMap(serviceProvideDto));
    }

    @Override
    public int deleteServiceProvide(@RequestBody ServiceProvideDto serviceProvideDto) {
        serviceProvideDto.setStatusCd("1");
        return serviceServiceDaoImpl.updateServiceProvideInfo(BeanConvertUtil.beanCovertMap(serviceProvideDto));
    }
}
