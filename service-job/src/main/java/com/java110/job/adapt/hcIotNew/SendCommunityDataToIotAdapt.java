package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.system.Business;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发送全量数据到 物联网
 */
@Component(value = "sendCommunityDataToIotAdapt")
public class SendCommunityDataToIotAdapt extends DatabusAdaptImpl {


    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;


    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        String communityId = data.getString("communityId");

        //todo 同步小区
        sendCommunityAndProperty(communityId);

        //todo 同步业主
        sendOwner(communityId);


    }

    /**
     * 同步小区和物业公司
     *
     * @param communityId 小区id
     */
    private void sendCommunityAndProperty(String communityId) {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityId);
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "小区成员不存在");

        /**
         * {
         * “communityId”:”12313”,
         * “name”:”演示小区”,
         * “address”:”地址”,
         * “cityCode”:”110101”,
         * “tel”:”18909711445”,
         * “storeId”:”11111”,
         * “storeId”:”演示物业”,
         * }
         */

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        Assert.listOnlyOne(storeDtos, "物业不存在");

//        HttpEntity httpEntity = new HttpEntity(JSONArray.toJSONString(staffs), getHeaders());
//        responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);


    }

    private void sendOwner(String communityId) {
    }


}
