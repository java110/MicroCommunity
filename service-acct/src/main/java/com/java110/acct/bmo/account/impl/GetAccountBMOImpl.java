package com.java110.acct.bmo.account.impl;

import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.utils.lock.DistributedLock;
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

    //键(积分账户最大使用积分)
    public static final String MAXIMUM_NUMBER = "MAXIMUM_NUMBER";

    //键(积分账户抵扣比例)
    public static final String DEDUCTION_PROPORTION = "DEDUCTION_PROPORTION";

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

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
     *
     * @param accountDto
     * @param ownerDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryOwnerAccount(AccountDto accountDto, OwnerDto ownerDto) {

        List<OwnerDto> ownerDtos = null;
        List<AccountDto> accountDtos = null;
        int count = 0;
        if (!StringUtil.isEmpty(ownerDto.getIdCard())) {
            //先查询业主
            ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            if (ownerDtos != null && ownerDtos.size() > 0) {
                accountDto.setAcctName("");
                accountDto.setObjId(ownerDtos.get(0).getMemberId());
            }
        }

        count = accountInnerServiceSMOImpl.queryAccountsCount(accountDto);
        if (count > 0) {
            accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        } else {
            accountDtos = new ArrayList<>();
        }


        if (accountDtos == null || accountDtos.size() < 1) {
            //添加 账户
            accountDtos = addAccountDto(accountDto, ownerDto);

        }
        //积分账户最大使用积分
        String maximumNumber = CommunitySettingFactory.getValue(ownerDto.getCommunityId(), MAXIMUM_NUMBER);
        //积分账户抵扣比例
        String deductionProportion = CommunitySettingFactory.getValue(ownerDto.getCommunityId(), DEDUCTION_PROPORTION);
        List<AccountDto> accountList = new ArrayList<>();
        for (AccountDto account : accountDtos) {
            if (!StringUtil.isEmpty(maximumNumber)) {
                account.setMaximumNumber(maximumNumber);
            }
            if (!StringUtil.isEmpty(deductionProportion)) {
                account.setDeductionProportion(deductionProportion);
            }
            accountList.add(account);
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) accountDto.getRow()), count, accountList);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


    private List<AccountDto> addAccountDto(AccountDto accountDto, OwnerDto ownerDto) {
        // todo  查询账户名称 这里如果存在业主则业主名称 不是业主 则 填写用户名称，如果用户都没有 则返回空
        String acctName = getAccountName(ownerDto);

        if (StringUtil.isEmpty(acctName)) {
            return new ArrayList<>();
        }
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + "AddCountDto" + ownerDto.getOwnerId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            AccountPo accountPo = new AccountPo();
            accountPo.setAmount("0");
            accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
            accountPo.setObjId(ownerDto.getOwnerId());
            accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
            accountPo.setAcctType(AccountDto.ACCT_TYPE_CASH);
            accountPo.setAcctName(acctName);
            accountPo.setPartId(ownerDto.getCommunityId());
            accountPo.setLink(ownerDto.getLink());
            accountInnerServiceSMOImpl.saveAccount(accountPo);
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            return accountDtos;
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    private String getAccountName(OwnerDto ownerDto) {

        // todo  owner
        if (!StringUtil.isEmpty(ownerDto.getOwnerId())) {
            OwnerDto tmpOwnerDto = new OwnerDto();
            tmpOwnerDto.setMemberId(ownerDto.getOwnerId());
            tmpOwnerDto.setCommunityId(ownerDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
            if (ownerDtos == null || ownerDtos.size() < 1) {
                return "";
            }
            ownerDto.setCommunityId(ownerDtos.get(0).getCommunityId());
            ownerDto.setLink(ownerDtos.get(0).getLink());
            return ownerDtos.get(0).getName();
        }

        // todo 必须包含 手机号和小区
        if (StringUtil.isEmpty(ownerDto.getLink()) || StringUtil.isEmpty(ownerDto.getCommunityId())) {
            return "";
        }

        // todo 业主用 手机号查询
        OwnerDto tmpOwnerDto = new OwnerDto();
        tmpOwnerDto.setLink(ownerDto.getLink());
        tmpOwnerDto.setCommunityId(ownerDto.getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            ownerDto.setOwnerId(ownerDtos.get(0).getMemberId());
            return ownerDtos.get(0).getName();
        }

        //todo 非业主是游客
        UserDto userDto = new UserDto();
        userDto.setTel(ownerDto.getLink());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        if (userDtos != null && userDtos.size() > 0) {
            ownerDto.setOwnerId("-1");
            return userDtos.get(0).getName();
        }
        return "";
    }

}
