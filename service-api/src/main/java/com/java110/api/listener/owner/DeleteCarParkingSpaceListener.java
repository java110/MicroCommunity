package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName DeleteCarParkingSpaceListener
 * @Description 释放车位
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * @site http://www.homecommunity.cn
 * add by wuxw 2019/4/26
 **/

@Java110Listener("deleteCarParkingSpaceListener")
public class DeleteCarParkingSpaceListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(DeleteCarParkingSpaceListener.class);

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DELETE_CAR_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    /**
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        String psId = ownerCarDtos.get(0).getPsId();

        if (StringUtil.isEmpty(psId) || "-1".equals(psId)) {
            throw new IllegalArgumentException("车位已经释放无需释放");
        }

        if (ownerCarDtos.get(0).getEndTime().getTime() > DateUtil.getCurrentDate().getTime()) {
            throw new IllegalArgumentException("车位租用还未结束不能释放");
        }

        reqJson.put("ownerCarDto", ownerCarDtos.get(0));
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerCarDto ownerCarDto = (OwnerCarDto) reqJson.get("ownerCarDto");
        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);
        boolean oweFee = false;
        for (FeeDto tmpFeeDto : feeDtoList) {
            if (tmpFeeDto.getEndTime().getTime() < ownerCarDto.getEndTime().getTime()) {
                oweFee = true;
                break;
            }
        }

        if (OwnerCarDto.STATE_OWE.equals(ownerCarDto.getState())) {
            oweFee = false;
        }


        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setPsId("-1");
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        if (oweFee) {
            ownerCarPo.setState(OwnerCarDto.STATE_OWE);
        }
        super.update(context, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR);

        reqJson.put("carNumType", ParkingSpaceDto.STATE_FREE);
        reqJson.put("psId", ownerCarDto.getPsId());
        parkingSpaceBMOImpl.modifySellParkingSpaceState(reqJson, context);
    }


    @Override
    public int getOrder() {
        return 0;
    }

}
