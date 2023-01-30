package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "owner.deleteAppUserBindingOwner")
public class DeleteAppUserBindingOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");

        if ("DEV".equals(env) || "TEST".equals(env)) {
            throw new IllegalArgumentException("演示环境不能解绑，解绑后演示账号手机端无法登陆");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(reqJson, OwnerAppUserPo.class);
        int flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("删除失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
