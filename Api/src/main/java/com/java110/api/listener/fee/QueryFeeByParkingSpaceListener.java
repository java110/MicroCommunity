package com.java110.api.listener.fee;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.dto.FeeDto;
import com.java110.dto.OwnerCarDto;
import com.java110.dto.ParkingSpaceDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiParkingSpaceFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryFeeByParkingSpace")
public class QueryFeeByParkingSpaceListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;


    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_FEE_BY_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateFeeData(reqJson);
        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setPayerObjId(reqJson.getString("psId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);
        ResponseEntity<String> responseEntity = null;
        if (feeDtos == null || feeDtos.size() == 0) {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
            return ;
        }

        FeeDto feeDto = feeDtos.get(0);

        ApiParkingSpaceFeeVo apiFeeVo = BeanConvertUtil.covertBean(feeDto, ApiParkingSpaceFeeVo.class);

        //停车位信息
        ParkingSpaceDto parkingSpaceDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceDto.class);
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        Assert.listOnlyOne(parkingSpaceDtos, "未查询到或查询多条 车位信息");

        parkingSpaceDto = parkingSpaceDtos.get(0);

        BeanConvertUtil.covertBean(parkingSpaceDto, apiFeeVo);

        //查询车辆信息
        OwnerCarDto  ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "未查询到或查询多条 车辆信息");
        ownerCarDto = ownerCarDtos.get(0);

        BeanConvertUtil.covertBean(ownerCarDto, apiFeeVo);


        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);


        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateFeeData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "psId", "请求中未包含psId信息");


    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }

    public IFeeInnerServiceSMO getFeeInnerServiceSMOImpl() {
        return feeInnerServiceSMOImpl;
    }

    public void setFeeInnerServiceSMOImpl(IFeeInnerServiceSMO feeInnerServiceSMOImpl) {
        this.feeInnerServiceSMOImpl = feeInnerServiceSMOImpl;
    }


    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }


    public IOwnerCarInnerServiceSMO getOwnerCarInnerServiceSMOImpl() {
        return ownerCarInnerServiceSMOImpl;
    }

    public void setOwnerCarInnerServiceSMOImpl(IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl) {
        this.ownerCarInnerServiceSMOImpl = ownerCarInnerServiceSMOImpl;
    }
}
