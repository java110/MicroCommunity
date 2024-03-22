package com.java110.user.bmo.owner.impl;

import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.user.UserPo;
import com.java110.user.bmo.owner.IGeneratorOwnerUserBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneratorOwnerUserBMOImpl implements IGeneratorOwnerUserBMO {


    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void generator(OwnerPo ownerPo) {
        String autoUser = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH, "AUTO_GENERATOR_OWNER_USER");

        if (!"ON".equals(autoUser)) {
            return;
        }

        int flag = 0;

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);

        UserDto userDto = new UserDto();
        userDto.setTel(ownerPo.getLink());
        userDto.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        String userId = "";
        if (ListUtil.isNull(userDtos)) {
            UserPo userPo = new UserPo();
            userPo.setUserId(GenerateCodeFactory.getUserId());
            userPo.setName(ownerPo.getName());
            userPo.setTel(ownerPo.getLink());
            userPo.setPassword(AuthenticationFactory.passwdMd5(ownerPo.getLink()));
            userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ORDINARY);
            userPo.setAge(ownerPo.getAge());
            userPo.setAddress(ownerPo.getAddress());
            userPo.setSex(ownerPo.getSex());
            flag = userV1InnerServiceSMOImpl.saveUser(userPo);
            if (flag < 1) {
                throw new CmdException("注册失败");
            }
            userId = userPo.getUserId();
        } else {
            userId = userDtos.get(0).getUserId();

        }

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        //状态类型，10000 审核中，12000 审核成功，13000 审核失败
        ownerAppUserPo.setState("12000");
        ownerAppUserPo.setAppTypeCd("10010");
        ownerAppUserPo.setAppUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appUserId));
        ownerAppUserPo.setMemberId(ownerPo.getMemberId());
        ownerAppUserPo.setCommunityName(tmpCommunityDto.getName());
        ownerAppUserPo.setCommunityId(ownerPo.getCommunityId());
        ownerAppUserPo.setAppUserName(ownerPo.getName());
        ownerAppUserPo.setIdCard(ownerPo.getIdCard());
        ownerAppUserPo.setAppType("WECHAT");
        ownerAppUserPo.setLink(ownerPo.getLink());
        ownerAppUserPo.setUserId(userId);
        ownerAppUserPo.setOpenId("-1");
        ownerAppUserPo.setOwnerTypeCd(ownerPo.getOwnerTypeCd());

        flag = ownerAppUserV1InnerServiceSMOImpl.saveOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("添加用户业主关系失败");
        }
    }
}
