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
package com.java110.job.adapt.hcGov.floor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.UnitDto;
import com.java110.dto.community.CommunityAttrDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IFloorAttrInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcGov.HcGovConstant;
import com.java110.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.java110.po.floor.FloorPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改楼栋同步HC政务接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "editFloorToHcGovAdapt")
public class EditFloorToHcGovAdapt extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;
    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;

    @Autowired
    private IFloorAttrInnerServiceSMO floorAttrInnerServiceSMOImpl;

    /**
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessOwnerCars = new JSONArray();
        if (data.containsKey(FloorPo.class.getSimpleName())) {
            Object bObj = data.get(FloorPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessOwnerCars.add(bObj);
            } else if (bObj instanceof List) {
                businessOwnerCars = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessOwnerCars = (JSONArray) bObj;
            }
        }else {
            if (data instanceof JSONObject) {
                businessOwnerCars.add(data);
            }
        }
        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bOwnerCarIndex = 0; bOwnerCarIndex < businessOwnerCars.size(); bOwnerCarIndex++) {
            JSONObject businessOwnerCar = businessOwnerCars.getJSONObject(bOwnerCarIndex);
            doEditFloor(business, businessOwnerCar);
        }
    }

    private void doEditFloor(Business business, JSONObject businessFloor) {

        FloorPo floorPo = BeanConvertUtil.covertBean(businessFloor, FloorPo.class);
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(floorPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);
        String extCommunityId = "";
        String communityId = tmpCommunityDto.getCommunityId();
        String floorId = floorPo.getFloorId();
        String extFloorId = "";

        for (CommunityAttrDto communityAttrDto : tmpCommunityDto.getCommunityAttrDtos()) {
            if (HcGovConstant.EXT_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                extCommunityId = communityAttrDto.getValue();
            }
        }
        UnitDto unitDto = new UnitDto();
        unitDto.setFloorId(floorPo.getFloorId());
        unitDto.setCommunityId(floorPo.getCommunityId());
        List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
        String layerCount = "0";
        String floorUse = "0";
        if (unitDtos != null && unitDtos.size() > 0) {
            layerCount = unitDtos.get(0).getLayerCount();
            floorUse = unitDtos.size() + "";
        }
        FloorAttrDto floorAttrDto = new FloorAttrDto();
        floorAttrDto.setFloorId(floorPo.getFloorId());
        floorAttrDto.setCommunityId(floorPo.getCommunityId());
        floorAttrDto.setSpecCd( HcGovConstant.EXT_COMMUNITY_ID);
        List<FloorAttrDto> floorAttrDtos = floorAttrInnerServiceSMOImpl.queryFloorAttrs(floorAttrDto);
        Assert.listNotNull(floorAttrDtos, "未包含楼栋外部编码属性");
        for (FloorAttrDto floorAttr : floorAttrDtos) {
            if (HcGovConstant.EXT_COMMUNITY_ID.equals(floorAttr.getSpecCd())) {
                extFloorId = floorAttr.getValue();
            }
        }

        JSONObject body = new JSONObject();
        body.put("floorNum", floorPo.getFloorNum());
        body.put("extFloorId", extFloorId);
        body.put("floorName", floorPo.getName());
        body.put("floorType", MappingCache.getValue(HcGovConstant.GOV_DOMAIN, HcGovConstant.FLOOR_TYPE));
        body.put("floorArea", floorPo.getFloorArea());
        body.put("layerCount", layerCount);
        body.put("unitCount", floorUse);
        body.put("floorUse", tmpCommunityDto.getName() + "_住宅");
        body.put("personName", "HC小区管理系统");
        body.put("personLink", "18909711234");

        JSONObject kafkaData = baseHcGovSendAsynImpl.createHeadersOrBody(body, extCommunityId, HcGovConstant.EDIT_FLOOR_ACTION, HcGovConstant.COMMUNITY_SECURE);
        baseHcGovSendAsynImpl.sendKafka(HcGovConstant.GOV_TOPIC, kafkaData, communityId, floorId, HcGovConstant.COMMUNITY_SECURE);
    }

}
