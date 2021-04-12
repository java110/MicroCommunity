package com.java110.common.smo.impl;

import com.java110.common.dao.IAdvertServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IAdvertInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.advert.AdvertDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 广告信息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AdvertInnerServiceSMOImpl extends BaseServiceSMO implements IAdvertInnerServiceSMO {

    @Autowired
    private IAdvertServiceDao advertServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<AdvertDto> queryAdverts(@RequestBody AdvertDto advertDto) {
        //校验是否传了 分页信息
        int page = advertDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            advertDto.setPage((page - 1) * advertDto.getRow());
        }
        List<AdvertDto> adverts = BeanConvertUtil.covertBeanList(advertServiceDaoImpl.getAdvertInfo(BeanConvertUtil.beanCovertMap(advertDto)), AdvertDto.class);
        return adverts;
    }

    @Override
    public int queryAdvertsCount(@RequestBody AdvertDto advertDto) {
        return advertServiceDaoImpl.queryAdvertsCount(BeanConvertUtil.beanCovertMap(advertDto));
    }

    @Override
    public int updateAdverts(@RequestBody AdvertDto advertDto) {
        int saveFlag = 1;
        advertServiceDaoImpl.updateAdverts(BeanConvertUtil.beanCovertMap(advertDto));
        return saveFlag;
    }

    public IAdvertServiceDao getAdvertServiceDaoImpl() {
        return advertServiceDaoImpl;
    }

    public void setAdvertServiceDaoImpl(IAdvertServiceDao advertServiceDaoImpl) {
        this.advertServiceDaoImpl = advertServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
