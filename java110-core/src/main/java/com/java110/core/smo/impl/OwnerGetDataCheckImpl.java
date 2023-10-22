package com.java110.core.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.smo.IOwnerGetDataCheck;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.app.AppDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 业主 查询安全性校验
 */
@Service
public class OwnerGetDataCheckImpl implements IOwnerGetDataCheck {

    @Autowired(required = false)
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired(required = false)
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;


    @Autowired(required = false)
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired(required = false)
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    private boolean isOwner(String appId) {
        if (!AppDto.WECHAT_OWNER_APP_ID.equals(appId) && !AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {
            return false;
        }

        return true;
    }


    @Override
    public void checkOwnerAccount(String appId, String loginUserId, JSONObject reqJson) {

        if (!isOwner(appId)) {
            return;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(loginUserId);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        //todo 没有登录，说明不需要校验
        if (userDtos == null || userDtos.isEmpty()) {
            return;
        }

        //todo 如果 包含acctId 校验
        ifAcctIdCheck(reqJson, userDtos.get(0));

        // todo 如果包含link 校验
        ifAccountLinkCheck(reqJson, userDtos.get(0));

        String acctId = reqJson.getString("acctId");
        String link = reqJson.getString("link");
        if (StringUtil.isEmpty(acctId) && StringUtil.isEmpty(link)) {
            throw new IllegalArgumentException("业主查询条件错误");
        }

    }

    @Override
    public void checkOwnerFee(String appId, String loginUserId, JSONObject reqJson) {
        if (!isOwner(appId)) {
            return;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(loginUserId);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        //todo 没有登录，说明不需要校验
        if (userDtos == null || userDtos.isEmpty()) {
            return;
        }

        //todo 查询业主信息
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(userDtos.get(0).getTel());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        //todo 游客不校验
        if (ownerDtos == null || ownerDtos.isEmpty()) {
            return;
        }

        //todo 根据ownerId 查询
        ifFeeOwnerId(reqJson, ownerDtos.get(0));

        //todo 根据payerObjId 查询
        ifFeePayerObjId(reqJson, ownerDtos.get(0));

        //todo 根据feeId 查询
        ifFeeFeeId(reqJson, ownerDtos.get(0));

        String ownerId = reqJson.getString("ownerId");
        String payerObjId = reqJson.getString("payerObjId");
        String feeId = reqJson.getString("feeId");

        if (StringUtil.isEmpty(ownerId) && StringUtil.isEmpty(payerObjId) && StringUtil.isEmpty(feeId)) {
            throw new IllegalArgumentException("业主查询费用条件错误");
        }

    }

    private void ifFeeFeeId(JSONObject reqJson, OwnerDto ownerDto) {
        if (!reqJson.containsKey("feeId")) {
            return;
        }

        String feeId = reqJson.getString("feeId");
        if (StringUtil.isEmpty(feeId)) {
            return;
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return;
        }

        String ownerId = FeeAttrDto.getFeeAttrValue(feeDtos.get(0), FeeAttrDto.SPEC_CD_OWNER_ID);

        if (StringUtil.isEmpty(ownerId)) {
            return;
        }

        if (!ownerDto.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("业主查询不属于自己的数据");
        }
    }

    private void ifFeePayerObjId(JSONObject reqJson, OwnerDto ownerDto) {

        if (!reqJson.containsKey("payerObjId")) {
            return;
        }

        String payerObjId = reqJson.getString("payerObjId");
        if (StringUtil.isEmpty(payerObjId)) {
            return;
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("payerObjId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return;
        }

        String ownerId = FeeAttrDto.getFeeAttrValue(feeDtos.get(0), FeeAttrDto.SPEC_CD_OWNER_ID);

        if (StringUtil.isEmpty(ownerId)) {
            return;
        }

        if (!ownerDto.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("业主查询不属于自己的数据");
        }


    }

    private void ifFeeOwnerId(JSONObject reqJson, OwnerDto ownerDto) {

        if (!reqJson.containsKey("ownerId")) {
            return;
        }

        String ownerId = reqJson.getString("ownerId");
        if (StringUtil.isEmpty(ownerId)) {
            return;
        }

        if (!ownerId.equals(ownerDto.getOwnerId())) {
            throw new IllegalArgumentException("业主查询不属于自己的数据");
        }
    }

    private void ifAccountLinkCheck(JSONObject reqJson, UserDto userDto) {

        if (!reqJson.containsKey("link")) {
            return;
        }

        String link = reqJson.getString("link");
        if (StringUtil.isEmpty(link)) {
            return;
        }

        if (!userDto.getTel().equals(link)) {
            throw new IllegalArgumentException("业主查询不属于自己的数据");
        }
    }

    private void ifAcctIdCheck(JSONObject reqJson, UserDto userDto) {

        if (!reqJson.containsKey("accId")) {
            return;
        }

        String acctId = reqJson.getString("acctId");
        if (StringUtil.isEmpty(acctId)) {
            return;
        }

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.isEmpty()) {
            return;
        }

        if (!userDto.getTel().equals(accountDtos.get(0).getLink())) {
            throw new IllegalArgumentException("业主查询不属于自己的数据");
        }
    }
}
