package com.java110.community.smo.impl;


import com.java110.community.dao.IMenuServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.basePrivilege.HasPrivilegeDto;
import com.java110.dto.menu.MenuDto;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public List<MenuGroupDto> queryMenuGroups(@RequestBody MenuGroupDto menuGroupDto) {

        //校验是否传了 分页信息

        int page = menuGroupDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            menuGroupDto.setPage((page - 1) * menuGroupDto.getRow());
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
        return menuServiceDaoImpl.queryMenuGroupsCount(BeanConvertUtil.beanCovertMap(menuGroupDto));
    }

    public IMenuServiceDao getMenuServiceDaoImpl() {
        return menuServiceDaoImpl;
    }

    public void setMenuServiceDaoImpl(IMenuServiceDao menuServiceDaoImpl) {
        this.menuServiceDaoImpl = menuServiceDaoImpl;
    }


    @Override
    public List<BasePrivilegeDto> queryBasePrivileges(@RequestBody BasePrivilegeDto basePrivilegeDto) {

        //校验是否传了 分页信息

        int page = basePrivilegeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            basePrivilegeDto.setPage((page - 1) * basePrivilegeDto.getRow());
        }

        List<BasePrivilegeDto> basePrivileges = BeanConvertUtil.covertBeanList(menuServiceDaoImpl.getBasePrivilegeInfo(BeanConvertUtil.beanCovertMap(basePrivilegeDto)), BasePrivilegeDto.class);


        return basePrivileges;
    }

    @Override
    public int updateBasePrivilege(@RequestBody BasePrivilegeDto basePrivilegeDto) {
        return menuServiceDaoImpl.updateBasePrivilegeInfo(BeanConvertUtil.beanCovertMap(basePrivilegeDto));
    }

    @Override
    public int saveBasePrivilege(@RequestBody BasePrivilegeDto basePrivilegeDto) {
        return menuServiceDaoImpl.saveBasePrivilegeInfo(BeanConvertUtil.beanCovertMap(basePrivilegeDto));
    }

    @Override
    public int deleteBasePrivilege(@RequestBody BasePrivilegeDto basePrivilegeDto) {
        basePrivilegeDto.setStatusCd("1");
        return menuServiceDaoImpl.updateBasePrivilegeInfo(BeanConvertUtil.beanCovertMap(basePrivilegeDto));
    }

    @Override
    public int queryBasePrivilegesCount(@RequestBody BasePrivilegeDto basePrivilegeDto) {
        return menuServiceDaoImpl.queryBasePrivilegesCount(BeanConvertUtil.beanCovertMap(basePrivilegeDto));
    }

    @Override
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/checkUserHasResource", method = RequestMethod.POST)
    public List<Map> checkUserHasResource(@RequestBody BasePrivilegeDto basePrivilegeDto) {
        return menuServiceDaoImpl.checkUserHasResource(BeanConvertUtil.beanCovertMap(basePrivilegeDto));
    }


    @Override
    public List<MenuDto> queryMenus(@RequestBody MenuDto menuDto) {

        //校验是否传了 分页信息

        int page = menuDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            menuDto.setPage((page - 1) * menuDto.getRow());
        }

        List<MenuDto> menus = BeanConvertUtil.covertBeanList(menuServiceDaoImpl.getMenuInfo(BeanConvertUtil.beanCovertMap(menuDto)), MenuDto.class);


        return menus;
    }

    @Override
    public int updateMenu(@RequestBody MenuDto menuDto) {
        return menuServiceDaoImpl.updateMenuInfo(BeanConvertUtil.beanCovertMap(menuDto));
    }

    @Override
    public int saveMenu(@RequestBody MenuDto menuDto) {
        return menuServiceDaoImpl.saveMenuInfo(BeanConvertUtil.beanCovertMap(menuDto));
    }

    @Override
    public int deleteMenu(@RequestBody MenuDto menuDto) {
        menuDto.setStatusCd("1");
        return menuServiceDaoImpl.updateMenuInfo(BeanConvertUtil.beanCovertMap(menuDto));
    }

    @Override
    public List<HasPrivilegeDto> hasPrivilege(@RequestBody HasPrivilegeDto hasPrivilegeDto) {
        List<HasPrivilegeDto> privilegeDtos = BeanConvertUtil.covertBeanList(menuServiceDaoImpl.hasPrivilege(BeanConvertUtil.beanCovertMap(hasPrivilegeDto)), HasPrivilegeDto.class);
        return privilegeDtos;
    }

    @Override
    public int queryMenusCount(@RequestBody MenuDto menuDto) {
        return menuServiceDaoImpl.queryMenusCount(BeanConvertUtil.beanCovertMap(menuDto));
    }

}
