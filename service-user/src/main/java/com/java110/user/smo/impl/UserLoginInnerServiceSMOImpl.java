package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.intf.user.IUserLoginInnerServiceSMO;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.user.dao.IUserLoginServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 用户登录内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserLoginInnerServiceSMOImpl extends BaseServiceSMO implements IUserLoginInnerServiceSMO {

    @Autowired
    private IUserLoginServiceDao userLoginServiceDaoImpl;


    @Override
    public int saveUserLogin(@RequestBody UserLoginPo userLoginPo) {
        int saveFlag = 1;
        userLoginServiceDaoImpl.saveUserLoginInfo(BeanConvertUtil.beanCovertMap(userLoginPo));
        return saveFlag;
    }

    @Override
    public int updateUserLogin(@RequestBody UserLoginPo userLoginPo) {
        int saveFlag = 1;
        userLoginServiceDaoImpl.updateUserLoginInfo(BeanConvertUtil.beanCovertMap(userLoginPo));
        return saveFlag;
    }

    @Override
    public int deleteUserLogin(@RequestBody UserLoginPo userLoginPo) {
        int saveFlag = 1;
//        userLoginServiceDaoImpl.updateUserLoginInfo(BeanConvertUtil.beanCovertMap(userLoginPo));
        return saveFlag;
    }

    @Override
    public List<UserLoginDto> queryUserLogins(@RequestBody UserLoginDto userLoginDto) {

        //校验是否传了 分页信息

        int page = userLoginDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userLoginDto.setPage((page - 1) * userLoginDto.getRow());
        }

        List<UserLoginDto> userLogins = BeanConvertUtil.covertBeanList(userLoginServiceDaoImpl.getUserLoginInfo(BeanConvertUtil.beanCovertMap(userLoginDto)), UserLoginDto.class);

        return userLogins;
    }


    @Override
    public int queryUserLoginsCount(@RequestBody UserLoginDto userLoginDto) {
        return userLoginServiceDaoImpl.queryUserLoginsCount(BeanConvertUtil.beanCovertMap(userLoginDto));
    }

    public IUserLoginServiceDao getUserLoginServiceDaoImpl() {
        return userLoginServiceDaoImpl;
    }

    public void setUserLoginServiceDaoImpl(IUserLoginServiceDao userLoginServiceDaoImpl) {
        this.userLoginServiceDaoImpl = userLoginServiceDaoImpl;
    }
}
