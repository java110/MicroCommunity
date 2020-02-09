package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionRouteServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.inspectionRoute.IInspectionRouteInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionRoute.InspectionRouteDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检路线内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionRouteInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionRouteInnerServiceSMO {

    @Autowired
    private IInspectionRouteServiceDao inspectionRouteServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionRouteDto> queryInspectionRoutes(@RequestBody  InspectionRouteDto inspectionRouteDto) {

        //校验是否传了 分页信息

        int page = inspectionRouteDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionRouteDto.setPage((page - 1) * inspectionRouteDto.getRow());
        }

        List<InspectionRouteDto> inspectionRoutes = BeanConvertUtil.covertBeanList(inspectionRouteServiceDaoImpl.getInspectionRouteInfo(BeanConvertUtil.beanCovertMap(inspectionRouteDto)), InspectionRouteDto.class);

        if (inspectionRoutes == null || inspectionRoutes.size() == 0) {
            return inspectionRoutes;
        }

        return inspectionRoutes;
    }




    @Override
    public int queryInspectionRoutesCount(@RequestBody InspectionRouteDto inspectionRouteDto) {
        return inspectionRouteServiceDaoImpl.queryInspectionRoutesCount(BeanConvertUtil.beanCovertMap(inspectionRouteDto));    }

    public IInspectionRouteServiceDao getInspectionRouteServiceDaoImpl() {
        return inspectionRouteServiceDaoImpl;
    }

    public void setInspectionRouteServiceDaoImpl(IInspectionRouteServiceDao inspectionRouteServiceDaoImpl) {
        this.inspectionRouteServiceDaoImpl = inspectionRouteServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
