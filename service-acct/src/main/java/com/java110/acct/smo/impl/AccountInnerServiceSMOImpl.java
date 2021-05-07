package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 账户内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountInnerServiceSMOImpl extends BaseServiceSMO implements IAccountInnerServiceSMO {

    @Autowired
    private IAccountServiceDao accountServiceDaoImpl;


    @Override
    public List<AccountDto> queryAccounts(@RequestBody AccountDto accountDto) {

        //校验是否传了 分页信息

        int page = accountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountDto.setPage((page - 1) * accountDto.getRow());
        }

        List<AccountDto> accounts = BeanConvertUtil.covertBeanList(accountServiceDaoImpl.getAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), AccountDto.class);


        return accounts;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param account 小区账户信息
     * @param users   用户列表
     */
    private void refreshAccount(AccountDto account, List<UserDto> users) {
        for (UserDto user : users) {
            if (account.getAcctId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, account);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param accounts 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AccountDto> accounts) {
        List<String> userIds = new ArrayList<String>();
        for (AccountDto account : accounts) {
            userIds.add(account.getAcctId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAccountsCount(@RequestBody AccountDto accountDto) {
        return accountServiceDaoImpl.queryAccountsCount(BeanConvertUtil.beanCovertMap(accountDto));
    }

    @Override
    @Java110Transactional
    public int updateAccount(@RequestBody AccountPo accountPo) {
        return accountServiceDaoImpl.updateAccount(BeanConvertUtil.beanCovertMap(accountPo));
    }

    public IAccountServiceDao getAccountServiceDaoImpl() {
        return accountServiceDaoImpl;
    }

    public void setAccountServiceDaoImpl(IAccountServiceDao accountServiceDaoImpl) {
        this.accountServiceDaoImpl = accountServiceDaoImpl;
    }


}
