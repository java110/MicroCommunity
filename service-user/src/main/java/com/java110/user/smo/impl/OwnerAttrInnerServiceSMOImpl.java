package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.user.dao.IOwnerAttrServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerAttrInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerAttrInnerServiceSMO {

    @Autowired
    private IOwnerAttrServiceDao ownerAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerAttrDto> queryOwnerAttrs(@RequestBody OwnerAttrDto ownerAttrDto) {

        //校验是否传了 分页信息

        int page = ownerAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerAttrDto.setPage((page - 1) * ownerAttrDto.getRow());
        }

        List<OwnerAttrDto> ownerAttrs = BeanConvertUtil.covertBeanList(ownerAttrServiceDaoImpl.getOwnerAttrInfo(BeanConvertUtil.beanCovertMap(ownerAttrDto)), OwnerAttrDto.class);

        if (ownerAttrs == null || ownerAttrs.size() == 0) {
            return ownerAttrs;
        }

        String[] userIds = getUserIds(ownerAttrs);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerAttrDto ownerAttr : ownerAttrs) {
            refreshOwnerAttr(ownerAttr, users);
        }
        return ownerAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerAttr 小区业主属性信息
     * @param users     用户列表
     */
    private void refreshOwnerAttr(OwnerAttrDto ownerAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerAttr);
            }
        }

        if (StringUtil.isEmpty(ownerAttr.getValueName())) {
            ownerAttr.setValueName(ownerAttr.getValue());
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerAttrDto> ownerAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerAttrDto ownerAttr : ownerAttrs) {
            userIds.add(ownerAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerAttrsCount(@RequestBody OwnerAttrDto ownerAttrDto) {
        return ownerAttrServiceDaoImpl.queryOwnerAttrsCount(BeanConvertUtil.beanCovertMap(ownerAttrDto));
    }
    @Override
    public int saveOwnerAttr(@RequestBody OwnerAttrPo ownerAttrPo) {
        return ownerAttrServiceDaoImpl.saveOwnerAttr(BeanConvertUtil.beanCovertMap(ownerAttrPo));
    }
    @Override
    public int updateOwnerAttrInfoInstance(@RequestBody OwnerAttrPo ownerAttrPo) {
        return ownerAttrServiceDaoImpl.updateOwnerAttrInfoInstance(BeanConvertUtil.beanCovertMap(ownerAttrPo));
    }

    public IOwnerAttrServiceDao getOwnerAttrServiceDaoImpl() {
        return ownerAttrServiceDaoImpl;
    }

    public void setOwnerAttrServiceDaoImpl(IOwnerAttrServiceDao ownerAttrServiceDaoImpl) {
        this.ownerAttrServiceDaoImpl = ownerAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
