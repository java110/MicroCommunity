package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.complaint.IComplaintInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.store.dao.IComplaintServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 投诉建议内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ComplaintInnerServiceSMOImpl extends BaseServiceSMO implements IComplaintInnerServiceSMO {

    @Autowired
    private IComplaintServiceDao complaintServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ComplaintDto> queryComplaints(@RequestBody ComplaintDto complaintDto) {

        //校验是否传了 分页信息

        int page = complaintDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            complaintDto.setPage((page - 1) * complaintDto.getRow());
        }

        List<ComplaintDto> complaints = BeanConvertUtil.covertBeanList(complaintServiceDaoImpl.getComplaintInfo(BeanConvertUtil.beanCovertMap(complaintDto)), ComplaintDto.class);

        return complaints;
    }


    @Override
    public int queryComplaintsCount(@RequestBody ComplaintDto complaintDto) {
        return complaintServiceDaoImpl.queryComplaintsCount(BeanConvertUtil.beanCovertMap(complaintDto));
    }

    public IComplaintServiceDao getComplaintServiceDaoImpl() {
        return complaintServiceDaoImpl;
    }

    public void setComplaintServiceDaoImpl(IComplaintServiceDao complaintServiceDaoImpl) {
        this.complaintServiceDaoImpl = complaintServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
