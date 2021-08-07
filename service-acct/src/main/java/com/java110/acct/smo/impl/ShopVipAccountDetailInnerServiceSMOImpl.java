package com.java110.acct.smo.impl;


import com.java110.acct.dao.IShopVipAccountDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDetailDto;
import com.java110.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 会员账户交易内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ShopVipAccountDetailInnerServiceSMOImpl extends BaseServiceSMO implements IShopVipAccountDetailInnerServiceSMO {

    @Autowired
    private IShopVipAccountDetailServiceDao shopVipAccountDetailServiceDaoImpl;


    @Override
    public int saveShopVipAccountDetail(@RequestBody ShopVipAccountDetailPo shopVipAccountDetailPo) {
        int saveFlag = 1;
        shopVipAccountDetailServiceDaoImpl.saveShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(shopVipAccountDetailPo));
        return saveFlag;
    }

    @Override
    public int updateShopVipAccountDetail(@RequestBody ShopVipAccountDetailPo shopVipAccountDetailPo) {
        int saveFlag = 1;
        shopVipAccountDetailServiceDaoImpl.updateShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(shopVipAccountDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteShopVipAccountDetail(@RequestBody ShopVipAccountDetailPo shopVipAccountDetailPo) {
        int saveFlag = 1;
        shopVipAccountDetailPo.setStatusCd("1");
        shopVipAccountDetailServiceDaoImpl.updateShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(shopVipAccountDetailPo));
        return saveFlag;
    }

    @Override
    public List<ShopVipAccountDetailDto> queryShopVipAccountDetails(@RequestBody ShopVipAccountDetailDto shopVipAccountDetailDto) {

        //校验是否传了 分页信息

        int page = shopVipAccountDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            shopVipAccountDetailDto.setPage((page - 1) * shopVipAccountDetailDto.getRow());
        }

        List<ShopVipAccountDetailDto> shopVipAccountDetails = BeanConvertUtil.covertBeanList(shopVipAccountDetailServiceDaoImpl.getShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(shopVipAccountDetailDto)), ShopVipAccountDetailDto.class);

        return shopVipAccountDetails;
    }


    @Override
    public int queryShopVipAccountDetailsCount(@RequestBody ShopVipAccountDetailDto shopVipAccountDetailDto) {
        return shopVipAccountDetailServiceDaoImpl.queryShopVipAccountDetailsCount(BeanConvertUtil.beanCovertMap(shopVipAccountDetailDto));
    }

    public IShopVipAccountDetailServiceDao getShopVipAccountDetailServiceDaoImpl() {
        return shopVipAccountDetailServiceDaoImpl;
    }

    public void setShopVipAccountDetailServiceDaoImpl(IShopVipAccountDetailServiceDao shopVipAccountDetailServiceDaoImpl) {
        this.shopVipAccountDetailServiceDaoImpl = shopVipAccountDetailServiceDaoImpl;
    }
}
