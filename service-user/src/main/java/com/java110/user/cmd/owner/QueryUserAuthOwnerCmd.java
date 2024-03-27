package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.LoginOwnerResDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询认证业主信息
 */
@Java110Cmd(serviceCode = "owner.queryUserAuthOwner")
public class QueryUserAuthOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);
        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("用户未登录");
        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)
                || OwnerAppUserDto.STATE_AUDIT_ERROR.equals(ownerAppUserDtos.get(0).getState())) {
            throw new CmdException("用户未认证,请先认证");
        }

        if (OwnerAppUserDto.STATE_AUDITING.equals(ownerAppUserDtos.get(0).getState())) {
            ResultVo resultVo = new ResultVo(-101, "认证审核中");
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerAppUserDtos.get(0).getMemberId());
        ownerDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        ownerDto = ownerDtos.get(0);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerDto.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "小区不存在");

        LoginOwnerResDto loginOwnerResDto = new LoginOwnerResDto();
        loginOwnerResDto.setOwnerId(ownerDto.getOwnerId());
        loginOwnerResDto.setMemberId(ownerDto.getMemberId());
        loginOwnerResDto.setOwnerName(ownerDto.getName());
        loginOwnerResDto.setUserId(userId);
        loginOwnerResDto.setUserName(ownerDto.getName());
        loginOwnerResDto.setOwnerTel(ownerDto.getLink());
        loginOwnerResDto.setCommunityId(ownerDto.getCommunityId());
        loginOwnerResDto.setCommunityName(communityDtos.get(0).getName());
        loginOwnerResDto.setCommunityTel(communityDtos.get(0).getTel());
        loginOwnerResDto.setOwnerTypeCd(ownerDto.getOwnerTypeCd());
        loginOwnerResDto.setAppUserId(ownerAppUserDtos.get(0).getAppUserId());
        context.setResponseEntity(ResultVo.createResponseEntity(loginOwnerResDto));

    }
}
