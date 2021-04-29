package com.java110.store.smo.impl;


import com.java110.store.dao.IWechatMenuServiceDao;
import com.java110.intf.store.IWechatMenuInnerServiceSMO;
import com.java110.dto.wechatMenu.WechatMenuDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 公众号菜单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WechatMenuInnerServiceSMOImpl extends BaseServiceSMO implements IWechatMenuInnerServiceSMO {

    @Autowired
    private IWechatMenuServiceDao wechatMenuServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<WechatMenuDto> queryWechatMenus(@RequestBody  WechatMenuDto wechatMenuDto) {

        //校验是否传了 分页信息

        int page = wechatMenuDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            wechatMenuDto.setPage((page - 1) * wechatMenuDto.getRow());
        }

        List<WechatMenuDto> wechatMenus = BeanConvertUtil.covertBeanList(wechatMenuServiceDaoImpl.getWechatMenuInfo(BeanConvertUtil.beanCovertMap(wechatMenuDto)), WechatMenuDto.class);

        if (wechatMenus == null || wechatMenus.size() == 0) {
            return wechatMenus;
        }

        String[] userIds = getUserIds(wechatMenus);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (WechatMenuDto wechatMenu : wechatMenus) {
            refreshWechatMenu(wechatMenu, users);
        }
        return wechatMenus;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param wechatMenu 小区公众号菜单信息
     * @param users 用户列表
     */
    private void refreshWechatMenu(WechatMenuDto wechatMenu, List<UserDto> users) {
        for (UserDto user : users) {
            if (wechatMenu.getWechatMenuId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, wechatMenu);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param wechatMenus 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<WechatMenuDto> wechatMenus) {
        List<String> userIds = new ArrayList<String>();
        for (WechatMenuDto wechatMenu : wechatMenus) {
            userIds.add(wechatMenu.getWechatMenuId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryWechatMenusCount(@RequestBody WechatMenuDto wechatMenuDto) {
        return wechatMenuServiceDaoImpl.queryWechatMenusCount(BeanConvertUtil.beanCovertMap(wechatMenuDto));    }

    public IWechatMenuServiceDao getWechatMenuServiceDaoImpl() {
        return wechatMenuServiceDaoImpl;
    }

    public void setWechatMenuServiceDaoImpl(IWechatMenuServiceDao wechatMenuServiceDaoImpl) {
        this.wechatMenuServiceDaoImpl = wechatMenuServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
