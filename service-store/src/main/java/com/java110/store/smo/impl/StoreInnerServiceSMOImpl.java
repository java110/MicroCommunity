package com.java110.store.smo.impl;

import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.store.StoreDto;
import com.java110.store.dao.IStoreServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName StoreInnerServiceSMOImpl 商户内部实现类
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/20 15:17
 * @Version 1.0
 * add by wuxw 2019/9/20
 **/
@RestController
public class StoreInnerServiceSMOImpl extends BaseServiceSMO implements IStoreInnerServiceSMO {

    @Autowired
    private IStoreServiceDao storeServiceDaoImpl;

    @Override
    public List<StoreDto> getStores(@RequestBody StoreDto storeDto) {
        //校验是否传了 分页信息

        int page = storeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeDto.setPage((page - 1) * storeDto.getRow());
        }

        List<StoreDto> storeDtos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStores(BeanConvertUtil.beanCovertMap(storeDto)), StoreDto.class);

        if (storeDtos == null || storeDtos.size() == 0) {
            return storeDtos;
        }

      /*  String[] userIds = getUserIds(ownerCars);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerCarDto ownerCar : ownerCars) {
            refreshOwnerCar(ownerCar, users);
        }*/
        return storeDtos;
    }

    public List<StoreAttrDto> getStoreAttrs(@RequestBody StoreAttrDto storeAttrDto) {
        List<StoreAttrDto> storeAttrDtos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStoreAttrs(BeanConvertUtil.beanCovertMap(storeAttrDto)), StoreAttrDto.class);
        return storeAttrDtos;
    }


    public int getStoreCount(@RequestBody StoreDto storeDto) {
        return storeServiceDaoImpl.getStoreCount(BeanConvertUtil.beanCovertMap(storeDto));
    }

    /**
     * 查询员工和员工所属商户信息
     *
     * @param storeUserDto
     * @return
     */
    @Override
    public List<StoreUserDto> getStoreUserInfo(@RequestBody StoreUserDto storeUserDto) {
        List<StoreUserDto> storeUserInfos = BeanConvertUtil.covertBeanList(storeServiceDaoImpl.getStoreUserInfo(BeanConvertUtil.beanCovertMap(storeUserDto)), StoreUserDto.class);
        return storeUserInfos;
    }


    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
