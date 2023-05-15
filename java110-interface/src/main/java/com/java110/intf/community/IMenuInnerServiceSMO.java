package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.basePrivilege.HasPrivilegeDto;
import com.java110.dto.menu.MenuDto;
import com.java110.dto.menu.MenuGroupDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IMenuGroupInnerServiceSMO
 * @Description 路由接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/menuApi")
public interface IMenuInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuGroupDto 对象数据
     */
    @RequestMapping(value = "/queryMenuGroups", method = RequestMethod.POST)
    List<MenuGroupDto> queryMenuGroups(@RequestBody MenuGroupDto routeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMenuGroupsCount", method = RequestMethod.POST)
    int queryMenuGroupsCount(@RequestBody MenuGroupDto routeDto);

    /**
     * <p>修改APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/updateMenuGroup", method = RequestMethod.POST)
    int updateMenuGroup(@RequestBody MenuGroupDto routeDto);


    /**
     * <p>添加APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuGroupDto 对象数据
     */
    @RequestMapping(value = "/saveMenuGroup", method = RequestMethod.POST)
    int saveMenuGroup(@RequestBody MenuGroupDto routeDto);

    /**
     * <p>删除APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuGroupDto 对象数据
     */
    @RequestMapping(value = "/deleteMenuGroup", method = RequestMethod.POST)
    int deleteMenuGroup(@RequestBody MenuGroupDto routeDto);





    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/checkUserHasResource", method = RequestMethod.POST)
    List<Map> checkUserHasResource(@RequestBody BasePrivilegeDto routeDto);


    /**
     * <p>查询小区楼信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuGroupDto 对象数据
     */
    @RequestMapping(value = "/queryBasePrivileges", method = RequestMethod.POST)
    List<BasePrivilegeDto> queryBasePrivileges(@RequestBody BasePrivilegeDto routeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBasePrivilegesCount", method = RequestMethod.POST)
    int queryBasePrivilegesCount(@RequestBody BasePrivilegeDto routeDto);

    /**
     * <p>修改APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/updateBasePrivilege", method = RequestMethod.POST)
    int updateBasePrivilege(@RequestBody BasePrivilegeDto routeDto);


    /**
     * <p>添加APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return BasePrivilegeDto 对象数据
     */
    @RequestMapping(value = "/saveBasePrivilege", method = RequestMethod.POST)
    int saveBasePrivilege(@RequestBody BasePrivilegeDto routeDto);

    /**
     * <p>删除APP信息</p>
     *
     * @param routeDto 数据对象分享
     * @return BasePrivilegeDto 对象数据
     */
    @RequestMapping(value = "/deleteBasePrivilege", method = RequestMethod.POST)
    int deleteBasePrivilege(@RequestBody BasePrivilegeDto routeDto);




    /**
     * <p>查询菜单信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuGroupDto 对象数据
     */
    @RequestMapping(value = "/queryMenus", method = RequestMethod.POST)
    List<MenuDto> queryMenus(@RequestBody MenuDto routeDto);

    /**
     * 查询<p>菜单</p>总记录数
     *
     * @param routeDto 数据对象分享
     * @return 菜单记录数
     */
    @RequestMapping(value = "/queryMenusCount", method = RequestMethod.POST)
    int queryMenusCount(@RequestBody MenuDto routeDto);

    /**
     * <p>修改菜单信息</p>
     *
     * @param routeDto 数据对象分享
     * @return ServiceDto 对象数据
     */
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    int updateMenu(@RequestBody MenuDto routeDto);


    /**
     * <p>添加菜单信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuDto 对象数据
     */
    @RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
    int saveMenu(@RequestBody MenuDto routeDto);

    /**
     * <p>删除菜单信息</p>
     *
     * @param routeDto 数据对象分享
     * @return MenuDto 对象数据
     */
    @RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
    int deleteMenu(@RequestBody MenuDto routeDto);

    @RequestMapping(value = "/hasPrivilege", method = RequestMethod.POST)
    List<HasPrivilegeDto> hasPrivilege(@RequestBody HasPrivilegeDto hasPrivilegeDto);
}
