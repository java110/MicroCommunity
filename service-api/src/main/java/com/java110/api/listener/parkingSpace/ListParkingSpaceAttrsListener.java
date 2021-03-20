package com.java110.api.listener.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IParkingSpaceAttrInnerServiceSMO;
import com.java110.dto.parkingSpaceAttr.ParkingSpaceAttrDto;
import com.java110.utils.constant.ServiceCodeParkingSpaceAttrConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listParkingSpaceAttrsListener")
public class ListParkingSpaceAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IParkingSpaceAttrInnerServiceSMO parkingSpaceAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingSpaceAttrConstant.LIST_PARKINGSPACEATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IParkingSpaceAttrInnerServiceSMO getParkingSpaceAttrInnerServiceSMOImpl() {
        return parkingSpaceAttrInnerServiceSMOImpl;
    }

    public void setParkingSpaceAttrInnerServiceSMOImpl(IParkingSpaceAttrInnerServiceSMO parkingSpaceAttrInnerServiceSMOImpl) {
        this.parkingSpaceAttrInnerServiceSMOImpl = parkingSpaceAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ParkingSpaceAttrDto parkingSpaceAttrDto = BeanConvertUtil.covertBean(reqJson, ParkingSpaceAttrDto.class);

        int count = parkingSpaceAttrInnerServiceSMOImpl.queryParkingSpaceAttrsCount(parkingSpaceAttrDto);

        List<ParkingSpaceAttrDto> parkingSpaceAttrDtos = null;

        if (count > 0) {
            parkingSpaceAttrDtos = parkingSpaceAttrInnerServiceSMOImpl.queryParkingSpaceAttrs(parkingSpaceAttrDto);
        } else {
            parkingSpaceAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, parkingSpaceAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
