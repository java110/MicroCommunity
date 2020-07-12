package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionPlanStaffServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IInspectionPlanStaffInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 执行计划人内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionPlanStaffInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionPlanStaffInnerServiceSMO {

    @Autowired
    private IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionPlanStaffDto> queryInspectionPlanStaffs(@RequestBody InspectionPlanStaffDto inspectionPlanStaffDto) {

        //校验是否传了 分页信息

        int page = inspectionPlanStaffDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionPlanStaffDto.setPage((page - 1) * inspectionPlanStaffDto.getRow());
        }

        List<InspectionPlanStaffDto> inspectionPlanStaffs = BeanConvertUtil.covertBeanList(inspectionPlanStaffServiceDaoImpl.getInspectionPlanStaffInfo(BeanConvertUtil.beanCovertMap(inspectionPlanStaffDto)), InspectionPlanStaffDto.class);

        return inspectionPlanStaffs;
    }


    @Override
    public int queryInspectionPlanStaffsCount(@RequestBody InspectionPlanStaffDto inspectionPlanStaffDto) {
        return inspectionPlanStaffServiceDaoImpl.queryInspectionPlanStaffsCount(BeanConvertUtil.beanCovertMap(inspectionPlanStaffDto));
    }

    public IInspectionPlanStaffServiceDao getInspectionPlanStaffServiceDaoImpl() {
        return inspectionPlanStaffServiceDaoImpl;
    }

    public void setInspectionPlanStaffServiceDaoImpl(IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl) {
        this.inspectionPlanStaffServiceDaoImpl = inspectionPlanStaffServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
