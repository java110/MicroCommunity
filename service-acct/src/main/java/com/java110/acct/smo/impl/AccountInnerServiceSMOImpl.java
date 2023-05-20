package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountDetailServiceDao;
import com.java110.acct.dao.IAccountServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    @Autowired
    private IAccountDetailServiceDao accountDetailServiceDaoImpl;


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

    @Override
    public int saveAccount(@RequestBody AccountPo accountPo) {
        //todo 查询账户是否已经存在
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(accountPo.getObjId());
        accountDto.setPartId(accountPo.getPartId());
        accountDto.setLink(accountPo.getLink());
        int flag = accountServiceDaoImpl.queryAccountsCount(BeanConvertUtil.beanCovertMap(accountDto));
        if (flag > 0) {
            return flag;
        }
        //todo 保存
        accountServiceDaoImpl.saveAccount(BeanConvertUtil.beanCovertMap(accountPo));
        return 1;
    }

    /**
     * 预存接口
     *
     * @param accountDetailPo
     * @return
     */
    @Override
    @Java110Transactional
    public int prestoreAccount(@RequestBody AccountDetailPo accountDetailPo) {

        if (StringUtil.isEmpty(accountDetailPo.getAcctId())) {
            throw new IllegalArgumentException("账户ID为空");
        }
        //开始枷锁
        String requestId = DistributedLock.getLockUUID();
        String key = accountDetailPo.getAcctId();
        AccountDto accountDto = null;
        List<AccountDto> accounts = null;
        int flag;
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            accountDto = new AccountDto();
            accountDto.setObjId(accountDetailPo.getObjId());
            accountDto.setAcctId(accountDetailPo.getAcctId());
            accounts = BeanConvertUtil.covertBeanList(accountServiceDaoImpl.getAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), AccountDto.class);
            if (accounts == null || accounts.size() < 1) {
                throw new IllegalArgumentException("账户不存在");
            }
            //在账户增加
            double amount = Double.parseDouble(accounts.get(0).getAmount());
            BigDecimal amountBig = new BigDecimal(amount);
            amount = amountBig.add(new BigDecimal(Double.parseDouble(accountDetailPo.getAmount()))).doubleValue();
            AccountPo accountPo = new AccountPo();
            accountPo.setObjId(accountDetailPo.getObjId());
            accountPo.setAcctId(accountDetailPo.getAcctId());
            accountPo.setAmount(amount + "");
            flag = accountServiceDaoImpl.updateAccount(BeanConvertUtil.beanCovertMap(accountPo));
            if (flag < 1) {
                throw new IllegalArgumentException("更新账户失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
        accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
        if (StringUtil.isEmpty(accountDetailPo.getDetailId())) {
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        }
        if (StringUtil.isEmpty(accountDetailPo.getOrderId())) {
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        }
        if (StringUtil.isEmpty(accountDetailPo.getRelAcctId())) {
            accountDetailPo.setRelAcctId("-1");
        }

        //保存交易明细
        return accountDetailServiceDaoImpl.saveAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }

    /**
     * 扣款接口
     *
     * @param accountDetailPo
     * @return
     */
    @Override
    @Java110Transactional
    public int withholdAccount(@RequestBody AccountDetailPo accountDetailPo) {

        if (StringUtil.isEmpty(accountDetailPo.getAcctId())) {
            throw new IllegalArgumentException("账户ID为空");
        }
        //开始枷锁
        String requestId = DistributedLock.getLockUUID();
        String key = accountDetailPo.getAcctId();
        AccountDto accountDto = null;
        List<AccountDto> accounts = null;
        int flag;
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            accountDto = new AccountDto();
            accountDto.setObjId(accountDetailPo.getObjId());
            accountDto.setAcctId(accountDetailPo.getAcctId());
            accounts = BeanConvertUtil.covertBeanList(accountServiceDaoImpl.getAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), AccountDto.class);
            if (accounts == null || accounts.size() < 1) {
                throw new IllegalArgumentException("账户不存在");
            }
            //在账户增加
            double amount = Double.parseDouble(accounts.get(0).getAmount());
            BigDecimal amountBig = new BigDecimal(amount);
            amount = amountBig.subtract(new BigDecimal(Double.parseDouble(accountDetailPo.getAmount()))).doubleValue();
            if (amount < 0) {
                throw new IllegalArgumentException("余额不足");
            }
            AccountPo accountPo = new AccountPo();
            accountPo.setObjId(accountDetailPo.getObjId());
            accountPo.setAcctId(accountDetailPo.getAcctId());
            accountPo.setAmount(amount + "");
            flag = accountServiceDaoImpl.updateAccount(BeanConvertUtil.beanCovertMap(accountPo));
            if (flag < 1) {
                throw new IllegalArgumentException("更新账户失败");
            }
            accountDetailPo.setObjType(accounts.get(0).getObjType());
        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }
        accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
        if (StringUtil.isEmpty(accountDetailPo.getDetailId())) {
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        }
        if (StringUtil.isEmpty(accountDetailPo.getOrderId())) {
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        }
        if (StringUtil.isEmpty(accountDetailPo.getRelAcctId())) {
            accountDetailPo.setRelAcctId("-1");
        }
        //保存交易明细
        return accountDetailServiceDaoImpl.saveAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }

    public IAccountServiceDao getAccountServiceDaoImpl() {
        return accountServiceDaoImpl;
    }

    public void setAccountServiceDaoImpl(IAccountServiceDao accountServiceDaoImpl) {
        this.accountServiceDaoImpl = accountServiceDaoImpl;
    }


}
