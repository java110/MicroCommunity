package com.java110.acct.cmd.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.account.AccountDetailDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "account.cancelAccountDetail")
public class CancelAccountDetailCmd extends Cmd {

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "detailId", "未包含明细");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "remark", "未包含撤销原因");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        AccountDetailDto accountDetailDto = new AccountDetailDto();
        accountDetailDto.setDetailId(reqJson.getString("detailId"));
        accountDetailDto.setDetailType(AccountDetailDto.DETAIL_TYPE_IN);
        List<AccountDetailDto> accountDetailDtos = accountDetailInnerServiceSMOImpl.queryAccountDetails(accountDetailDto);

        Assert.listOnlyOne(accountDetailDtos, "入账明细不存在");

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDetailDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDetailDtos.get(0).getObjId());
        accountDetailPo.setAmount(accountDetailDtos.get(0).getAmount());
        accountDetailPo.setRemark("明细：" + reqJson.getString("detailId") + "撤销，原因：" + reqJson.getString("remark"));
        int flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);
        if (flag < 1) {
            throw new CmdException("撤销失败");
        }

        AccountDetailPo accountDetailPo1 = new AccountDetailPo();
        accountDetailPo1.setDetailId(accountDetailDtos.get(0).getDetailId());
        accountDetailPo1.setDetailType(AccountDetailDto.DETAIL_TYPE_IN_CANCEL);
        flag = accountDetailInnerServiceSMOImpl.updateAccountDetails(accountDetailPo1);
        if (flag < 1) {
            throw new CmdException("撤销失败");
        }
    }
}
