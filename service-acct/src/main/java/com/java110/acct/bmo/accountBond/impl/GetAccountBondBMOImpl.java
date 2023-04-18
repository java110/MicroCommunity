package com.java110.acct.bmo.accountBond.impl;

import com.java110.acct.bmo.accountBond.IGetAccountBondBMO;
import com.java110.intf.acct.IAccountBondInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.account.AccountBondDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountBondBMOImpl")
public class GetAccountBondBMOImpl implements IGetAccountBondBMO {

    @Autowired
    private IAccountBondInnerServiceSMO accountBondInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountBondDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AccountBondDto accountBondDto) {


        int count = accountBondInnerServiceSMOImpl.queryAccountBondsCount(accountBondDto);

        List<AccountBondDto> accountBondDtos = null;
        if (count > 0) {
            accountBondDtos = accountBondInnerServiceSMOImpl.queryAccountBonds(accountBondDto);
        } else {
            accountBondDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountBondDto.getRow()), count, accountBondDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
