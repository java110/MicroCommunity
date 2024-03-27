package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "owner.deleteAuthOwner")
public class DeleteAuthOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "未包含审核记录");

        String userId = CmdContextUtils.getUserId(context);


        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("认证记录不存在");
        }

        if (OwnerAppUserDto.STATE_AUDIT_SUCCESS.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("认证完成记录不能删除，请联系物业删除");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setAppUserId(reqJson.getString("appUserId"));
        ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);

    }
}
