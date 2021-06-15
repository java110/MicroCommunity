package com.java110.community.smo.impl;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IRouteServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IRouteInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.service.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 路由内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RouteInnerServiceSMOImpl extends BaseServiceSMO implements IRouteInnerServiceSMO {

    @Autowired
    private IRouteServiceDao routeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RouteDto> queryRoutes(@RequestBody  RouteDto routeDto) {

        //校验是否传了 分页信息

        int page = routeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            routeDto.setPage((page - 1) * routeDto.getRow());
        }

        List<RouteDto> routes = BeanConvertUtil.covertBeanList(routeServiceDaoImpl.getRouteInfo(BeanConvertUtil.beanCovertMap(routeDto)), RouteDto.class);


        return routes;
    }

    @Override
    public int updateRoute(@RequestBody RouteDto routeDto) {
        return routeServiceDaoImpl.updateRouteInfo(BeanConvertUtil.beanCovertMap(routeDto));
    }

    @Override
    public int saveRoute(@RequestBody RouteDto routeDto) {
        return routeServiceDaoImpl.saveRouteInfo(BeanConvertUtil.beanCovertMap(routeDto));
    }

    @Override
    public int deleteRoute(@RequestBody RouteDto routeDto) {
        routeDto.setStatusCd("1");
        return routeServiceDaoImpl.updateRouteInfo(BeanConvertUtil.beanCovertMap(routeDto));
    }

    @Override
    public int queryRoutesCount(@RequestBody RouteDto routeDto) {
        return routeServiceDaoImpl.queryRoutesCount(BeanConvertUtil.beanCovertMap(routeDto));    }

    public IRouteServiceDao getRouteServiceDaoImpl() {
        return routeServiceDaoImpl;
    }

    public void setRouteServiceDaoImpl(IRouteServiceDao routeServiceDaoImpl) {
        this.routeServiceDaoImpl = routeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
