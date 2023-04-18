package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountBankServiceDao;
import com.java110.dto.account.AccountBankDto;
import com.java110.intf.acct.IAccountBankInnerServiceSMO;
import com.java110.po.accountBank.AccountBankPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 开户行内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountBankInnerServiceSMOImpl extends BaseServiceSMO implements IAccountBankInnerServiceSMO {

    @Autowired
    private IAccountBankServiceDao accountBankServiceDaoImpl;


    @Override
    public int saveAccountBank(@RequestBody AccountBankPo accountBankPo) {
        int saveFlag = 1;
        accountBankServiceDaoImpl.saveAccountBankInfo(BeanConvertUtil.beanCovertMap(accountBankPo));
        return saveFlag;
    }

    @Override
    public int updateAccountBank(@RequestBody AccountBankPo accountBankPo) {
        int saveFlag = 1;
        accountBankServiceDaoImpl.updateAccountBankInfo(BeanConvertUtil.beanCovertMap(accountBankPo));
        return saveFlag;
    }

    @Override
    public int deleteAccountBank(@RequestBody AccountBankPo accountBankPo) {
        int saveFlag = 1;
        accountBankPo.setStatusCd("1");
        accountBankServiceDaoImpl.updateAccountBankInfo(BeanConvertUtil.beanCovertMap(accountBankPo));
        return saveFlag;
    }

    @Override
    public List<AccountBankDto> queryAccountBanks(@RequestBody AccountBankDto accountBankDto) {

        //校验是否传了 分页信息

        int page = accountBankDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountBankDto.setPage((page - 1) * accountBankDto.getRow());
        }

        List<AccountBankDto> accountBanks = BeanConvertUtil.covertBeanList(accountBankServiceDaoImpl.getAccountBankInfo(BeanConvertUtil.beanCovertMap(accountBankDto)), AccountBankDto.class);

        return accountBanks;
    }


    @Override
    public int queryAccountBanksCount(@RequestBody AccountBankDto accountBankDto) {
        return accountBankServiceDaoImpl.queryAccountBanksCount(BeanConvertUtil.beanCovertMap(accountBankDto));
    }

    public IAccountBankServiceDao getAccountBankServiceDaoImpl() {
        return accountBankServiceDaoImpl;
    }

    public void setAccountBankServiceDaoImpl(IAccountBankServiceDao accountBankServiceDaoImpl) {
        this.accountBankServiceDaoImpl = accountBankServiceDaoImpl;
    }
}
