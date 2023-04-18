package com.java110.acct.bmo.accountWithdrawalApply.impl;

import com.java110.acct.bmo.accountWithdrawalApply.IGetAccountWithdrawalApplyBMO;
import com.java110.intf.acct.IAccountWithdrawalApplyInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.account.AccountWithdrawalApplyDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountWithdrawalApplyBMOImpl")
public class GetAccountWithdrawalApplyBMOImpl implements IGetAccountWithdrawalApplyBMO {

    @Autowired
    private IAccountWithdrawalApplyInnerServiceSMO accountWithdrawalApplyInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountWithdrawalApplyDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AccountWithdrawalApplyDto accountWithdrawalApplyDto) {


        int count = accountWithdrawalApplyInnerServiceSMOImpl.queryAccountWithdrawalApplysCount(accountWithdrawalApplyDto);

        List<AccountWithdrawalApplyDto> accountWithdrawalApplyDtos = null;
        if (count > 0) {
            accountWithdrawalApplyDtos = accountWithdrawalApplyInnerServiceSMOImpl.queryAccountWithdrawalApplys(accountWithdrawalApplyDto);
        } else {
            accountWithdrawalApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountWithdrawalApplyDto.getRow()), count, accountWithdrawalApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listStateWithdrawalApplys(String[] states, int page, int row) {
        int count = accountWithdrawalApplyInnerServiceSMOImpl.listStateWithdrawalApplysCount( states);

        List<AccountWithdrawalApplyDto> accountWithdrawalApplyDtos = null;
        if (count > 0) {
            accountWithdrawalApplyDtos = accountWithdrawalApplyInnerServiceSMOImpl.listStateWithdrawalApplys(states);
        } else {
            accountWithdrawalApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) page), count, accountWithdrawalApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
