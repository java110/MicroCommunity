package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionPlanServiceDao;
import com.java110.dto.inspectionPlan.InspectionPlanDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IInspectionPlanInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检计划内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionPlanInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionPlanInnerServiceSMO {

    @Autowired
    private IInspectionPlanServiceDao inspectionPlanServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionPlanDto> queryInspectionPlans(@RequestBody  InspectionPlanDto inspectionPlanDto) {

        //校验是否传了 分页信息

        int page = inspectionPlanDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionPlanDto.setPage((page - 1) * inspectionPlanDto.getRow());
        }

        List<InspectionPlanDto> inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanServiceDaoImpl.getInspectionPlanInfo(BeanConvertUtil.beanCovertMap(inspectionPlanDto)), InspectionPlanDto.class);

        return inspectionPlans;
    }




    @Override
    public int queryInspectionPlansCount(@RequestBody InspectionPlanDto inspectionPlanDto) {
        return inspectionPlanServiceDaoImpl.queryInspectionPlansCount(BeanConvertUtil.beanCovertMap(inspectionPlanDto));    }

    public IInspectionPlanServiceDao getInspectionPlanServiceDaoImpl() {
        return inspectionPlanServiceDaoImpl;
    }

    public void setInspectionPlanServiceDaoImpl(IInspectionPlanServiceDao inspectionPlanServiceDaoImpl) {
        this.inspectionPlanServiceDaoImpl = inspectionPlanServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
