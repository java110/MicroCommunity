package com.java110.acct.cmd.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "account.deleteAccount")
public class DeleteAccountCmd  extends Cmd {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "acctId", "acctId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AccountPo accountPo = BeanConvertUtil.covertBean(reqJson, AccountPo.class);
        int flag = accountInnerServiceSMOImpl.updateAccount(accountPo);

        if(flag < 1){
            throw new CmdException("更新失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
