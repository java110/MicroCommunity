package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionTaskDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionTaskDetail.InspectionTaskDetailDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检任务明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionTaskDetailInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionTaskDetailInnerServiceSMO {

    @Autowired
    private IInspectionTaskDetailServiceDao inspectionTaskDetailServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionTaskDetailDto> queryInspectionTaskDetails(@RequestBody InspectionTaskDetailDto inspectionTaskDetailDto) {

        //校验是否传了 分页信息

        int page = inspectionTaskDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionTaskDetailDto.setPage((page - 1) * inspectionTaskDetailDto.getRow());
        }

        List<InspectionTaskDetailDto> inspectionTaskDetails = BeanConvertUtil.covertBeanList(inspectionTaskDetailServiceDaoImpl.getInspectionTaskDetailInfo(BeanConvertUtil.beanCovertMap(inspectionTaskDetailDto)), InspectionTaskDetailDto.class);

        return inspectionTaskDetails;
    }


    @Override
    public int queryInspectionTaskDetailsCount(@RequestBody InspectionTaskDetailDto inspectionTaskDetailDto) {
        return inspectionTaskDetailServiceDaoImpl.queryInspectionTaskDetailsCount(BeanConvertUtil.beanCovertMap(inspectionTaskDetailDto));
    }

    public IInspectionTaskDetailServiceDao getInspectionTaskDetailServiceDaoImpl() {
        return inspectionTaskDetailServiceDaoImpl;
    }

    public void setInspectionTaskDetailServiceDaoImpl(IInspectionTaskDetailServiceDao inspectionTaskDetailServiceDaoImpl) {
        this.inspectionTaskDetailServiceDaoImpl = inspectionTaskDetailServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
