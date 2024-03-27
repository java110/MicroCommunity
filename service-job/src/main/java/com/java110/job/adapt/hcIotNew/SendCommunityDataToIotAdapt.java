package com.java110.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired
    private ISendIot sendIotImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    public static final int DEFAULT_DEAL_COUNT = 200;

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
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

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
         * “storeName”:”演示物业”,
         * }
         */

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        Assert.listOnlyOne(storeDtos, "物业不存在");

        JSONObject paramIn = new JSONObject();
        paramIn.put("communityId", communityDtos.get(0).getCommunityId());
        paramIn.put("name", communityDtos.get(0).getName());
        paramIn.put("address", communityDtos.get(0).getAddress());
        paramIn.put("cityCode", communityDtos.get(0).getCityCode());
        paramIn.put("tel", storeDtos.get(0).getTel()); //todo 这里取 物业的
        paramIn.put("storeId", storeDtos.get(0).getStoreId());
        paramIn.put("storeName", storeDtos.get(0).getName());
        ResultVo resultVo = sendIotImpl.post("/iot/api/community.addCommunityApi", paramIn);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new IllegalArgumentException("同步小区物业失败：" + resultVo.getMsg());
        }


        //todo 同步员工
        sendStaff(storeDtos.get(0));

    }

    private void sendStaff(StoreDto storeDto) {


        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreId(storeDto.getStoreId());
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);
        if (ListUtil.isNull(storeUserDtos)) {
            return;
        }

        JSONObject staff = null;
        for (StoreUserDto tmpStoreUserDto : storeUserDtos) {
            if(StoreUserDto.REL_CD_MANAGER.equals(tmpStoreUserDto.getRelCd())){
                continue;
            }
            try {
                staff = new JSONObject();
                staff.put("propertyId", storeDto.getStoreId());
                staff.put("staffId", tmpStoreUserDto.getUserId());
                staff.put("name", tmpStoreUserDto.getName());
                staff.put("tel", tmpStoreUserDto.getTel());
                staff.put("relCd", tmpStoreUserDto.getRelCd());

                sendIotImpl.post("/iot/api/staff.addStaffApi", staff);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendOwner(String communityId) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        int count = ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);

        int page = 1;
        int max = 15;
        if (count < DEFAULT_DEAL_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_COUNT);
            max = DEFAULT_DEAL_COUNT;
        }

        ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);

        //todo  每次按200条处理
        List<OwnerDto> ownerDtos = null;
        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            ownerDto.setPage(pageIndex + 1);
            ownerDto.setRow(max);
            ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
            // 离散费用
            doSendOwners(communityId, ownerDtos);
        }

    }

    /**
     * 推送业主
     *
     * @param communityId
     * @param ownerDtos
     */
    private void doSendOwners(String communityId, List<OwnerDto> ownerDtos) {

        for (OwnerDto ownerDto : ownerDtos) {
            try {
                ownerDataToIotImpl.sendOwnerData(ownerDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
