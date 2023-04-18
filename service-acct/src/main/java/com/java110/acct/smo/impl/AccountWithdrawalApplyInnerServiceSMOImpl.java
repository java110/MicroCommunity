package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountWithdrawalApplyServiceDao;
import com.java110.dto.account.AccountWithdrawalApplyDto;
import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 账户提现内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountWithdrawalApplyInnerServiceSMOImpl extends BaseServiceSMO implements IAccountWithdrawalApplyInnerServiceSMO {

    @Autowired
    private IAccountWithdrawalApplyServiceDao accountWithdrawalApplyServiceDaoImpl;


    @Override
    public int saveAccountWithdrawalApply(@RequestBody AccountWithdrawalApplyPo accountWithdrawalApplyPo) {
        int saveFlag = 1;
        accountWithdrawalApplyServiceDaoImpl.saveAccountWithdrawalApplyInfo(BeanConvertUtil.beanCovertMap(accountWithdrawalApplyPo));
        return saveFlag;
    }

     @Override
    public int updateAccountWithdrawalApply(@RequestBody  AccountWithdrawalApplyPo accountWithdrawalApplyPo) {
        int saveFlag = 1;
         accountWithdrawalApplyServiceDaoImpl.updateAccountWithdrawalApplyInfo(BeanConvertUtil.beanCovertMap(accountWithdrawalApplyPo));
        return saveFlag;
    }

     @Override
    public int deleteAccountWithdrawalApply(@RequestBody  AccountWithdrawalApplyPo accountWithdrawalApplyPo) {
        int saveFlag = 1;
        accountWithdrawalApplyPo.setStatusCd("1");
        accountWithdrawalApplyServiceDaoImpl.updateAccountWithdrawalApplyInfo(BeanConvertUtil.beanCovertMap(accountWithdrawalApplyPo));
        return saveFlag;
    }

    @Override
    public List<AccountWithdrawalApplyDto> queryAccountWithdrawalApplys(@RequestBody  AccountWithdrawalApplyDto accountWithdrawalApplyDto) {

        //校验是否传了 分页信息

        int page = accountWithdrawalApplyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountWithdrawalApplyDto.setPage((page - 1) * accountWithdrawalApplyDto.getRow());
        }

        List<AccountWithdrawalApplyDto> accountWithdrawalApplys = BeanConvertUtil.covertBeanList(accountWithdrawalApplyServiceDaoImpl.getAccountWithdrawalApplyInfo(BeanConvertUtil.beanCovertMap(accountWithdrawalApplyDto)), AccountWithdrawalApplyDto.class);

        return accountWithdrawalApplys;
    }


    @Override
    public int queryAccountWithdrawalApplysCount(@RequestBody AccountWithdrawalApplyDto accountWithdrawalApplyDto) {
        return accountWithdrawalApplyServiceDaoImpl.queryAccountWithdrawalApplysCount(BeanConvertUtil.beanCovertMap(accountWithdrawalApplyDto));    }

    @Override
    public List<AccountWithdrawalApplyDto> listStateWithdrawalApplys(@RequestParam String[] states) {

        //校验是否传了 分页信息
        int page =1;
        int row = 10;

        if (page != PageDto.DEFAULT_PAGE) {
            page = ((page - 1) * row);
        }

        Map stateInfo = new HashMap();
        stateInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        stateInfo.put("states", states);
        stateInfo.put("page", page);
        stateInfo.put("row", row);

        return BeanConvertUtil.covertBeanList(accountWithdrawalApplyServiceDaoImpl.listStateWithdrawalApplys(stateInfo), AccountWithdrawalApplyDto.class);
    }


    @Override
    public int listStateWithdrawalApplysCount(@RequestParam String[] states) {
        Map stateInfo = new HashMap();
        stateInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
        stateInfo.put("states", states);
        System.out.println( states.length + "statesstatesstates"+states[0] );
        return accountWithdrawalApplyServiceDaoImpl.listStateWithdrawalApplysCount(stateInfo);    }

    public IAccountWithdrawalApplyServiceDao getAccountWithdrawalApplyServiceDaoImpl() {
        return accountWithdrawalApplyServiceDaoImpl;
    }

    public void setAccountWithdrawalApplyServiceDaoImpl(IAccountWithdrawalApplyServiceDao accountWithdrawalApplyServiceDaoImpl) {
        this.accountWithdrawalApplyServiceDaoImpl = accountWithdrawalApplyServiceDaoImpl;
    }
}
