package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.common.ICarInoutInnerServiceSMO;
import com.java110.core.smo.common.IMachineInnerServiceSMO;
import com.java110.core.smo.user.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.community.IParkingSpaceInnerServiceSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.api.machine.MachineResDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MachineRoadGateOpenListener
 * @Description 查询空闲车位
 * @Author wuxw
 * @Date 2020/1/25 21:50
 * @Version 1.0
 * add by wuxw 2020/1/25
 **/
@Java110Listener("machineGetFreeParkingSpaceListener")
public class MachineGetFreeParkingSpaceListener extends BaseMachineListener {
    private static Logger logger = LoggerFactory.getLogger(MachineGetFreeParkingSpaceListener.class);


    private static final String MACHINE_DIRECTION_IN = "3306"; // 进入

    private static final String MACHINE_DIRECTION_OUT = "3307"; //出去

    private static final String HIRE_SELL_OUT = "hireSellOut"; // 出租或出售车辆出场

    private static final String CAR_BLACK = "1111"; // 车辆黑名单
    private static final String CAR_WHITE = "2222"; // 车辆白名单

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO carInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        //super.validateMachineHeader(event, reqJson);

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        //JSONObject outParam = null;
        String communityId = reqJson.getString("communityId");

        //查询出小区内车位状态为空闲的数量
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(communityId);
        parkingSpaceDto.setState("F");
        int freeParkingSpaceCount = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);


        //查询出小区内的在场车辆
        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCommunityId(communityId);
        carInoutDto.setStates(new String[]{"100300", "100400", "100600"});//状态，100300 进场状态 100400 支付完成 100500 离场状态 100600 支付超时重新支付
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);
        List<String> carNums = new ArrayList<>();//小区内的在场车辆车牌
        for (CarInoutDto tmpCarInoutDto : carInoutDtos) {
            carNums.add(tmpCarInoutDto.getCarNum());
        }
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        if(!carNums.isEmpty()){
            ownerCarDto.setCarNums(carNums.toArray(new String[carNums.size()]));
        }
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        //付款方ID-车牌号
        Map<String,String> psIdAndCarNumMap = new HashMap<>();
        List<String> psIds = new ArrayList<>();
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            psIds.add(tmpOwnerCarDto.getPsId());
            psIdAndCarNumMap.put(tmpOwnerCarDto.getPsId(),tmpOwnerCarDto.getCarNum());
        }
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setPayerObjIds(psIds.toArray(new String[psIds.size()]));
        feeDto.setNoArrearsEndTime(new Date());

//        int communityCarCount = feeInnerServiceSMOImpl.queryFeesCount(feeDto);
        List<FeeDto> communityCars = feeInnerServiceSMOImpl.queryFees(feeDto);//有效的月报车位信息，已经支付租金的
        for(FeeDto communityCar:communityCars){
            if(psIdAndCarNumMap.containsKey(communityCar.getPayerObjId())){
                carNums.remove(psIdAndCarNumMap.get(communityCar.getPayerObjId()));//把场内月租车位的业主车牌去掉，不算进场车辆
            }
        }

        //在场车辆车牌号【数组】-业主车牌号（有效的已租已售）【数组】，业主车牌有进场才去扣除，没进场不进行扣减
//        int realCarCount = carInoutDtos.size() - communityCarCount;
        int realCarCount = carNums.size();

        int realFreeParkingSpaceCount = freeParkingSpaceCount - realCarCount;

        JSONObject realFreeParkingSpace = new JSONObject();
        realFreeParkingSpace.put("total", freeParkingSpaceCount);
        realFreeParkingSpace.put("freeCount", realFreeParkingSpaceCount < 0 ? 0 : realFreeParkingSpaceCount);

        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功", realFreeParkingSpace));

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_GET_FREE_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
