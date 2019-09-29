package com.java110.community.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.BeanConvertUtil;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.PageDto;
import com.java110.dto.UserDto;
import com.java110.dto.community.CommunityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

        //校验是否传了 分页信息

        int page = communityMemberDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityMemberDto.setPage((page - 1) * communityMemberDto.getRow());
        }

        List<Map> communityMembers = communityServiceDaoImpl.getCommunityMembers(BeanConvertUtil.beanCovertMap(communityMemberDto));
        return BeanConvertUtil.covertBeanList(communityMembers, CommunityMemberDto.class);
    }

    @Override
    public int getCommunityMemberCount(@RequestBody CommunityMemberDto communityMemberDto) {
         logger.debug("getCommunityMemberCount：{}", JSONObject.toJSONString(communityMemberDto));

        return communityServiceDaoImpl.getCommunityMemberCount(BeanConvertUtil.beanCovertMap(communityMemberDto));
    }

    @Override
    public List<CommunityDto> queryCommunitys(@RequestBody  CommunityDto communityDto) {

        //校验是否传了 分页信息

        int page = communityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityDto.setPage((page - 1) * communityDto.getRow());
        }

        List<CommunityDto> communitys = BeanConvertUtil.covertBeanList(communityServiceDaoImpl.getCommunityInfoNew(BeanConvertUtil.beanCovertMap(communityDto)), CommunityDto.class);



        return communitys;
    }





    @Override
    public int queryCommunitysCount(@RequestBody CommunityDto communityDto) {
        return communityServiceDaoImpl.queryCommunitysCount(BeanConvertUtil.beanCovertMap(communityDto));
    }

    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
