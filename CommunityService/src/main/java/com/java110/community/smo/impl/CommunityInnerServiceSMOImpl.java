package com.java110.community.smo.impl;

import com.java110.common.util.BeanConvertUtil;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区服务内部类
 */
@RestController
public class CommunityInnerServiceSMOImpl extends BaseServiceSMO implements ICommunityInnerServiceSMO {

    @Autowired
    private ICommunityServiceDao communityServiceDaoImpl;


    @Override
    public List<CommunityMemberDto> getCommunityMembers(CommunityMemberDto communityMemberDto) {
        Map info = new HashMap();
        List<Map> communityMembers = communityServiceDaoImpl.getCommunityMembers(BeanConvertUtil.covertBean(communityMemberDto, info));
        return BeanConvertUtil.covertBeanList(communityMembers, CommunityMemberDto.class);
    }

    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
