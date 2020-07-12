package com.java110.common.smo.impl;


import com.java110.common.dao.IAdvertItemServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IAdvertItemInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 广告项信息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AdvertItemInnerServiceSMOImpl extends BaseServiceSMO implements IAdvertItemInnerServiceSMO {

    @Autowired
    private IAdvertItemServiceDao advertItemServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<AdvertItemDto> queryAdvertItems(@RequestBody AdvertItemDto advertItemDto) {

        //校验是否传了 分页信息

        int page = advertItemDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            advertItemDto.setPage((page - 1) * advertItemDto.getRow());
        }

        List<AdvertItemDto> advertItems = BeanConvertUtil.covertBeanList(advertItemServiceDaoImpl.getAdvertItemInfo(BeanConvertUtil.beanCovertMap(advertItemDto)), AdvertItemDto.class);

        return advertItems;
    }


    @Override
    public int queryAdvertItemsCount(@RequestBody AdvertItemDto advertItemDto) {
        return advertItemServiceDaoImpl.queryAdvertItemsCount(BeanConvertUtil.beanCovertMap(advertItemDto));
    }

    public IAdvertItemServiceDao getAdvertItemServiceDaoImpl() {
        return advertItemServiceDaoImpl;
    }

    public void setAdvertItemServiceDaoImpl(IAdvertItemServiceDao advertItemServiceDaoImpl) {
        this.advertItemServiceDaoImpl = advertItemServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
