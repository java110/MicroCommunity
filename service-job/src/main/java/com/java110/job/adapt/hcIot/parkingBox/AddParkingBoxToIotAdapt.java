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
package com.java110.job.adapt.hcIot.parkingBox;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.parking.ParkingBoxDto;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.intf.community.IParkingBoxV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.parkingBox.ParkingBoxPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 停车场同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "addParkingBoxToIotAdapt")
public class AddParkingBoxToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcParkingBoxAsynImpl;


    @Autowired
    private IParkingBoxV1InnerServiceSMO parkingBoxInnerServiceSMOImpl;

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaInnerServiceSMOImpl;


    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;


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
//        if (data.containsKey(ParkingBoxPo.class.getSimpleName())) {
//            Object bObj = data.get(ParkingBoxPo.class.getSimpleName());
//            JSONArray businessParkingBoxs = null;
//            if (bObj instanceof JSONObject) {
//                businessParkingBoxs = new JSONArray();
//                businessParkingBoxs.add(bObj);
//            } else if (bObj instanceof List) {
//                businessParkingBoxs = JSONArray.parseArray(JSONObject.toJSONString(bObj));
//            } else {
//                businessParkingBoxs = (JSONArray) bObj;
//            }
//            //JSONObject businessParkingBox = data.getJSONObject("businessParkingBox");
//            for (int bParkingBoxIndex = 0; bParkingBoxIndex < businessParkingBoxs.size(); bParkingBoxIndex++) {
//                JSONObject businessParkingBox = businessParkingBoxs.getJSONObject(bParkingBoxIndex);
//                doSendParkingBox(business, businessParkingBox);
//            }
//        }
        doSendParkingBox(business, data);
    }

    private void doSendParkingBox(Business business, JSONObject businessParkingBox) {

        ParkingBoxPo parkingBoxPo = BeanConvertUtil.covertBean(businessParkingBox, ParkingBoxPo.class);

        ParkingBoxDto parkingBoxDto = new ParkingBoxDto();
        parkingBoxDto.setBoxId(parkingBoxPo.getBoxId());
        parkingBoxDto.setCommunityId(parkingBoxPo.getCommunityId());
        List<ParkingBoxDto> parkingBoxDtos = parkingBoxInnerServiceSMOImpl.queryParkingBoxs(parkingBoxDto);

        Assert.listOnlyOne(parkingBoxDtos, "未找到岗亭");

        //查询属性
        ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
        parkingBoxAreaDto.setBoxId(parkingBoxDtos.get(0).getBoxId());
        parkingBoxAreaDto.setCommunityId(parkingBoxDtos.get(0).getCommunityId());
        List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaInnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

        JSONObject postParameters = new JSONObject();

        postParameters.putAll(BeanConvertUtil.beanCovertMap(parkingBoxDtos.get(0)));
        postParameters.put("extBoxId",parkingBoxPo.getBoxId());
        postParameters.put("extPaId", parkingBoxDtos.get(0).getPaId());
        postParameters.put("extCommunityId", parkingBoxDtos.get(0).getCommunityId());
        postParameters.put("areas", parkingBoxAreaDtos);
        hcParkingBoxAsynImpl.addParkingBox(postParameters);
    }
}
