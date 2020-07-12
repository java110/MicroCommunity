package com.java110.api.listener.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeParkingAreaConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.parkingArea.ApiParkingAreaDataVo;
import com.java110.vo.api.parkingArea.ApiParkingAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listParkingAreasListener")
public class ListParkingAreasListener extends AbstractServiceApiListener {

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingAreaConstant.LIST_PARKINGAREAS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IParkingAreaInnerServiceSMO getParkingAreaInnerServiceSMOImpl() {
        return parkingAreaInnerServiceSMOImpl;
    }

    public void setParkingAreaInnerServiceSMOImpl(IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl) {
        this.parkingAreaInnerServiceSMOImpl = parkingAreaInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ParkingAreaDto parkingAreaDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaDto.class);

        int count = parkingAreaInnerServiceSMOImpl.queryParkingAreasCount(parkingAreaDto);

        List<ApiParkingAreaDataVo> parkingAreas = null;

        if (count > 0) {
            parkingAreas = BeanConvertUtil.covertBeanList(parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto), ApiParkingAreaDataVo.class);
        } else {
            parkingAreas = new ArrayList<>();
        }

        ApiParkingAreaVo apiParkingAreaVo = new ApiParkingAreaVo();

        apiParkingAreaVo.setTotal(count);
        apiParkingAreaVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiParkingAreaVo.setParkingAreas(parkingAreas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiParkingAreaVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
