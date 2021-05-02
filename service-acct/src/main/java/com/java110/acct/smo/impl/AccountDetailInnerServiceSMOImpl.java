package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountDetailServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param accountDetail 小区账户交易信息
     * @param users         用户列表
     */
    private void refreshAccountDetail(AccountDetailDto accountDetail, List<UserDto> users) {
        for (UserDto user : users) {
            if (accountDetail.getDetailId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, accountDetail);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param accountDetails 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AccountDetailDto> accountDetails) {
        List<String> userIds = new ArrayList<String>();
        for (AccountDetailDto accountDetail : accountDetails) {
            userIds.add(accountDetail.getDetailId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAccountDetailsCount(@RequestBody AccountDetailDto accountDetailDto) {
        return accountDetailServiceDaoImpl.queryAccountDetailsCount(BeanConvertUtil.beanCovertMap(accountDetailDto));
    }

    public IAccountDetailServiceDao getAccountDetailServiceDaoImpl() {
        return accountDetailServiceDaoImpl;
    }

    public void setAccountDetailServiceDaoImpl(IAccountDetailServiceDao accountDetailServiceDaoImpl) {
        this.accountDetailServiceDaoImpl = accountDetailServiceDaoImpl;
    }

}
