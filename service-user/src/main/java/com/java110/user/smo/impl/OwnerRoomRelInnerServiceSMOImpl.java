package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.user.dao.IOwnerRoomRelServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主房屋内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerRoomRelInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerRoomRelInnerServiceSMO {

    @Autowired
    private IOwnerRoomRelServiceDao ownerRoomRelServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerRoomRelDto> queryOwnerRoomRels(@RequestBody OwnerRoomRelDto ownerRoomRelDto) {

        //校验是否传了 分页信息

        int page = ownerRoomRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerRoomRelDto.setPage((page - 1) * ownerRoomRelDto.getRow());
        }

        List<OwnerRoomRelDto> ownerRoomRels = BeanConvertUtil.covertBeanList(ownerRoomRelServiceDaoImpl.getOwnerRoomRelInfo(BeanConvertUtil.beanCovertMap(ownerRoomRelDto)), OwnerRoomRelDto.class);

//        if (ownerRoomRels == null || ownerRoomRels.size() == 0) {
//            return ownerRoomRels;
//        }
//
//        String[] userIds = getUserIds(ownerRoomRels);
//        //根据 userId 查询用户信息
//        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);
//
//        for (OwnerRoomRelDto ownerRoomRel : ownerRoomRels) {
//            refreshOwnerRoomRel(ownerRoomRel, users);
//        }
        return ownerRoomRels;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerRoomRel 小区业主房屋信息
     * @param users        用户列表
     */
    private void refreshOwnerRoomRel(OwnerRoomRelDto ownerRoomRel, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerRoomRel.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerRoomRel);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerRoomRels 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerRoomRelDto> ownerRoomRels) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerRoomRelDto ownerRoomRel : ownerRoomRels) {
            userIds.add(ownerRoomRel.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerRoomRelsCount(@RequestBody OwnerRoomRelDto ownerRoomRelDto) {
        return ownerRoomRelServiceDaoImpl.queryOwnerRoomRelsCount(BeanConvertUtil.beanCovertMap(ownerRoomRelDto));
    }

    @Override
    public int saveOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo) {
        return ownerRoomRelServiceDaoImpl.saveOwnerRoomRels(BeanConvertUtil.beanCovertMap(ownerRoomRelPo));
    }

    @Override
    public int saveBusinessOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo) {
        ownerRoomRelServiceDaoImpl.saveBusinessOwnerRoomRelInfo(BeanConvertUtil.beanCovertMap(ownerRoomRelPo));
        return 1;
    }


    @Override
    public int updateOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo) {
        return ownerRoomRelServiceDaoImpl.updateOwnerRoomRels(BeanConvertUtil.beanCovertMap(ownerRoomRelPo));
    }


    public IOwnerRoomRelServiceDao getOwnerRoomRelServiceDaoImpl() {
        return ownerRoomRelServiceDaoImpl;
    }

    public void setOwnerRoomRelServiceDaoImpl(IOwnerRoomRelServiceDao ownerRoomRelServiceDaoImpl) {
        this.ownerRoomRelServiceDaoImpl = ownerRoomRelServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
