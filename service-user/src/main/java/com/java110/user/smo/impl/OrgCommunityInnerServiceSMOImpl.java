package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.user.dao.IOrgCommunityServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 隶属小区内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OrgCommunityInnerServiceSMOImpl extends BaseServiceSMO implements IOrgCommunityInnerServiceSMO {

    @Autowired
    private IOrgCommunityServiceDao orgCommunityServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OrgCommunityDto> queryOrgCommunitys(@RequestBody OrgCommunityDto orgCommunityDto) {

        //校验是否传了 分页信息

        int page = orgCommunityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orgCommunityDto.setPage((page - 1) * orgCommunityDto.getRow());
        }

        List<OrgCommunityDto> orgCommunitys = BeanConvertUtil.covertBeanList(orgCommunityServiceDaoImpl.getOrgCommunityInfo(BeanConvertUtil.beanCovertMap(orgCommunityDto)), OrgCommunityDto.class);

        return orgCommunitys;
    }


    @Override
    public int queryOrgCommunitysCount(@RequestBody OrgCommunityDto orgCommunityDto) {
        return orgCommunityServiceDaoImpl.queryOrgCommunitysCount(BeanConvertUtil.beanCovertMap(orgCommunityDto));
    }

    public IOrgCommunityServiceDao getOrgCommunityServiceDaoImpl() {
        return orgCommunityServiceDaoImpl;
    }

    public void setOrgCommunityServiceDaoImpl(IOrgCommunityServiceDao orgCommunityServiceDaoImpl) {
        this.orgCommunityServiceDaoImpl = orgCommunityServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
