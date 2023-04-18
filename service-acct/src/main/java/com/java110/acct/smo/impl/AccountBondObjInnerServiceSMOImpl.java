package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountBondObjServiceDao;
import com.java110.dto.account.AccountBondObjDto;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.po.accountBondObj.AccountBondObjPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 保证金对象内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountBondObjInnerServiceSMOImpl extends BaseServiceSMO implements IAccountBondObjInnerServiceSMO {

    @Autowired
    private IAccountBondObjServiceDao accountBondObjServiceDaoImpl;


    @Override
    public int saveAccountBondObj(@RequestBody AccountBondObjPo accountBondObjPo) {
        int saveFlag = 1;
        accountBondObjServiceDaoImpl.saveAccountBondObjInfo(BeanConvertUtil.beanCovertMap(accountBondObjPo));
        return saveFlag;
    }

     @Override
    public int updateAccountBondObj(@RequestBody  AccountBondObjPo accountBondObjPo) {
        int saveFlag = 1;
         accountBondObjServiceDaoImpl.updateAccountBondObjInfo(BeanConvertUtil.beanCovertMap(accountBondObjPo));
        return saveFlag;
    }

     @Override
    public int deleteAccountBondObj(@RequestBody  AccountBondObjPo accountBondObjPo) {
        int saveFlag = 1;
        accountBondObjPo.setStatusCd("1");
        accountBondObjServiceDaoImpl.updateAccountBondObjInfo(BeanConvertUtil.beanCovertMap(accountBondObjPo));
        return saveFlag;
    }

    @Override
    public List<AccountBondObjDto> queryAccountBondObjs(@RequestBody  AccountBondObjDto accountBondObjDto) {

        //校验是否传了 分页信息

        int page = accountBondObjDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountBondObjDto.setPage((page - 1) * accountBondObjDto.getRow());
        }

        List<AccountBondObjDto> accountBondObjs = BeanConvertUtil.covertBeanList(accountBondObjServiceDaoImpl.getAccountBondObjInfo(BeanConvertUtil.beanCovertMap(accountBondObjDto)), AccountBondObjDto.class);

        return accountBondObjs;
    }


    @Override
    public int queryAccountBondObjsCount(@RequestBody AccountBondObjDto accountBondObjDto) {
        return accountBondObjServiceDaoImpl.queryAccountBondObjsCount(BeanConvertUtil.beanCovertMap(accountBondObjDto));    }

    public IAccountBondObjServiceDao getAccountBondObjServiceDaoImpl() {
        return accountBondObjServiceDaoImpl;
    }

    public void setAccountBondObjServiceDaoImpl(IAccountBondObjServiceDao accountBondObjServiceDaoImpl) {
        this.accountBondObjServiceDaoImpl = accountBondObjServiceDaoImpl;
    }
}
