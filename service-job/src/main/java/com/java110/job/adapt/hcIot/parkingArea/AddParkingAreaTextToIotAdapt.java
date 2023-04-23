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
package com.java110.job.adapt.hcIot.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.parking.ParkingAreaTextDto;
import com.java110.entity.order.Business;
import com.java110.intf.community.IParkingAreaTextV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.parkingAreaText.ParkingAreaTextPo;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 停车场问候语同步适配器
 *
 * 541100030003
 * 541100040003
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "addParkingAreaTextToIotAdapt")
public class AddParkingAreaTextToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcParkingAreaTextAsynImpl;


    @Autowired
    private IParkingAreaTextV1InnerServiceSMO parkingAreaTextInnerServiceSMOImpl;


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
        JSONArray businessParkingAreaTexts = new JSONArray();
        if (data.containsKey(ParkingAreaTextPo.class.getSimpleName())) {
            Object bObj = data.get(ParkingAreaTextPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessParkingAreaTexts.add(bObj);
            } else if (bObj instanceof List) {
                businessParkingAreaTexts = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessParkingAreaTexts = (JSONArray) bObj;
            }
            //JSONObject businessParkingAreaText = data.getJSONObject("businessParkingAreaText");
        }else {
            if (data instanceof JSONObject) {
                businessParkingAreaTexts.add(data);
            }
        }
        for (int bParkingAreaTextIndex = 0; bParkingAreaTextIndex < businessParkingAreaTexts.size(); bParkingAreaTextIndex++) {
            JSONObject businessParkingAreaText = businessParkingAreaTexts.getJSONObject(bParkingAreaTextIndex);
            doSendParkingAreaText(business, businessParkingAreaText);
        }
    }

    private void doSendParkingAreaText(Business business, JSONObject businessParkingAreaText) {

        if (!businessParkingAreaText.containsKey("textId")) {
            return;
        }

        ParkingAreaTextDto parkingAreaTextDto = new ParkingAreaTextDto();
        parkingAreaTextDto.setTextId(businessParkingAreaText.getString("textId"));
        parkingAreaTextDto.setCommunityId(businessParkingAreaText.getString("communityId"));
        List<ParkingAreaTextDto> parkingAreaTextDtos = parkingAreaTextInnerServiceSMOImpl.queryParkingAreaTexts(parkingAreaTextDto);

        Assert.listOnlyOne(parkingAreaTextDtos, "未包含问候语");

        JSONObject postParameters = new JSONObject();

        postParameters.put("typeCd", parkingAreaTextDtos.get(0).getTypeCd());
        postParameters.put("text1", parkingAreaTextDtos.get(0).getText1());
        postParameters.put("text2", parkingAreaTextDtos.get(0).getText2());
        postParameters.put("text3", parkingAreaTextDtos.get(0).getText3());
        postParameters.put("text4", parkingAreaTextDtos.get(0).getText4());
        postParameters.put("voice", parkingAreaTextDtos.get(0).getVoice());
        postParameters.put("extPaId", parkingAreaTextDtos.get(0).getPaId());
        postParameters.put("extCommunityId", parkingAreaTextDtos.get(0).getCommunityId());
        hcParkingAreaTextAsynImpl.addParkingAreaText(postParameters);
    }
}
