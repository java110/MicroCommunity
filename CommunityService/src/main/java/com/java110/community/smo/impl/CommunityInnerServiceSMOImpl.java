package com.java110.community.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.BeanConvertUtil;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区服务内部类
 */
@RestController
public class CommunityInnerServiceSMOImpl extends BaseServiceSMO implements ICommunityInnerServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(CommunityServiceSMOImpl.class);


    @Autowired
    private ICommunityServiceDao communityServiceDaoImpl;


    @Override
    public List<CommunityMemberDto> getCommunityMembers(@RequestBody CommunityMemberDto communityMemberDto) {

        logger.debug("communityMemberDto：{}", JSONObject.toJSONString(communityMemberDto));

        List<Map> communityMembers = communityServiceDaoImpl.getCommunityMembers(BeanConvertUtil.beanCovertMap(communityMemberDto));
        return BeanConvertUtil.covertBeanList(communityMembers, CommunityMemberDto.class);
    }

    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
