package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryOwnerCommunityImpl implements IMallCommonApiBmo {

    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");

    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("link"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }


        //todo 查询小区

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerDtos.get(0).getCommunityId());
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        if (ListUtil.isNull(communityDtos)) {
            throw new CmdException("小区不存在");
        }


        //todo 查询物业信息
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityDtos.get(0).getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        if (ListUtil.isNull(communityMemberDtos)) {
            throw new CmdException("物业不存在");
        }


        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        if (ListUtil.isNull(storeDtos)) {
            throw new CmdException("物业不存在");
        }

        JSONObject data = new JSONObject();
        data.put("ownerId", ownerDtos.get(0).getMemberId());
        data.put("ownerName", ownerDtos.get(0).getName());
        data.put("ownerTel", ownerDtos.get(0).getLink());
        data.put("communityId", communityDtos.get(0).getCommunityId());
        data.put("communityName", communityDtos.get(0).getName());
        data.put("storeId", storeDtos.get(0).getStoreId());
        data.put("storeName", storeDtos.get(0).getName());

        context.setResponseEntity(ResultVo.createResponseEntity(data));


    }
}
