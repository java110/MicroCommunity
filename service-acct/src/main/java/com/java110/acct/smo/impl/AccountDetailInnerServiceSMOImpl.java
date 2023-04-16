package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountDetailServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 账户交易内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountDetailInnerServiceSMOImpl extends BaseServiceSMO implements IAccountDetailInnerServiceSMO {

    @Autowired
    private IAccountDetailServiceDao accountDetailServiceDaoImpl;

    @Override
    public List<AccountDetailDto> queryAccountDetails(@RequestBody AccountDetailDto accountDetailDto) {

        //校验是否传了 分页信息

        int page = accountDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountDetailDto.setPage((page - 1) * accountDetailDto.getRow());
        }

        List<AccountDetailDto> accountDetails = BeanConvertUtil.covertBeanList(accountDetailServiceDaoImpl.getAccountDetailInfo(BeanConvertUtil.beanCovertMap(accountDetailDto)), AccountDetailDto.class);


        return accountDetails;
    }


    @Override
    public int queryAccountDetailsCount(@RequestBody AccountDetailDto accountDetailDto) {
        return accountDetailServiceDaoImpl.queryAccountDetailsCount(BeanConvertUtil.beanCovertMap(accountDetailDto));
    }

    @Override
    @Java110Transactional
    public int saveAccountDetails(@RequestBody AccountDetailPo accountDetailPo) {
        return accountDetailServiceDaoImpl.saveAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }
    @Override
    @Java110Transactional
    public int updateAccountDetails(@RequestBody AccountDetailPo accountDetailPo) {
        return accountDetailServiceDaoImpl.updateAccountDetails(BeanConvertUtil.beanCovertMap(accountDetailPo));
    }

    public IAccountDetailServiceDao getAccountDetailServiceDaoImpl() {
        return accountDetailServiceDaoImpl;
    }

    public void setAccountDetailServiceDaoImpl(IAccountDetailServiceDao accountDetailServiceDaoImpl) {
        this.accountDetailServiceDaoImpl = accountDetailServiceDaoImpl;
    }

}
