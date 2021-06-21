package com.java110.acct.bmo.account.impl;

import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.util.StringUtil;
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

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

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

    @Override
    public ResponseEntity<String> getDetail(AccountDetailDto accountDetailDto) {
        int count = accountDetailInnerServiceSMOImpl.queryAccountDetailsCount(accountDetailDto);

        List<AccountDetailDto> accountDetailDtos = null;
        if (count > 0) {
            accountDetailDtos = accountDetailInnerServiceSMOImpl.queryAccountDetails(accountDetailDto);
        } else {
            accountDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountDetailDto.getRow()), count, accountDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 查询业主账号
     * @param accountDto
     * @param ownerDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryOwnerAccount(AccountDto accountDto, OwnerDto ownerDto) {

        List<OwnerDto> ownerDtos = null;
        if(!StringUtil.isEmpty(ownerDto.getLink()) || !StringUtil.isEmpty(ownerDto.getIdCard())) {
            //先查询业主
            ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        }

        if(ownerDtos != null && ownerDtos.size()> 0){
            accountDto.setAcctName("");
            accountDto.setObjId(ownerDtos.get(0).getMemberId());
        }

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
