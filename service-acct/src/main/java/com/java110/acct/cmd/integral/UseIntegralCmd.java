package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.integral.IntegralSettingDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IIntegralSettingV1InnerServiceSMO;
import com.java110.intf.acct.IIntegralUserDetailV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.integralUserDetail.IntegralUserDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "integral.useIntegral")
public class UseIntegralCmd extends Cmd {

    @Autowired
    private IIntegralSettingV1InnerServiceSMO integralSettingV1InnerServiceSMOImpl;

    @Autowired
    private IIntegralUserDetailV1InnerServiceSMO integralUserDetailV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "acctId", "核销账号不存在");
        Assert.hasKeyAndValue(reqJson, "useMoney", "核销账号不存在");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        IntegralSettingDto integralSettingDto = new IntegralSettingDto();
        integralSettingDto.setCommunityId(reqJson.getString("communityId"));

        List<IntegralSettingDto> integralSettingDtos = integralSettingV1InnerServiceSMOImpl.queryIntegralSettings(integralSettingDto);

        Assert.listOnlyOne(integralSettingDtos, "请设置积分设置");

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(reqJson.getString("acctId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        Assert.listOnlyOne(accountDtos, "账户不存在");

        double settingMoney = Double.parseDouble(integralSettingDtos.get(0).getMoney());
        if(settingMoney == 0){
            settingMoney = 1;
        }
        BigDecimal useMoneyDec = new BigDecimal(Double.parseDouble(reqJson.getString("useMoney")));
        useMoneyDec = useMoneyDec.divide(new BigDecimal(settingMoney),2, BigDecimal.ROUND_HALF_UP);

        long quantity = new Double(Math.ceil(useMoneyDec.doubleValue())).longValue();
        long oldQuantity = new Double(Double.parseDouble(accountDtos.get(0).getAmount())).longValue();

        if (quantity > oldQuantity) {
            throw new CmdException("当前积分不够(" + oldQuantity + ")");
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setRemark(reqJson.getString("remark"));
        accountDetailPo.setAmount(quantity + "");

        int flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);
        if (flag < 1) {
            throw new CmdException("扣款失败");
        }

        IntegralUserDetailPo integralUserDetailPo = new IntegralUserDetailPo();
        integralUserDetailPo.setAcctDetailId("-1");
        integralUserDetailPo.setAcctId(reqJson.getString("acctId"));
        integralUserDetailPo.setAcctName(accountDtos.get(0).getAcctName());
        integralUserDetailPo.setDetailType("1001");
        integralUserDetailPo.setBusinessKey("-1");
        integralUserDetailPo.setUseQuantity(quantity + "");
        integralUserDetailPo.setMoney(reqJson.getString("useMoney"));
        integralUserDetailPo.setRemark(reqJson.getString("remark"));
        integralUserDetailPo.setCreateUserId(userDtos.get(0).getUserId());
        integralUserDetailPo.setUserName(userDtos.get(0).getName());
        integralUserDetailPo.setTel(userDtos.get(0).getTel());
        integralUserDetailPo.setCommunityId(reqJson.getString("communityId"));
        integralUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId("11"));
        integralUserDetailV1InnerServiceSMOImpl.saveIntegralUserDetail(integralUserDetailPo);
    }
}
