package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionServiceDao;
import com.java110.core.smo.inspectionPoint.IInspectionInnerServiceSMO;
import com.java110.dto.inspectionPoint.InspectionDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检点内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionInnerServiceSMO {

    @Autowired
    private IInspectionServiceDao inspectionServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionDto> queryInspections(@RequestBody  InspectionDto inspectionDto) {

        //校验是否传了 分页信息

        int page = inspectionDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionDto.setPage((page - 1) * inspectionDto.getRow());
        }

        List<InspectionDto> inspections = BeanConvertUtil.covertBeanList(inspectionServiceDaoImpl.getInspectionInfo(BeanConvertUtil.beanCovertMap(inspectionDto)), InspectionDto.class);

        return inspections;
    }


    @Override
    public int queryInspectionsCount(@RequestBody InspectionDto inspectionDto) {
        return inspectionServiceDaoImpl.queryInspectionsCount(BeanConvertUtil.beanCovertMap(inspectionDto));    }

    public IInspectionServiceDao getInspectionServiceDaoImpl() {
        return inspectionServiceDaoImpl;
    }

    public void setInspectionServiceDaoImpl(IInspectionServiceDao inspectionServiceDaoImpl) {
        this.inspectionServiceDaoImpl = inspectionServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
