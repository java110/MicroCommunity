package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.user.dao.IOrgServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 组织内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OrgInnerServiceSMOImpl extends BaseServiceSMO implements IOrgInnerServiceSMO {

    @Autowired
    private IOrgServiceDao orgServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;


    @Override
    public List<OrgDto> queryOrgs(@RequestBody OrgDto orgDto) {

        //校验是否传了 分页信息

        int page = orgDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orgDto.setPage((page - 1) * orgDto.getRow());
        }

        List<OrgDto> orgs = BeanConvertUtil.covertBeanList(orgServiceDaoImpl.getOrgInfo(BeanConvertUtil.beanCovertMap(orgDto)), OrgDto.class);

        String[] communityIds = getCommunityIds(orgs);
        if (communityIds == null || communityIds.length < 1) {
            return orgs;
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityIds(communityIds);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        for (CommunityDto tmpCommunityDto : communityDtos) {
            for (OrgDto tmpOrgDto : orgs) {
                if (tmpCommunityDto.getCommunityId().equals(tmpOrgDto.getBelongCommunityId())) {
                    tmpOrgDto.setBelongCommunityName(tmpCommunityDto.getName());
                }
            }
        }

        return orgs;
    }


    @Override
    public int queryOrgsCount(@RequestBody OrgDto orgDto) {
        return orgServiceDaoImpl.queryOrgsCount(BeanConvertUtil.beanCovertMap(orgDto));
    }


    /**
     * <p>查询上级组织信息</p>
     *
     * @param orgDto 数据对象分享
     * @return OrgDto 对象数据
     */
    @Override
    public List<OrgDto> queryParentOrgs(@RequestBody OrgDto orgDto) {

        //校验是否传了 分页信息

        List<OrgDto> orgs = BeanConvertUtil.covertBeanList(orgServiceDaoImpl.getParentOrgInfo(BeanConvertUtil.beanCovertMap(orgDto)), OrgDto.class);

        if (orgs == null) {
            orgs = new ArrayList<>();
        }

        return orgs;
    }


    /**
     * 获取批量userId
     *
     * @param orgDtos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getCommunityIds(List<OrgDto> orgDtos) {
        List<String> communityIds = new ArrayList<String>();
        for (OrgDto orgDto : orgDtos) {
            if ("9999".equals(orgDto.getBelongCommunityId())) {
                orgDto.setBelongCommunityName("入驻所有小区");
                continue;
            }
            if (StringUtil.isEmpty(orgDto.getBelongCommunityId())) {
                orgDto.setBelongCommunityName("未知小区");
                continue;
            }
            communityIds.add(orgDto.getBelongCommunityId());
        }
        return communityIds.toArray(new String[communityIds.size()]);
    }

    public IOrgServiceDao getOrgServiceDaoImpl() {
        return orgServiceDaoImpl;
    }

    public void setOrgServiceDaoImpl(IOrgServiceDao orgServiceDaoImpl) {
        this.orgServiceDaoImpl = orgServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }
}
