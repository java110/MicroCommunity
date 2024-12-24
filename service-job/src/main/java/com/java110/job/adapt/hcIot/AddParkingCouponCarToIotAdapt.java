package com.java110.job.adapt.hcIot;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingCouponCarDto;
import com.java110.dto.system.Business;
import com.java110.intf.acct.IParkingCouponCarV1InnerServiceSMO;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.intf.community.IParkingAreaV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.parking.ParkingCouponCarPo;
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
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;


    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

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

        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(parkingCouponCarDtos.get(0).getPaId());
        parkingAreaDto.setCommunityId(parkingCouponCarDtos.get(0).getCommunityId());
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaV1InnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "未找到停车场");
        ParkingCouponCarDto tmpParkingCouponCarDto = parkingCouponCarDtos.get(0);
        tmpParkingCouponCarDto.setPaNum(parkingAreaDtos.get(0).getNum());


        iotInnerServiceSMOImpl.sendCarCoupon(tmpParkingCouponCarDto);
    }
}