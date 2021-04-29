package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @ClassName DeleteCarParkingSpaceListener
 * @Description 续租车位
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * @site http://www.homecommunity.cn
 * add by wuxw 2019/4/26
 **/

@Java110Listener("carAddParkingSpaceListener")
public class CarAddParkingSpaceListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(CarAddParkingSpaceListener.class);

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_CAR_ADD_PARKING_SPACE;
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
        Assert.jsonObjectHaveKey(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.jsonObjectHaveKey(reqJson, "endTime", "请求报文中未包含startTime");
        Assert.jsonObjectHaveKey(reqJson, "psId", "请求报文中未包含psId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");

        String psId = ownerCarDtos.get(0).getPsId();

        if (!StringUtil.isEmpty(psId) && !"-1".equals(psId)) {
            throw new IllegalArgumentException("已有车位无需续租");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(reqJson, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
        super.update(context, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR);
        reqJson.put("carNumType", ParkingSpaceDto.STATE_HIRE);
        parkingSpaceBMOImpl.modifySellParkingSpaceState(reqJson, context);

    }


    @Override
    public int getOrder() {
        return 0;
    }

}
