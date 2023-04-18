package com.java110.community.smo.impl;


import com.java110.community.dao.ICommunityLocationAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityLocationAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 位置属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommunityLocationAttrInnerServiceSMOImpl extends BaseServiceSMO implements ICommunityLocationAttrInnerServiceSMO {

    @Autowired
    private ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl;


    @Override
    public List<CommunityLocationAttrDto> queryCommunityLocationAttrs(@RequestBody CommunityLocationAttrDto communityLocationAttrDto) {

        //校验是否传了 分页信息

        int page = communityLocationAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            communityLocationAttrDto.setPage((page - 1) * communityLocationAttrDto.getRow());
        }

        List<CommunityLocationAttrDto> communityLocationAttrs = BeanConvertUtil.covertBeanList(communityLocationAttrServiceDaoImpl.getCommunityLocationAttrInfo(BeanConvertUtil.beanCovertMap(communityLocationAttrDto)), CommunityLocationAttrDto.class);

        return communityLocationAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param communityLocationAttr 小区位置属性信息
     * @param users                 用户列表
     */
    private void refreshCommunityLocationAttr(CommunityLocationAttrDto communityLocationAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (communityLocationAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, communityLocationAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param communityLocationAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CommunityLocationAttrDto> communityLocationAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (CommunityLocationAttrDto communityLocationAttr : communityLocationAttrs) {
            userIds.add(communityLocationAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommunityLocationAttrsCount(@RequestBody CommunityLocationAttrDto communityLocationAttrDto) {
        return communityLocationAttrServiceDaoImpl.queryCommunityLocationAttrsCount(BeanConvertUtil.beanCovertMap(communityLocationAttrDto));
    }

    @Override
    public int saveCommunityLocationAttr( @RequestBody CommunityLocationAttrPo communityLocationAttrPo) {
        return communityLocationAttrServiceDaoImpl.saveCommunityLocationAttr(BeanConvertUtil.beanCovertMap(communityLocationAttrPo));
    }

    public ICommunityLocationAttrServiceDao getCommunityLocationAttrServiceDaoImpl() {
        return communityLocationAttrServiceDaoImpl;
    }

    public void setCommunityLocationAttrServiceDaoImpl(ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl) {
        this.communityLocationAttrServiceDaoImpl = communityLocationAttrServiceDaoImpl;
    }

}
