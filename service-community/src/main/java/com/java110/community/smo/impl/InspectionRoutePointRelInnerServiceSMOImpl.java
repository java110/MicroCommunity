package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.community.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检路线巡检点关系内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionRoutePointRelInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionRoutePointRelInnerServiceSMO {

    @Autowired
    private IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionRoutePointRelDto> queryInspectionRoutePointRels(@RequestBody InspectionRoutePointRelDto inspectionRoutePointRelDto) {

        //校验是否传了 分页信息

        int page = inspectionRoutePointRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionRoutePointRelDto.setPage((page - 1) * inspectionRoutePointRelDto.getRow());
        }

        List<InspectionRoutePointRelDto> inspectionRoutePointRels = BeanConvertUtil.covertBeanList(inspectionRoutePointRelServiceDaoImpl.getInspectionRoutePointRelInfo(BeanConvertUtil.beanCovertMap(inspectionRoutePointRelDto)), InspectionRoutePointRelDto.class);

        return inspectionRoutePointRels;
    }


    @Override
    public int queryInspectionRoutePointRelsCount(@RequestBody InspectionRoutePointRelDto inspectionRoutePointRelDto) {
        return inspectionRoutePointRelServiceDaoImpl.queryInspectionRoutePointRelsCount(BeanConvertUtil.beanCovertMap(inspectionRoutePointRelDto));
    }

    public IInspectionRoutePointRelServiceDao getInspectionRoutePointRelServiceDaoImpl() {
        return inspectionRoutePointRelServiceDaoImpl;
    }

    public void setInspectionRoutePointRelServiceDaoImpl(IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl) {
        this.inspectionRoutePointRelServiceDaoImpl = inspectionRoutePointRelServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
