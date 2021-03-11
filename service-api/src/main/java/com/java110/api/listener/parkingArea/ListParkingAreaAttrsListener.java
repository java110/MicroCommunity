package com.java110.api.listener.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.parkingAreaAttr.ParkingAreaAttrDto;
import com.java110.intf.community.IParkingAreaAttrInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeParkingAreaAttrConstant;
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
@Java110Listener("listParkingAreaAttrsListener")
public class ListParkingAreaAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IParkingAreaAttrInnerServiceSMO parkingAreaAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingAreaAttrConstant.LIST_PARKINGAREAATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IParkingAreaAttrInnerServiceSMO getParkingAreaAttrInnerServiceSMOImpl() {
        return parkingAreaAttrInnerServiceSMOImpl;
    }

    public void setParkingAreaAttrInnerServiceSMOImpl(IParkingAreaAttrInnerServiceSMO parkingAreaAttrInnerServiceSMOImpl) {
        this.parkingAreaAttrInnerServiceSMOImpl = parkingAreaAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ParkingAreaAttrDto parkingAreaAttrDto = BeanConvertUtil.covertBean(reqJson, ParkingAreaAttrDto.class);

        int count = parkingAreaAttrInnerServiceSMOImpl.queryParkingAreaAttrsCount(parkingAreaAttrDto);

        List<ParkingAreaAttrDto> parkingAreaAttrDtos = null;

        if (count > 0) {
            parkingAreaAttrDtos = parkingAreaAttrInnerServiceSMOImpl.queryParkingAreaAttrs(parkingAreaAttrDto);
        } else {
            parkingAreaAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, parkingAreaAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
