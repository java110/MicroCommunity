package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.userAddress.UserAddressDto;
import com.java110.intf.user.IUserAddressInnerServiceSMO;
import com.java110.po.userAddress.UserAddressPo;
import com.java110.user.dao.IUserAddressServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 用户联系地址内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserAddressInnerServiceSMOImpl extends BaseServiceSMO implements IUserAddressInnerServiceSMO {

    @Autowired
    private IUserAddressServiceDao userAddressServiceDaoImpl;


    @Override
    public int saveUserAddress(@RequestBody UserAddressPo userAddressPo) {
        int saveFlag = 1;
        userAddressServiceDaoImpl.saveUserAddressInfo(BeanConvertUtil.beanCovertMap(userAddressPo));
        return saveFlag;
    }

    @Override
    public int updateUserAddress(@RequestBody UserAddressPo userAddressPo) {
        int saveFlag = 1;
        userAddressServiceDaoImpl.updateUserAddressInfo(BeanConvertUtil.beanCovertMap(userAddressPo));
        return saveFlag;
    }

    @Override
    public int deleteUserAddress(@RequestBody UserAddressPo userAddressPo) {
        int saveFlag = 1;
        userAddressPo.setStatusCd("1");
        userAddressServiceDaoImpl.updateUserAddressInfo(BeanConvertUtil.beanCovertMap(userAddressPo));
        return saveFlag;
    }

    @Override
    public List<UserAddressDto> queryUserAddresss(@RequestBody UserAddressDto userAddressDto) {

        //校验是否传了 分页信息

        int page = userAddressDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userAddressDto.setPage((page - 1) * userAddressDto.getRow());
        }

        List<UserAddressDto> userAddresss = BeanConvertUtil.covertBeanList(userAddressServiceDaoImpl.getUserAddressInfo(BeanConvertUtil.beanCovertMap(userAddressDto)), UserAddressDto.class);

        return userAddresss;
    }


    @Override
    public int queryUserAddresssCount(@RequestBody UserAddressDto userAddressDto) {
        return userAddressServiceDaoImpl.queryUserAddresssCount(BeanConvertUtil.beanCovertMap(userAddressDto));
    }

    public IUserAddressServiceDao getUserAddressServiceDaoImpl() {
        return userAddressServiceDaoImpl;
    }

    public void setUserAddressServiceDaoImpl(IUserAddressServiceDao userAddressServiceDaoImpl) {
        this.userAddressServiceDaoImpl = userAddressServiceDaoImpl;
    }
}
