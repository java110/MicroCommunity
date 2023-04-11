package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "org.listOrgNoCommunitys")
public class ListOrgNoCommunitysCmd extends Cmd {

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMO;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        if(!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId"))) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId",storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");

        Assert.hasKeyAndValue(reqJson, "roleId", "必填，请填写角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        RoleCommunityDto roleCommunityDto = BeanConvertUtil.covertBean(reqJson, RoleCommunityDto.class);
        roleCommunityDto.setPage(PageDto.DEFAULT_PAGE);
        List<RoleCommunityDto> orgCommunityDtos = roleCommunityV1InnerServiceSMO.queryRoleCommunitys(roleCommunityDto);
        List<String> communityIds = new ArrayList<>();
        for(RoleCommunityDto tmpOrgCommunityDto : orgCommunityDtos){
            communityIds.add(tmpOrgCommunityDto.getCommunityId());
        }
        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);
        if(communityIds.size()>0) {
            communityDto.setNotInCommunityId(communityIds.toArray(new String[communityIds.size()]));
        }
        communityDto.setAuditStatusCd("1100");
        communityDto.setMemberId(reqJson.getString("storeId"));
        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<ApiCommunityDataVo> communitys = null;

        if (count > 0) {
            communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);
        } else {
            communitys = new ArrayList<>();
        }

        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
