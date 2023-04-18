package com.java110.acct.bmo.accountBondObjDetail.impl;

import com.java110.acct.bmo.accountBondObjDetail.IGetAccountBondObjDetailBMO;
import com.java110.intf.acct.IAccountBondObjDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.account.AccountBondObjDetailDto;

import java.util.ArrayList;
import java.util.List;

@Service("getAccountBondObjDetailBMOImpl")
public class GetAccountBondObjDetailBMOImpl implements IGetAccountBondObjDetailBMO {

    @Autowired
    private IAccountBondObjDetailInnerServiceSMO accountBondObjDetailInnerServiceSMOImpl;

    /**
     *
     *
     * @param  accountBondObjDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AccountBondObjDetailDto accountBondObjDetailDto) {


        int count = accountBondObjDetailInnerServiceSMOImpl.queryAccountBondObjDetailsCount(accountBondObjDetailDto);

        List<AccountBondObjDetailDto> accountBondObjDetailDtos = null;
        if (count > 0) {
            accountBondObjDetailDtos = accountBondObjDetailInnerServiceSMOImpl.queryAccountBondObjDetails(accountBondObjDetailDto);
        } else {
            accountBondObjDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountBondObjDetailDto.getRow()), count, accountBondObjDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
