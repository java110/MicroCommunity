/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt.hcGov.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.po.floor.FloorPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增房屋同步HC政务接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "addRoomToHcGovAdapt")
public class AddRoomToHcGovAdapt extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;


    /**
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessRoom = new JSONArray();
        if (data.containsKey(RoomPo.class.getSimpleName())) {
            Object bObj = data.get(RoomPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessRoom.add(bObj);
            } else if (bObj instanceof List) {
                businessRoom = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessRoom = (JSONArray) bObj;
            }
        }else {
            if (data instanceof JSONObject) {
                businessRoom.add(data);
            }
        }
        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bRoomIndex = 0; bRoomIndex < businessRoom.size(); bRoomIndex++) {
            JSONObject businessOwnerCar = businessRoom.getJSONObject(bRoomIndex);
            doAddRoom(business, businessOwnerCar);

        }
    }

    private void doAddRoom(Business business, JSONObject businessRooom) {

        RoomPo roomPo = BeanConvertUtil.covertBean(businessRooom, RoomPo.class);
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId( roomPo.getRoomId() );
        roomDto.setCommunityId( roomPo.getCommunityId() );
        List<RoomDto>  roomDtos = roomInnerServiceSMOImpl.queryRooms( roomDto );
        Assert.listNotNull(roomDtos, "未查询到房屋信息");
        roomPo = BeanConvertUtil.covertBean(roomDtos.get(0), RoomPo.class);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(roomPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");

        UnitDto unitDto = new UnitDto();
        unitDto.setUnitId( roomPo.getUnitId() );
        unitDto.setCommunityId( roomPo.getCommunityId() );
        List<UnitDto>  unitDtos = unitInnerServiceSMOImpl.queryUnits( unitDto );
        Assert.listNotNull(unitDtos, "未包含单元信息");

        unitDto = new UnitDto();
        unitDto.setCommunityId( unitDtos.get( 0 ).getCommunityId() );
        unitDto.setFloorId( unitDtos.get( 0 ).getFloorId() );
        List<UnitDto>  unitDtoList = unitInnerServiceSMOImpl.queryUnits( unitDto );
        Assert.listNotNull(unitDtoList, "未通过楼栋所属ID查到楼栋数据，请检查数据");

        String  unitCount = unitDtoList.size()+""; //单元数
        String  layerCount = unitDtoList.get( 0 ).getLayerCount(); //楼层数

        CommunityDto tmpCommunityDto = communityDtos.get(0);
        String extCommunityId = "";
        String extFloorId = "";
        String communityId = tmpCommunityDto.getCommunityId();
        String roomId = roomPo.getRoomId();

        for (CommunityAttrDto communityAttrDto : tmpCommunityDto.getCommunityAttrDtos()) {
            if (HcGovConstant.EXT_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                extCommunityId = communityAttrDto.getValue();
            }
        }
        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId( unitDtos.get( 0 ).getCommunityId() );
        floorDto.setFloorId( unitDtos.get( 0 ).getFloorId() );
        List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors( floorDto );
        if(floorDtos == null || floorDtos.size() < 1){
            return;
        }

        FloorDto tmpFloorDto = floorDtos.get(0);
        for (FloorAttrDto floorAttrDto : tmpFloorDto.getFloorAttrDto()) {
            if (HcGovConstant.EXT_COMMUNITY_ID.equals(floorAttrDto.getSpecCd())) {
                extFloorId = floorAttrDto.getValue();
            }
        }
        JSONObject body = new JSONObject();
        body.put("roomNum", roomPo.getRoomNum());
        body.put("builtUpArea", roomPo.getBuiltUpArea());
        body.put("layer", roomPo.getLayer());
        body.put("roomArea", roomPo.getRoomArea());
        body.put("layerCount", layerCount);
        body.put("unitCount", unitCount);
        body.put("roomRent", roomPo.getRoomRent());
        body.put("userId", roomPo.getUnitId());//户主ID后期待业主信息同步后采用政务系统编码
        body.put("extFloorId", extFloorId);
        body.put("state", roomDtos.get( 0 ).getStateName());
        body.put("roomSubType", roomDtos.get( 0 ).getRoomSubTypeName());

        JSONObject kafkaData = baseHcGovSendAsynImpl.createHeadersOrBody(body, extCommunityId, HcGovConstant.ADD_ROOM_ACTION, HcGovConstant.COMMUNITY_SECURE);
        baseHcGovSendAsynImpl.sendKafka(HcGovConstant.GOV_TOPIC, kafkaData, communityId, roomId, HcGovConstant.COMMUNITY_SECURE);
    }

}
