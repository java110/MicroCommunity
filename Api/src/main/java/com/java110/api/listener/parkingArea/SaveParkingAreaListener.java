package com.java110.api.listener.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingArea.IParkingAreaBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeParkingAreaConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveParkingAreaListener")
public class SaveParkingAreaListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IParkingAreaBMO parkingAreaBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "num", "必填，请填写停车场编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择停车场类型");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        //添加单元信息
        parkingAreaBMOImpl.addParkingArea(reqJson, context);


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingAreaConstant.ADD_PARKINGAREA;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


}
