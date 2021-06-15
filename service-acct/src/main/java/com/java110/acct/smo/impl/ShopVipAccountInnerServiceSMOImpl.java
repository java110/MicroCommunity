package com.java110.acct.smo.impl;


import com.java110.acct.dao.IShopVipAccountServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.intf.acct.IShopVipAccountInnerServiceSMO;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 会员账户内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ShopVipAccountInnerServiceSMOImpl extends BaseServiceSMO implements IShopVipAccountInnerServiceSMO {

    @Autowired
    private IShopVipAccountServiceDao shopVipAccountServiceDaoImpl;


    @Override
    public int saveShopVipAccount(@RequestBody ShopVipAccountPo shopVipAccountPo) {
        int saveFlag = 1;
        shopVipAccountServiceDaoImpl.saveShopVipAccountInfo(BeanConvertUtil.beanCovertMap(shopVipAccountPo));
        return saveFlag;
    }

    @Override
    public int updateShopVipAccount(@RequestBody ShopVipAccountPo shopVipAccountPo) {
        int saveFlag = 1;
        shopVipAccountServiceDaoImpl.updateShopVipAccountInfo(BeanConvertUtil.beanCovertMap(shopVipAccountPo));
        return saveFlag;
    }

    @Override
    public int deleteShopVipAccount(@RequestBody ShopVipAccountPo shopVipAccountPo) {
        int saveFlag = 1;
        shopVipAccountPo.setStatusCd("1");
        shopVipAccountServiceDaoImpl.updateShopVipAccountInfo(BeanConvertUtil.beanCovertMap(shopVipAccountPo));
        return saveFlag;
    }

    @Override
    public List<ShopVipAccountDto> queryShopVipAccounts(@RequestBody ShopVipAccountDto shopVipAccountDto) {

        //校验是否传了 分页信息

        int page = shopVipAccountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            shopVipAccountDto.setPage((page - 1) * shopVipAccountDto.getRow());
        }

        List<ShopVipAccountDto> shopVipAccounts = BeanConvertUtil.covertBeanList(shopVipAccountServiceDaoImpl.getShopVipAccountInfo(BeanConvertUtil.beanCovertMap(shopVipAccountDto)), ShopVipAccountDto.class);

        return shopVipAccounts;
    }


    @Override
    public int queryShopVipAccountsCount(@RequestBody ShopVipAccountDto shopVipAccountDto) {
        return shopVipAccountServiceDaoImpl.queryShopVipAccountsCount(BeanConvertUtil.beanCovertMap(shopVipAccountDto));
    }

    public IShopVipAccountServiceDao getShopVipAccountServiceDaoImpl() {
        return shopVipAccountServiceDaoImpl;
    }

    public void setShopVipAccountServiceDaoImpl(IShopVipAccountServiceDao shopVipAccountServiceDaoImpl) {
        this.shopVipAccountServiceDaoImpl = shopVipAccountServiceDaoImpl;
    }
}
