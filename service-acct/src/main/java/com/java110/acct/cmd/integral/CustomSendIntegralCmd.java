package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.account.AccountDto;
import com.java110.dto.integralConfig.IntegralConfigDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IIntegralConfigV1InnerServiceSMO;
import com.java110.intf.acct.IIntegralGiftDetailV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.integralGiftDetail.IntegralGiftDetailPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分赠送规则，提供第三方平台使用
 */
@Java110CmdDoc(title = "积分赠送",
        description = "三方平台赠送积分给用户",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/integral.customSendIntegral",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "integral.customSendIntegral"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "link", length = 30, remark = "手机号"),
        @Java110ParamDoc(name = "quantity", length = 30, remark = "赠送积分数量"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'link':'18909711443','quantity':'10','communityId':'12323123'}",
        resBody = "{'code':0,'msg':'成功'}"
)
@Java110Cmd(serviceCode = "integral.customSendIntegral")
public class CustomSendIntegralCmd extends Cmd {

    @Autowired
    private IIntegralGiftDetailV1InnerServiceSMO integralGiftDetailV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IIntegralConfigV1InnerServiceSMO integralConfigV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
        Assert.hasKeyAndValue(reqJson, "quantity", "未包含赠送积分数量");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        //向积分账户中充值积分
        AccountDto accountDto = new AccountDto();
        accountDto.setLink(reqJson.getString("link"));
        accountDto.setAcctType(AccountDto.ACCT_TYPE_INTEGRAL);
        accountDto.setPartId(reqJson.getString("communityId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            accountDtos = addAccountDto(accountDto, reqJson);
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setAmount(reqJson.getString("quantity"));
        int flag = accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);

        if (flag < 1) {
            throw new CmdException("扣款失败");
        }

        doGiftIntegral(accountDtos.get(0), reqJson);


    }

    private List<AccountDto> addAccountDto(AccountDto accountDto, JSONObject reqJson) {
        // todo  查询账户名称 这里如果存在业主则业主名称 不是业主 则 填写用户名称，如果用户都没有 则返回空
        String acctName = getAccountName(reqJson);

        if (StringUtil.isEmpty(acctName)) {
            return new ArrayList<>();
        }
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + "AddCountDto" + reqJson.getString("link");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            AccountPo accountPo = new AccountPo();
            accountPo.setAmount("0");
            accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
            accountPo.setObjId(reqJson.getString("objId"));
            accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
            accountPo.setAcctType(AccountDto.ACCT_TYPE_CASH);
            accountPo.setAcctName(acctName);
            accountPo.setPartId(reqJson.getString("communityId"));
            accountPo.setLink(reqJson.getString("link"));
            accountInnerServiceSMOImpl.saveAccount(accountPo);
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            return accountDtos;
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    private String getAccountName(JSONObject reqJson) {

        // todo 业主用 手机号查询
        OwnerDto tmpOwnerDto = new OwnerDto();
        tmpOwnerDto.setLink(reqJson.getString("link"));
        tmpOwnerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            reqJson.put("objId", ownerDtos.get(0).getMemberId());
            return ownerDtos.get(0).getName();
        }

        //todo 非业主是游客
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("link"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos != null && userDtos.size() > 0) {
            reqJson.put("objId", "-1");
            return userDtos.get(0).getName();
        }
        throw new CmdException("业主不存在");
    }

    private void doGiftIntegral(AccountDto accountDto, JSONObject reqJson) {

        IntegralConfigDto integralConfigDto = new IntegralConfigDto();
        integralConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<IntegralConfigDto> integralConfigDtos = integralConfigV1InnerServiceSMOImpl.queryIntegralConfigs(integralConfigDto);
        if(integralConfigDtos == null || integralConfigDtos.size()< 1){
            throw new CmdException("积分规则不存在，请添加");
        }

        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("link"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        //先加明细
        IntegralGiftDetailPo integralGiftDetailPo = new IntegralGiftDetailPo();
        integralGiftDetailPo.setCommunityId(reqJson.getString("communityId"));
        integralGiftDetailPo.setAcctId(accountDto.getAcctId());
        integralGiftDetailPo.setAcctName(accountDto.getAcctName());
        integralGiftDetailPo.setAcctDetailId("-1");
        integralGiftDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        integralGiftDetailPo.setConfigId(integralConfigDtos.get(0).getConfigId());
        integralGiftDetailPo.setConfigName(integralConfigDtos.get(0).getConfigName());
        integralGiftDetailPo.setRuleId("-1");
        integralGiftDetailPo.setRuleName(reqJson.getString("ruleName"));
        integralGiftDetailPo.setQuantity(reqJson.getString("quantity"));
        integralGiftDetailPo.setCreateUserId(userDtos.get(0).getUserId());
        integralGiftDetailPo.setUserName(userDtos.get(0).getName());
        integralGiftDetailPo.setTel(userDtos.get(0).getTel());
        integralGiftDetailV1InnerServiceSMOImpl.saveIntegralGiftDetail(integralGiftDetailPo);

    }
}
