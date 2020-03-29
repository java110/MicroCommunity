package com.java110.community.smo.impl;


import com.java110.community.dao.IInspectionTaskServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.inspectionTask.IInspectionTaskInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 活动内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionTaskInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionTaskInnerServiceSMO {

    @Autowired
    private IInspectionTaskServiceDao inspectionTaskServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionTaskDto> queryInspectionTasks(@RequestBody InspectionTaskDto inspectionTaskDto) {

        //校验是否传了 分页信息

        int page = inspectionTaskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionTaskDto.setPage((page - 1) * inspectionTaskDto.getRow());
        }

        List<InspectionTaskDto> inspectionTasks = BeanConvertUtil.covertBeanList(inspectionTaskServiceDaoImpl.getInspectionTaskInfo(BeanConvertUtil.beanCovertMap(inspectionTaskDto)), InspectionTaskDto.class);

        return inspectionTasks;
    }


    @Override
    public int queryInspectionTasksCount(@RequestBody InspectionTaskDto inspectionTaskDto) {
        return inspectionTaskServiceDaoImpl.queryInspectionTasksCount(BeanConvertUtil.beanCovertMap(inspectionTaskDto));
    }

    public IInspectionTaskServiceDao getInspectionTaskServiceDaoImpl() {
        return inspectionTaskServiceDaoImpl;
    }

    public void setInspectionTaskServiceDaoImpl(IInspectionTaskServiceDao inspectionTaskServiceDaoImpl) {
        this.inspectionTaskServiceDaoImpl = inspectionTaskServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
