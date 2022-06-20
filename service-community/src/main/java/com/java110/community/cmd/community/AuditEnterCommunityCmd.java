package com.java110.community.cmd.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.CommunityMemberDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.po.community.CommunityMemberPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "community.auditEnterCommunity")
public class AuditEnterCommunityCmd  extends Cmd {

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityMemberId", "小区成员ID不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写小区审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写小区审核原因");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityMemberId(reqJson.getString("communityMemberId"));
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "未查询到该小区成员信息【" + communityMemberDto.getCommunityMemberId() + "】");
        communityMemberDto = communityMemberDtos.get(0);

        Map oldCommunityInfo = BeanConvertUtil.beanCovertMap(communityMemberDto);
        oldCommunityInfo.put("auditStatusCd", reqJson.getString("state"));
        CommunityMemberPo communityMemberPo = BeanConvertUtil.covertBean(oldCommunityInfo, CommunityMemberPo.class);
        int flag = communityMemberV1InnerServiceSMOImpl.updateCommunityMember(communityMemberPo);

        if (flag < 1) {
            throw new CmdException("更新小区失败");
        }
    }
}
