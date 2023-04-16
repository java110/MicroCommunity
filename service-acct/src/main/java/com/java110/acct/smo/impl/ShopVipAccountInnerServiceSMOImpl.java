package com.java110.acct.smo.impl;


import com.java110.acct.dao.IShopVipAccountDetailServiceDao;
import com.java110.acct.dao.IShopVipAccountServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.shopVipAccount.ShopVipAccountDto;
import com.java110.intf.acct.IShopVipAccountInnerServiceSMO;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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
    private IShopVipAccountDetailServiceDao shopVipAccountDetailServiceDaoImpl;
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

    /**
     * 预存接口
     *
     * @param accountDetailPo
     * @return
     */
    @Override
    @Java110Transactional
    public int prestoreAccount(@RequestBody ShopVipAccountDetailPo accountDetailPo) {

        if (StringUtil.isEmpty(accountDetailPo.getVipAcctId())) {
            throw new IllegalArgumentException("账户ID为空");
        }
        //开始枷锁
        String requestId = DistributedLock.getLockUUID();
        String key = accountDetailPo.getVipAcctId();
        ShopVipAccountDto accountDto = null;
        List<ShopVipAccountDto> accounts = null;
        int flag;
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            accountDto = new ShopVipAccountDto();
            accountDto.setShopId(accountDetailPo.getShopId());
            accountDto.setVipAcctId(accountDetailPo.getVipAcctId());
            accounts = BeanConvertUtil.covertBeanList(shopVipAccountServiceDaoImpl.getShopVipAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), ShopVipAccountDto.class);
            if (accounts == null || accounts.size() < 1) {
                throw new IllegalArgumentException("账户不存在");
            }
            //在账户增加
            double amount = Double.parseDouble(accounts.get(0).getAmount());
            BigDecimal amountBig = new BigDecimal(amount);
            amount = amountBig.add(new BigDecimal(accountDetailPo.getAmount())).doubleValue();
            ShopVipAccountPo accountPo = new ShopVipAccountPo();
            accountPo.setShopId(accountDetailPo.getShopId());
            accountPo.setVipAcctId(accountDetailPo.getVipAcctId());
            accountPo.setAmount(amount + "");
            flag = shopVipAccountServiceDaoImpl.updateShopVipAccountInfo(BeanConvertUtil.beanCovertMap(accountPo));
            if (flag < 1) {
                throw new IllegalArgumentException("更新账户失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
        accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
        if(StringUtil.isEmpty(accountDetailPo.getDetailId())) {
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        }
        if(StringUtil.isEmpty(accountDetailPo.getOrderId())){
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        }
        if(StringUtil.isEmpty(accountDetailPo.getRelAcctId())){
            accountDetailPo.setRelAcctId("-1");
        }

        //保存交易明细
        return shopVipAccountDetailServiceDaoImpl.saveShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }

    /**
     * 扣款接口
     *
     * @param accountDetailPo
     * @return
     */
    @Override
    @Java110Transactional
    public int withholdAccount(@RequestBody ShopVipAccountDetailPo accountDetailPo) {

        if (StringUtil.isEmpty(accountDetailPo.getVipAcctId())) {
            throw new IllegalArgumentException("账户ID为空");
        }
        //开始枷锁
        String requestId = DistributedLock.getLockUUID();
        String key = accountDetailPo.getVipAcctId();
        ShopVipAccountDto accountDto = null;
        List<ShopVipAccountDto> accounts = null;
        int flag;
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            accountDto = new ShopVipAccountDto();
            accountDto.setShopId(accountDetailPo.getShopId());
            accountDto.setVipAcctId(accountDetailPo.getVipAcctId());
            accounts = BeanConvertUtil.covertBeanList(shopVipAccountServiceDaoImpl.getShopVipAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), ShopVipAccountDto.class);
            if (accounts == null || accounts.size() < 1) {
                throw new IllegalArgumentException("账户不存在");
            }
            //在账户增加
            double amount = Double.parseDouble(accounts.get(0).getAmount());
            BigDecimal amountBig = new BigDecimal(amount);
            amount = amountBig.subtract(new BigDecimal(accountDetailPo.getAmount())).doubleValue();
            if(amount < 0){
                throw new IllegalArgumentException("余额不足");
            }
            ShopVipAccountPo accountPo = new ShopVipAccountPo();
            accountPo.setShopId(accountDetailPo.getShopId());
            accountPo.setVipAcctId(accountDetailPo.getVipAcctId());
            accountPo.setAmount(amount + "");
            flag = shopVipAccountServiceDaoImpl.updateShopVipAccountInfo(BeanConvertUtil.beanCovertMap(accountPo));
            if (flag < 1) {
                throw new IllegalArgumentException("更新账户失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
        accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
        if(StringUtil.isEmpty(accountDetailPo.getDetailId())) {
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        }
        if(StringUtil.isEmpty(accountDetailPo.getOrderId())){
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        }
        if(StringUtil.isEmpty(accountDetailPo.getRelAcctId())){
            accountDetailPo.setRelAcctId("-1");
        }
        //保存交易明细
        return shopVipAccountDetailServiceDaoImpl.saveShopVipAccountDetailInfo(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }
}
