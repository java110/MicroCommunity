package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getFreeCommunitysImpl")
public class GetFreeCommunitysImpl implements IMallCommonApiBmo {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "areaCode", "未包含地区");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        JSONArray notInCommunityId = reqJson.getJSONArray("notInCommunityId");

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCityCode(reqJson.getString("areaCode"));
        if (!ListUtil.isNull(notInCommunityId)) {
            communityDto.setNotInCommunityId(notInCommunityId.toArray(new String[notInCommunityId.size()]));
        }

        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        context.setResponseEntity(ResultVo.createResponseEntity(communityDtos));

    }
}
