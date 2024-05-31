package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.dao.IPayFeeDetailNewV1ServiceDao;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.account.AccountPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 押金退费
 */
@Java110Cmd(serviceCode = "fee.refundFeeDeposit")
public class RefundFeeDepositCmd extends Cmd {

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "detailId", "未包含缴费ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        feeDetailDto.setFeeId(reqJson.getString("feeId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        Assert.listOnlyOne(feeDetailDtos, "不存在该缴费记录");

        //todo 根据查询 账户信息
        OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDetailDtos.get(0).getFeeId());
        if (ownerDto == null) {
            throw new CmdException("费用未查到业主");
        }


        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(ownerDto.getMemberId());
        accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH); //2003  现金账户
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (ListUtil.isNull(accountDtos)) {
            throw new CmdException("账户不存在,请到账户页面预存0元，生成账户");
        }


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(reqJson.getString("detailId"));
        payFeeDetailPo.setState(FeeDetailDto.STATE_RETURNED);

        int flag = payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


        PayFeeDetailPo refundPayFeeDetailPo = BeanConvertUtil.covertBean(feeDetailDtos.get(0), PayFeeDetailPo.class);
        refundPayFeeDetailPo.setRemark("押金转存");
        refundPayFeeDetailPo.setbId("-1");
        refundPayFeeDetailPo.setState(FeeDetailDto.STATE_RETURN_ORDER);
        refundPayFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        refundPayFeeDetailPo.setCashierId(userDtos.get(0).getUserId());
        refundPayFeeDetailPo.setCashierName(userDtos.get(0).getName());
        refundPayFeeDetailPo.setReceivableAmount(unum(refundPayFeeDetailPo.getReceivableAmount()) + "");
        refundPayFeeDetailPo.setReceivedAmount(unum(refundPayFeeDetailPo.getReceivedAmount()) + "");
        refundPayFeeDetailPo.setLateAmount("0");
        refundPayFeeDetailPo.setDiscountAmount("0");
        refundPayFeeDetailPo.setGiftAmount("0");
        refundPayFeeDetailPo.setDeductionAmount("0");
        refundPayFeeDetailPo.setAcctAmount("0");
        refundPayFeeDetailPo.setCycles(unum(refundPayFeeDetailPo.getCycles()) + "");
        refundPayFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_REFUND_ACCT);
        payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(refundPayFeeDetailPo);


        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setAmount(feeDetailDtos.get(0).getReceivedAmount());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setRemark("押金退款预存账户");
        accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);


    }


    private double unum(String value) {
        double dValue = Double.parseDouble(value);
        return dValue * -1;
    }
}
