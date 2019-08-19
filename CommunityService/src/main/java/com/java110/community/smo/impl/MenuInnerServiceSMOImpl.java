package com.java110.community.smo.impl;


import com.java110.common.util.BeanConvertUtil;
import com.java110.community.dao.IMenuServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.menu.IMenuInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.menuGroup.MenuGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 路由内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MenuInnerServiceSMOImpl extends BaseServiceSMO implements IMenuInnerServiceSMO {

    @Autowired
    private IMenuServiceDao menuServiceDaoImpl;


    @Override
    public List<MenuGroupDto> queryMenuGroups(@RequestBody  MenuGroupDto menuGroupDto) {

        //校验是否传了 分页信息

        int page = menuGroupDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            menuGroupDto.setPage((page - 1) * menuGroupDto.getRow());
            menuGroupDto.setRow(page * menuGroupDto.getRow());
        }

        List<MenuGroupDto> menuGroups = BeanConvertUtil.covertBeanList(menuServiceDaoImpl.getMenuGroupInfo(BeanConvertUtil.beanCovertMap(menuGroupDto)), MenuGroupDto.class);


        return menuGroups;
    }

    @Override
    public int updateMenuGroup(@RequestBody MenuGroupDto menuGroupDto) {
        return menuServiceDaoImpl.updateMenuGroupInfo(BeanConvertUtil.beanCovertMap(menuGroupDto));
    }

    @Override
    public int saveMenuGroup(@RequestBody MenuGroupDto menuGroupDto) {
        return menuServiceDaoImpl.saveMenuGroupInfo(BeanConvertUtil.beanCovertMap(menuGroupDto));
    }

    @Override
    public int deleteMenuGroup(@RequestBody MenuGroupDto menuGroupDto) {
        menuGroupDto.setStatusCd("1");
        return menuServiceDaoImpl.updateMenuGroupInfo(BeanConvertUtil.beanCovertMap(menuGroupDto));
    }

    @Override
    public int queryMenuGroupsCount(@RequestBody MenuGroupDto menuGroupDto) {
        return menuServiceDaoImpl.queryMenuGroupsCount(BeanConvertUtil.beanCovertMap(menuGroupDto));    }

    public IMenuServiceDao getMenuServiceDaoImpl() {
        return menuServiceDaoImpl;
    }

    public void setMenuServiceDaoImpl(IMenuServiceDao menuServiceDaoImpl) {
        this.menuServiceDaoImpl = menuServiceDaoImpl;
    }
}
