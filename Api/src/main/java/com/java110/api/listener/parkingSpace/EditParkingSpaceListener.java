
package com.java110.api.listener.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName EditParkingSpaceListener
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:19
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/
@Java110Listener("editParkingSpaceListener")
public class EditParkingSpaceListener extends AbstractServiceApiPlusListener {

    private static Logger logger = LoggerFactory.getLogger(EditParkingSpaceListener.class);

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EDIT_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "psId", "未包含停车位ID");
        Assert.jsonObjectHaveKey(reqJson, "paId", "未包含停车场信息");
        Assert.jsonObjectHaveKey(reqJson, "num", "请求报文中未包含num");
        Assert.jsonObjectHaveKey(reqJson, "area", "请求报文中未包含area");


        Assert.hasLength(reqJson.getString("psId"), "停车位ID不能为空");

        if (reqJson.getString("psId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "停车位ID必须为已有ID");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        parkingSpaceBMOImpl.editParkingSpace(reqJson, context);
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
