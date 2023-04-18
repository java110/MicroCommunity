package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.IUpdateAccountWithdrawalApplyBMO;
import com.java110.acct.dao.IAccountServiceDao;
import com.java110.core.annotation.Java110Transactional;

import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountWithdrawalApplyDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.accountWithdrawalApply.AccountWithdrawalApplyPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("updateAccountWithdrawalApplyBMOImpl")
public class UpdateAccountWithdrawalApplyBMOImpl implements IUpdateAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;
    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;
    @Autowired
    private IAccountServiceDao accountServiceDaoImpl;
    /**
     *
     *
     * @param accountWithdrawalApplyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AccountWithdrawalApplyPo accountWithdrawalApplyPo) {

            if(accountWithdrawalApplyPo.getState().equals( AccountWithdrawalApplyPo.STATE_PASS ) || accountWithdrawalApplyPo.getState().equals( AccountWithdrawalApplyPo.STATE_ERROR_PAYER )){
                AccountWithdrawalApplyDto accountWithdrawalApplyDto = new AccountWithdrawalApplyDto();
                accountWithdrawalApplyDto.setApplyId( accountWithdrawalApplyPo.getApplyId() );
                accountWithdrawalApplyDto.setAcctId( accountWithdrawalApplyPo.getAcctId() );
                //查询到账户体现的费用
                List<AccountWithdrawalApplyDto> accountWithdrawalApplyDtos = accountWithdrawalApplyInnerServiceSMOImpl.queryAccountWithdrawalApplys( accountWithdrawalApplyDto );
                if (accountWithdrawalApplyDtos == null || accountWithdrawalApplyDtos.size() < 1) {
                    throw new IllegalArgumentException("账户提现信息不存在");
                }
                AccountWithdrawalApplyDto WithdrawalApplyDto = accountWithdrawalApplyDtos.get( 0 );
                AccountDetailPo accountDetailPo = new AccountDetailPo();
                accountDetailPo.setAcctId( WithdrawalApplyDto.getAcctId() );
                accountDetailPo.setAmount( WithdrawalApplyDto.getAmount() );
                accountDetailPo.setRemark( accountWithdrawalApplyPo.getContext() );
                //查询账户详细信息获得objId
                AccountDto accountDto = new AccountDto();
                accountDto.setAcctId(WithdrawalApplyDto.getAcctId());
                List<AccountDto> accounts = BeanConvertUtil.covertBeanList(accountServiceDaoImpl.getAccountInfo(BeanConvertUtil.beanCovertMap(accountDto)), AccountDto.class);
                if (accounts == null || accounts.size() < 1) {
                    throw new IllegalArgumentException("账户不存在");
                }
                accountDetailPo.setObjId( accounts.get( 0 ).getObjId() );
                accountDetailPo.setObjType( "7007");
                //调用预存接口
                accountInnerServiceSMOImpl.prestoreAccount( accountDetailPo );
        }

        int flag = accountWithdrawalApplyInnerServiceSMOImpl.updateAccountWithdrawalApply(accountWithdrawalApplyPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
