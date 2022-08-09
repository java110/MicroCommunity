package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.updateAppUserBindingOwner")
public class UpdateAppUserBindingOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        if("1100".equals(reqJson.getString("state"))){
            reqJson.put("state","12000");
        }else{
            reqJson.put("state","13000");
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        Assert.listOnlyOne(ownerAppUserDtos, "存在多条审核单或未找到审核单");

        JSONObject businessOwnerAppUser = new JSONObject();
        businessOwnerAppUser.putAll(BeanConvertUtil.beanCovertMap(ownerAppUserDtos.get(0)));
        businessOwnerAppUser.put("state", reqJson.getString("state"));
        businessOwnerAppUser.put("appUserId", reqJson.getString("appUserId"));
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(businessOwnerAppUser, OwnerAppUserPo.class);
        int flag = ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("修改绑定失败");
        }
    }
}
