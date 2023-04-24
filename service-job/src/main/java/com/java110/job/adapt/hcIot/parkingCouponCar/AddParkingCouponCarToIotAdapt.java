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
package com.java110.job.adapt.hcIot.parkingCouponCar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.parkingCoupon.ParkingCouponCarDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.parkingCouponCar.ParkingCouponCarPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 停车劵同步
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "addParkingCouponCarToIotAdapt")
public class AddParkingCouponCarToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcTempCarFeeConfigAsynImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingCouponCarV1InnerServiceSMO parkingCouponCarV1InnerServiceSMOImpl;


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
        JSONArray businessParkingCouponCars = new JSONArray();
        if (data instanceof JSONObject) {
            businessParkingCouponCars.add(data);
        }

        for (int bParkingCouponCarIndex = 0; bParkingCouponCarIndex < businessParkingCouponCars.size(); bParkingCouponCarIndex++) {
            JSONObject businessParkingCouponCar = businessParkingCouponCars.getJSONObject(bParkingCouponCarIndex);
            doSendParkingCouponCar(business, businessParkingCouponCar);
        }
    }

    private void doSendParkingCouponCar(Business business, JSONObject businessTempCarFeeConfig) {

        ParkingCouponCarPo parkingCouponCarPo = BeanConvertUtil.covertBean(businessTempCarFeeConfig, ParkingCouponCarPo.class);

        ParkingCouponCarDto parkingCouponCarDto = new ParkingCouponCarDto();
        parkingCouponCarDto.setPccId(parkingCouponCarPo.getPccId());
        parkingCouponCarDto.setCommunityId(parkingCouponCarPo.getCommunityId());
        List<ParkingCouponCarDto> parkingCouponCarDtos = parkingCouponCarV1InnerServiceSMOImpl.queryParkingCouponCars(parkingCouponCarDto);

        Assert.listOnlyOne(parkingCouponCarDtos, "未找到车辆停车劵");

        JSONObject postParameters = new JSONObject();

        String couponName = parkingCouponCarDtos.get(0).getCouponName();
        String shopName = parkingCouponCarDtos.get(0).getShopName();

        if (StringUtil.isEmpty(couponName)) {
            couponName = parkingCouponCarDtos.get(0).getTypeCdName();
        }
        if (StringUtil.isEmpty(shopName)) {
            shopName = "物业公司";
        }

        postParameters.put("couponName", couponName);
        postParameters.put("shopName",shopName);
        postParameters.put("extCommunityId", parkingCouponCarDtos.get(0).getCommunityId());
        postParameters.put("extPaId", parkingCouponCarDtos.get(0).getPaId());
        postParameters.put("carNum", parkingCouponCarDtos.get(0).getCarNum());
        postParameters.put("giveWay", parkingCouponCarDtos.get(0).getGiveWay());
        postParameters.put("typeCd", parkingCouponCarDtos.get(0).getTypeCd());
        postParameters.put("value", parkingCouponCarDtos.get(0).getValue());
        postParameters.put("startTime", parkingCouponCarDtos.get(0).getStartTime());
        postParameters.put("endTime", parkingCouponCarDtos.get(0).getEndTime());
        postParameters.put("extPccId", parkingCouponCarDtos.get(0).getPccId());

        hcTempCarFeeConfigAsynImpl.addParkingCouponCar(postParameters);
    }
}
