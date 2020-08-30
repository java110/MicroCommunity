package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.entity.center.AppService;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName SaveParkingSpaceCreateFeeListener
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/31 15:57
 * @Version 1.0
 * add by wuxw 2020/1/31
 **/
@Java110Listener("saveParkingSpaceCreateFeeListener")
public class SaveParkingSpaceCreateFeeListener extends AbstractServiceApiListener {
    private static Logger logger = LoggerFactory.getLogger(SaveParkingSpaceCreateFeeListener.class);


    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_PARKING_SPEC_CREATE_FEE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "未包含收费类型");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        logger.debug("ServiceDataFlowEvent : {}", event);
        List<OwnerCarDto> ownerCarDtos = null;
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        //判断收费范围
        OwnerCarDto ownerCarDto = new OwnerCarDto();

        if ("1000".equals(reqJson.getString("locationTypeCd"))) {//小区
            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
            ownerCarDto.setValid("1");
            ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        } else if ("2000".equals(reqJson.getString("locationTypeCd"))) {//停车场
            //ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            ownerCarDto.setCommunityId(reqJson.getString("communityId"));
            ownerCarDto.setCarId(reqJson.getString("locationObjId"));
            ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        } else {
            throw new IllegalArgumentException("收费范围错误");
        }

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的车位");
        }

        dealParkingSpaceFee(ownerCarDtos, context, reqJson, event);
    }

    private void dealParkingSpaceFee(List<OwnerCarDto> ownerCarDtos, DataFlowContext context, JSONObject reqJson, ServiceDataFlowEvent event) {

        AppService service = event.getAppService();


        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        JSONObject paramInObj = null;
        ResponseEntity<String> responseEntity = null;
        int failParkingSpaces = 0;
        //添加单元信息
        for (int ownerCarIndex = 0; ownerCarIndex < ownerCarDtos.size(); ownerCarIndex++) {

            businesses.add(feeBMOImpl.addFee(ownerCarDtos.get(ownerCarIndex), reqJson, context));

            if (ownerCarIndex % DEFAULT_ADD_FEE_COUNT == 0 && ownerCarIndex != 0) {

                responseEntity = feeBMOImpl.callService(context, service.getServiceCode(), businesses);

                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    failParkingSpaces += businesses.size();
                }

                businesses = new JSONArray();
            }
        }
        if (businesses != null && businesses.size() > 0) {

            responseEntity = feeBMOImpl.callService(context, service.getServiceCode(), businesses);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                failParkingSpaces += businesses.size();
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("totalCar", ownerCarDtos.size());
        paramOut.put("successCar", ownerCarDtos.size() - failParkingSpaces);
        paramOut.put("errorCar", failParkingSpaces);

        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }
}
