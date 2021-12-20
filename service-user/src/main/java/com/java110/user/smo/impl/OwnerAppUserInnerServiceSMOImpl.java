package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.user.dao.IOwnerAppUserServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 绑定业主内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerAppUserInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerAppUserInnerServiceSMO {

    @Autowired
    private IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO iCommunityInnerServiceSMO;

    @Override
    public List<OwnerAppUserDto> queryOwnerAppUsers(@RequestBody OwnerAppUserDto ownerAppUserDto) {

        //校验是否传了 分页信息

        int page = ownerAppUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerAppUserDto.setPage((page - 1) * ownerAppUserDto.getRow());
        }

        List<OwnerAppUserDto> ownerAppUsers = BeanConvertUtil.covertBeanList(ownerAppUserServiceDaoImpl.getOwnerAppUserInfo(BeanConvertUtil.beanCovertMap(ownerAppUserDto)), OwnerAppUserDto.class);
        for (OwnerAppUserDto ownerAppUserDto1 : ownerAppUsers) {
            if (StringUtil.isEmpty(ownerAppUserDto1.getCommunityId()) || "-1".equals(ownerAppUserDto1.getCommunityId())) {
                continue;
            }
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(ownerAppUserDto1.getCommunityId());
            List<CommunityDto> communityDtoList = iCommunityInnerServiceSMO.queryCommunitys(communityDto);
            if (communityDtoList != null && communityDtoList.size() > 0) {
                ownerAppUserDto1.setsCommunityTel(communityDtoList.get(0).getTel());
            }
        }

        return ownerAppUsers;
    }


    @Override
    public int queryOwnerAppUsersCount(@RequestBody OwnerAppUserDto ownerAppUserDto) {
        return ownerAppUserServiceDaoImpl.queryOwnerAppUsersCount(BeanConvertUtil.beanCovertMap(ownerAppUserDto));
    }

    @Override
    public int updateOwnerAppUser(@RequestBody OwnerAppUserPo ownerAppUserPo) {
        Map info = BeanConvertUtil.beanCovertMap(ownerAppUserPo);
        info.put("statusCd", "0");
        ownerAppUserServiceDaoImpl.updateOwnerAppUserInfoInstance(info);
        return 0;
    }

    public IOwnerAppUserServiceDao getOwnerAppUserServiceDaoImpl() {
        return ownerAppUserServiceDaoImpl;
    }

    public void setOwnerAppUserServiceDaoImpl(IOwnerAppUserServiceDao ownerAppUserServiceDaoImpl) {
        this.ownerAppUserServiceDaoImpl = ownerAppUserServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
