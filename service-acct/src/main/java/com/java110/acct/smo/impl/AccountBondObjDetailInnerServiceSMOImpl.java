package com.java110.acct.smo.impl;


import com.java110.acct.dao.IAccountBondObjDetailServiceDao;
import com.java110.dto.account.AccountBondObjDetailDto;
import com.java110.intf.acct.IAccountBondObjDetailInnerServiceSMO;
import com.java110.po.accountBondObjDetail.AccountBondObjDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 保证金明细内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AccountBondObjDetailInnerServiceSMOImpl extends BaseServiceSMO implements IAccountBondObjDetailInnerServiceSMO {

    @Autowired
    private IAccountBondObjDetailServiceDao accountBondObjDetailServiceDaoImpl;


    @Override
    public int saveAccountBondObjDetail(@RequestBody AccountBondObjDetailPo accountBondObjDetailPo) {
        int saveFlag = 1;
        accountBondObjDetailServiceDaoImpl.saveAccountBondObjDetailInfo(BeanConvertUtil.beanCovertMap(accountBondObjDetailPo));
        return saveFlag;
    }

     @Override
    public int updateAccountBondObjDetail(@RequestBody  AccountBondObjDetailPo accountBondObjDetailPo) {
        int saveFlag = 1;
         accountBondObjDetailServiceDaoImpl.updateAccountBondObjDetailInfo(BeanConvertUtil.beanCovertMap(accountBondObjDetailPo));
        return saveFlag;
    }

     @Override
    public int deleteAccountBondObjDetail(@RequestBody  AccountBondObjDetailPo accountBondObjDetailPo) {
        int saveFlag = 1;
        accountBondObjDetailPo.setStatusCd("1");
        accountBondObjDetailServiceDaoImpl.updateAccountBondObjDetailInfo(BeanConvertUtil.beanCovertMap(accountBondObjDetailPo));
        return saveFlag;
    }

    @Override
    public List<AccountBondObjDetailDto> queryAccountBondObjDetails(@RequestBody  AccountBondObjDetailDto accountBondObjDetailDto) {

        //校验是否传了 分页信息

        int page = accountBondObjDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            accountBondObjDetailDto.setPage((page - 1) * accountBondObjDetailDto.getRow());
        }

        List<AccountBondObjDetailDto> accountBondObjDetails = BeanConvertUtil.covertBeanList(accountBondObjDetailServiceDaoImpl.getAccountBondObjDetailInfo(BeanConvertUtil.beanCovertMap(accountBondObjDetailDto)), AccountBondObjDetailDto.class);

        return accountBondObjDetails;
    }


    @Override
    public int queryAccountBondObjDetailsCount(@RequestBody AccountBondObjDetailDto accountBondObjDetailDto) {
        return accountBondObjDetailServiceDaoImpl.queryAccountBondObjDetailsCount(BeanConvertUtil.beanCovertMap(accountBondObjDetailDto));    }

    public IAccountBondObjDetailServiceDao getAccountBondObjDetailServiceDaoImpl() {
        return accountBondObjDetailServiceDaoImpl;
    }

    public void setAccountBondObjDetailServiceDaoImpl(IAccountBondObjDetailServiceDao accountBondObjDetailServiceDaoImpl) {
        this.accountBondObjDetailServiceDaoImpl = accountBondObjDetailServiceDaoImpl;
    }
}
