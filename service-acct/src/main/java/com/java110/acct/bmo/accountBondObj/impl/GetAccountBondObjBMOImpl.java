package com.java110.acct.bmo.accountBondObj.impl;

import com.java110.acct.bmo.accountBondObj.IGetAccountBondObjBMO;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.account.AccountBondObjDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountBondObjBMOImpl")
public class GetAccountBondObjBMOImpl implements IGetAccountBondObjBMO {

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountBondObjDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AccountBondObjDto accountBondObjDto) {


        int count = accountBondObjInnerServiceSMOImpl.queryAccountBondObjsCount(accountBondObjDto);

        List<AccountBondObjDto> accountBondObjDtos = null;
        if (count > 0) {
            accountBondObjDtos = accountBondObjInnerServiceSMOImpl.queryAccountBondObjs(accountBondObjDto);
        } else {
            accountBondObjDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountBondObjDto.getRow()), count, accountBondObjDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
