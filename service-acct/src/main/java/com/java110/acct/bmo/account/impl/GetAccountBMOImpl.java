package com.java110.acct.bmo.account.impl;

import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.dto.account.AccountDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountBMOImpl")
public class GetAccountBMOImpl implements IGetAccountBMO {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    /**
     * @param accountDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AccountDto accountDto) {


        int count = accountInnerServiceSMOImpl.queryAccountsCount(accountDto);

        List<AccountDto> accountDtos = null;
        if (count > 0) {
            accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        } else {
            accountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountDto.getRow()), count, accountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
