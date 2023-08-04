package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.deleteAppUserBindingOwner")
public class DeleteAppUserBindingOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV");

        if ("DEV".equals(env) || "TEST".equals(env)) {
            throw new IllegalArgumentException("演示环境不能解绑，解绑后演示账号手机端无法登陆");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        // todo 查询 绑定信息

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        Assert.listOnlyOne(ownerAppUserDtos, "绑定信息不存在");

        // 删除绑定信息
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(reqJson, OwnerAppUserPo.class);
        int flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("删除失败");
        }

        if (StringUtil.isEmpty(ownerAppUserDtos.get(0).getUserId())) {
            return;
        }
        if (ownerAppUserDtos.get(0).getUserId().startsWith("-")) {
            return;
        }
        // todo 删除用户信息
        UserPo userPo = new UserPo();
        userPo.setUserId(ownerAppUserDtos.get(0).getUserId());
        userV1InnerServiceSMOImpl.deleteUser(userPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
