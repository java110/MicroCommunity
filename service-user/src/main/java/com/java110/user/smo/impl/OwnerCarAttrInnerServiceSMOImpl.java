package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IOwnerCarAttrInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerCarAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主车辆属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerCarAttrInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerCarAttrInnerServiceSMO {

    @Autowired
    private IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl;


    @Override
    public List<OwnerCarAttrDto> queryOwnerCarAttrs(@RequestBody OwnerCarAttrDto ownerCarAttrDto) {

        //校验是否传了 分页信息

        int page = ownerCarAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerCarAttrDto.setPage((page - 1) * ownerCarAttrDto.getRow());
        }

        List<OwnerCarAttrDto> ownerCarAttrs = BeanConvertUtil.covertBeanList(ownerCarAttrServiceDaoImpl.getOwnerCarAttrInfo(BeanConvertUtil.beanCovertMap(ownerCarAttrDto)), OwnerCarAttrDto.class);

        return ownerCarAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerCarAttr 小区业主车辆属性信息
     * @param users        用户列表
     */
    private void refreshOwnerCarAttr(OwnerCarAttrDto ownerCarAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerCarAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerCarAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerCarAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerCarAttrDto> ownerCarAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerCarAttrDto ownerCarAttr : ownerCarAttrs) {
            userIds.add(ownerCarAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerCarAttrsCount(@RequestBody OwnerCarAttrDto ownerCarAttrDto) {
        return ownerCarAttrServiceDaoImpl.queryOwnerCarAttrsCount(BeanConvertUtil.beanCovertMap(ownerCarAttrDto));
    }

    @Override
    public int saveOwnerCarAttr(@RequestBody OwnerCarAttrPo ownerCarAttrPo) {
        return ownerCarAttrServiceDaoImpl.saveOwnerCarAttr(BeanConvertUtil.beanCovertMap(ownerCarAttrPo));
    }

    public IOwnerCarAttrServiceDao getOwnerCarAttrServiceDaoImpl() {
        return ownerCarAttrServiceDaoImpl;
    }

    public void setOwnerCarAttrServiceDaoImpl(IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl) {
        this.ownerCarAttrServiceDaoImpl = ownerCarAttrServiceDaoImpl;
    }

}
