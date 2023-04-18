package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.activities.ActivitiesBeautifulStaffDto;
import com.java110.intf.user.IActivitiesBeautifulStaffInnerServiceSMO;
import com.java110.po.activitiesBeautifulStaff.ActivitiesBeautifulStaffPo;
import com.java110.user.dao.IActivitiesBeautifulStaffServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 活动规则内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ActivitiesBeautifulStaffInnerServiceSMOImpl extends BaseServiceSMO implements IActivitiesBeautifulStaffInnerServiceSMO {

    @Autowired
    private IActivitiesBeautifulStaffServiceDao activitiesBeautifulStaffServiceDaoImpl;


    @Override
    public int saveActivitiesBeautifulStaff(@RequestBody ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {
        int saveFlag = 1;
        activitiesBeautifulStaffServiceDaoImpl.saveActivitiesBeautifulStaffInfo(BeanConvertUtil.beanCovertMap(activitiesBeautifulStaffPo));
        return saveFlag;
    }

    @Override
    public int updateActivitiesBeautifulStaff(@RequestBody ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {
        int saveFlag = 1;
        activitiesBeautifulStaffServiceDaoImpl.updateActivitiesBeautifulStaffInfo(BeanConvertUtil.beanCovertMap(activitiesBeautifulStaffPo));
        return saveFlag;
    }

    @Override
    public int deleteActivitiesBeautifulStaff(@RequestBody ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo) {
        int saveFlag = 1;
        activitiesBeautifulStaffPo.setStatusCd("1");
        activitiesBeautifulStaffServiceDaoImpl.updateActivitiesBeautifulStaffInfo(BeanConvertUtil.beanCovertMap(activitiesBeautifulStaffPo));
        return saveFlag;
    }

    @Override
    public List<ActivitiesBeautifulStaffDto> queryActivitiesBeautifulStaffs(@RequestBody ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto) {

        //校验是否传了 分页信息

        int page = activitiesBeautifulStaffDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            activitiesBeautifulStaffDto.setPage((page - 1) * activitiesBeautifulStaffDto.getRow());
        }

        List<ActivitiesBeautifulStaffDto> activitiesBeautifulStaffs = BeanConvertUtil.covertBeanList(activitiesBeautifulStaffServiceDaoImpl.getActivitiesBeautifulStaffInfo(BeanConvertUtil.beanCovertMap(activitiesBeautifulStaffDto)), ActivitiesBeautifulStaffDto.class);

        return activitiesBeautifulStaffs;
    }


    @Override
    public int queryActivitiesBeautifulStaffsCount(@RequestBody ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto) {
        return activitiesBeautifulStaffServiceDaoImpl.queryActivitiesBeautifulStaffsCount(BeanConvertUtil.beanCovertMap(activitiesBeautifulStaffDto));
    }

    public IActivitiesBeautifulStaffServiceDao getActivitiesBeautifulStaffServiceDaoImpl() {
        return activitiesBeautifulStaffServiceDaoImpl;
    }

    public void setActivitiesBeautifulStaffServiceDaoImpl(IActivitiesBeautifulStaffServiceDao activitiesBeautifulStaffServiceDaoImpl) {
        this.activitiesBeautifulStaffServiceDaoImpl = activitiesBeautifulStaffServiceDaoImpl;
    }
}
