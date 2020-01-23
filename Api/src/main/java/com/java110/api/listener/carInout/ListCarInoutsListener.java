package com.java110.api.listener.carInout;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.ICarInoutInnerServiceSMO;
import com.java110.dto.hardwareAdapation.CarInoutDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeCarInoutConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.carInout.ApiCarInoutDataVo;
import com.java110.vo.api.carInout.ApiCarInoutVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listCarInoutsListener")
public class ListCarInoutsListener extends AbstractServiceApiListener {

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeCarInoutConstant.LIST_CARINOUTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICarInoutInnerServiceSMO getCarInoutInnerServiceSMOImpl() {
        return carInoutInnerServiceSMOImpl;
    }

    public void setCarInoutInnerServiceSMOImpl(ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl) {
        this.carInoutInnerServiceSMOImpl = carInoutInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        CarInoutDto carInoutDto = BeanConvertUtil.covertBean(reqJson, CarInoutDto.class);

        if (reqJson.containsKey("state") && reqJson.getString("state").contains(",")) {
            carInoutDto.setState("");
            String[] states = reqJson.getString("state").split(",");
            carInoutDto.setStates(states);
        }

        int count = carInoutInnerServiceSMOImpl.queryCarInoutsCount(carInoutDto);

        List<ApiCarInoutDataVo> carInouts = null;

        if (count > 0) {
            carInouts = BeanConvertUtil.covertBeanList(carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto), ApiCarInoutDataVo.class);
        } else {
            carInouts = new ArrayList<>();
        }

        ApiCarInoutVo apiCarInoutVo = new ApiCarInoutVo();

        apiCarInoutVo.setTotal(count);
        apiCarInoutVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarInoutVo.setCarInouts(carInouts);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCarInoutVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
