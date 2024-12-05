package com.java110.acct.cmd.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.account.IOwnerPrestoreAccountBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IAccountReceiptV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.account.AccountPo;
import com.java110.po.account.AccountReceiptPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

/**
 * 业主预存
 */
@Java110Cmd(serviceCode = "account.ownerPrestoreAccount")
public class OwnerPrestoreAccountCmd extends Cmd {

    @Autowired
    private IOwnerPrestoreAccountBMO ownerPrestoreAccountBMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IAccountReceiptV1InnerServiceSMO accountReceiptV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "ownerId", "业主不能为空");
        Assert.hasKeyAndValue(reqJson, "amount", "金额不能为空");
        Assert.hasKeyAndValue(reqJson, "acctType", "账户类型不能为空");
        Assert.hasKeyAndValue(reqJson, "primeRate", "未包含支付方式");
        String acctType = reqJson.getString("acctType");
        if (AccountDto.ACCT_TYPE_PROPERTY_FEE.equals(acctType) || AccountDto.ACCT_TYPE_METER.equals(acctType)) {
            Assert.hasKeyAndValue(reqJson, "roomId", "未包含扣款房屋");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setRemark(reqJson.getString("remark"));
        accountDetailPo.setObjId(reqJson.getString("ownerId"));
        accountDetailPo.setAmount(reqJson.getString("amount"));


        //查询 业主是否有账户
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(accountDetailPo.getObjId());
        accountDto.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountDto.setPartId(reqJson.getString("communityId"));
        accountDto.setAcctType(reqJson.getString("acctType"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (ListUtil.isNull(accountDtos)) {
            accountDto = addAccountDto(reqJson);
            //保存交易明细
            AccountDetailPo accountDetail = BeanConvertUtil.covertBean(accountDetailPo, AccountDetailPo.class);
            accountDetail.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetail.setAcctId(accountDto.getAcctId());
            accountDetail.setObjType(AccountDetailDto.ORDER_TYPE_USER);
            accountDetail.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
            if (StringUtil.isEmpty(accountDetail.getDetailId())) {
                accountDetail.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            }
            if (StringUtil.isEmpty(accountDetail.getRelAcctId())) {
                accountDetail.setRelAcctId("-1");
            }
            accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetail);
        } else {
            accountDto = accountDtos.get(0);
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetailPo.setAcctId(accountDto.getAcctId());
            accountDetailPo.setObjType(AccountDetailDto.ORDER_TYPE_USER);
            int flag = accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);
            if (flag < 1) {
                throw new CmdException("预存失败");
            }
        }
        // todo 记录账户收款单
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在");

        AccountReceiptPo accountReceiptPo = new AccountReceiptPo();
        accountReceiptPo.setOwnerId(reqJson.getString("ownerId"));
        accountReceiptPo.setOwnerName(ownerDtos.get(0).getName());
        accountReceiptPo.setLink(ownerDtos.get(0).getLink());
        accountReceiptPo.setArId(GenerateCodeFactory.getGeneratorId("11"));
        accountReceiptPo.setAcctId(accountDto.getAcctId());
        accountReceiptPo.setPrimeRate(reqJson.getString("primeRate"));
        accountReceiptPo.setReceivableAmount(reqJson.getString("amount"));
        accountReceiptPo.setReceivedAmount(reqJson.getString("amount"));
        accountReceiptPo.setRemark(reqJson.getString("remark"));
        accountReceiptPo.setCommunityId(reqJson.getString("communityId"));
        accountReceiptV1InnerServiceSMOImpl.saveAccountReceipt(accountReceiptPo);

        context.setResponseEntity(ResultVo.success());
    }

    /**
     * 添加账户
     * @param reqJson
     * @return
     */
    private AccountDto addAccountDto(JSONObject reqJson) {

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos,"房屋不存在");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "业主不存在");

        AccountPo accountPo = new AccountPo();
        accountPo.setAmount(reqJson.getString("amount"));
        accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
        accountPo.setObjId(reqJson.getString("ownerId"));
        accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
        accountPo.setAcctType(reqJson.getString("acctType"));
        accountPo.setAcctName(ownerDtos.get(0).getName());
        accountPo.setPartId(reqJson.getString("communityId"));
        accountPo.setLink(ownerDtos.get(0).getLink());
        accountPo.setRoomId(roomDtos.get(0).getRoomId());
        accountPo.setRoomName(roomDtos.get(0).getRoomName());
        accountInnerServiceSMOImpl.saveAccount(accountPo);
        return BeanConvertUtil.covertBean(accountPo, AccountDto.class);
    }

}
