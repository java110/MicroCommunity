package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountBondServiceDao;
import com.java110.dto.account.AccountBondDto;
import com.java110.intf.acct.IAccountBondInnerServiceSMO;
import com.java110.po.accountBond.AccountBondPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 保证金内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountBondInnerServiceSMOImpl extends BaseServiceSMO implements IAccountBondInnerServiceSMO {

    @Autowired
    private IAccountBondServiceDao accountBondServiceDaoImpl;


    @Override
    public int saveAccountBond(@RequestBody AccountBondPo accountBondPo) {
        int saveFlag = 1;
        accountBondServiceDaoImpl.saveAccountBondInfo(BeanConvertUtil.beanCovertMap(accountBondPo));
        return saveFlag;
    }

     @Override
    public int updateAccountBond(@RequestBody  AccountBondPo accountBondPo) {
        int saveFlag = 1;
         accountBondServiceDaoImpl.updateAccountBondInfo(BeanConvertUtil.beanCovertMap(accountBondPo));
        return saveFlag;
    }

     @Override
    public int deleteAccountBond(@RequestBody  AccountBondPo accountBondPo) {
        int saveFlag = 1;
        accountBondPo.setStatusCd("1");
        accountBondServiceDaoImpl.updateAccountBondInfo(BeanConvertUtil.beanCovertMap(accountBondPo));
        return saveFlag;
    }

    @Override
    public List<AccountBondDto> queryAccountBonds(@RequestBody  AccountBondDto accountBondDto) {

        //校验是否传了 分页信息

        int page = accountBondDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountBondDto.setPage((page - 1) * accountBondDto.getRow());
        }

        List<AccountBondDto> accountBonds = BeanConvertUtil.covertBeanList(accountBondServiceDaoImpl.getAccountBondInfo(BeanConvertUtil.beanCovertMap(accountBondDto)), AccountBondDto.class);

        return accountBonds;
    }


    @Override
    public int queryAccountBondsCount(@RequestBody AccountBondDto accountBondDto) {
        return accountBondServiceDaoImpl.queryAccountBondsCount(BeanConvertUtil.beanCovertMap(accountBondDto));    }

    public IAccountBondServiceDao getAccountBondServiceDaoImpl() {
        return accountBondServiceDaoImpl;
    }

    public void setAccountBondServiceDaoImpl(IAccountBondServiceDao accountBondServiceDaoImpl) {
        this.accountBondServiceDaoImpl = accountBondServiceDaoImpl;
    }
}
