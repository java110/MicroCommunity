package com.java110.acct.cmd.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IGetAccountBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.smo.IOwnerGetDataCheck;
import com.java110.dto.account.AccountDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 查询账户
 */
@Java110Cmd(serviceCode = "account.queryCommunityOwnerAccount")
public class QueryCommunityOwnerAccountCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IOwnerGetDataCheck ownerGetDataCheckImpl;

    @Autowired
    private IGetAccountBMO getAccountBMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        AccountDto accountDto = BeanConvertUtil.covertBean(reqJson, AccountDto.class);

        hasFeeId(reqJson, accountDto);
        String ownerId = reqJson.getString("ownerId");
        if (!StringUtil.isEmpty(ownerId)) {
            accountDto.setObjId(ownerId);
        }

        accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
        String acctTypes = reqJson.getString("acctTypes");
        if (!StringUtil.isNullOrNone(acctTypes)) {
            accountDto.setAcctTypes(acctTypes.split(","));
        }

        //todo 业主账户安全性校验
        // ownerGetDataCheckImpl.checkOwnerAccount(appId, userId, BeanConvertUtil.beanCovertJson(accountDto));

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(accountDto.getObjId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setLink(reqJson.getString("link"));
        ownerDto.setIdCard(reqJson.getString("idCard"));
        ResponseEntity<String> responseEntity = getAccountBMOImpl.queryOwnerAccount(accountDto, ownerDto);
        context.setResponseEntity(responseEntity);
    }

    private void hasFeeId(JSONObject reqJson, AccountDto accountDto) {

        if (!reqJson.containsKey("feeId")) {
            return;
        }
        String feeId = reqJson.getString("feeId");


        if (StringUtil.isEmpty(feeId)) {
            return;
        }
        String ownerId = "";
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "查询费用信息错误！");
        //获取付费对象类型(3333 房屋 6666 是车位)
        String payerObjType = feeDtos.get(0).getPayerObjType();
        //获取付费对象id
        String payerObjId = feeDtos.get(0).getPayerObjId();
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) { //房屋
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(payerObjId);
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系表错误！");
            ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(payerObjType)) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(payerObjId);
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
//                Assert.listOnlyOne(ownerCarDtos, "查询业主车辆关系表错误！");
            ownerId = ownerCarDtos.get(0).getOwnerId();
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(payerObjType)) {
            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(payerObjId);
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
//                Assert.listOnlyOne(ownerCarDtos, "查询业主车辆关系表错误！");
            ownerId = contractDtos.get(0).getObjId();
        } else {
            ownerId = "-1";
        }
        accountDto.setObjId(ownerId);
    }
}
