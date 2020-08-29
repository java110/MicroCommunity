package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName SaveParkingSpaceListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("saveOwnerCarListener")
public class SaveOwnerCarListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(SaveOwnerCarListener.class);

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_OWNER_CAR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    /**
     * {"communityId":"7020181217000001",
     * "data":[
     * {
     * "flowComponent":"viewSelectParkingSpace","parkingSpaceFlag":"","num":"lbwnb","area":"11.00","state":"F",
     * "stateName":"空闲 ","remark":"","areaNum":"3","psId":"792020082657940123","typeCd":"1001"},
     * {"flowComponent":"viewOwnerInfo","viewOwnerFlag":"","ownerId":"772020082849180061","name":"王鹏飞","age":"28",
     * "sex":"0","userName":"wuxw","remark":"","idCard":"340803199211182134","link":"17721036947","ownerPhoto":"/img/noPhoto.jpg",
     * "showCallBackButton":"false"},
     * {"flowComponent":"addCar","carNum":"青AGK916","carBrand":"传祺","carType":"9901","carColor":"白色","carRemark":"",
     * "startTime":"2020-08-29 14:55:04","endTime":"2021-08-29 14:55:04","carNumType":"H","index":2}
     * ]}
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(reqJson, "carBrand", "请求报文中未包含carBrand");
        Assert.jsonObjectHaveKey(reqJson, "carType", "请求报文中未包含carType");
        Assert.jsonObjectHaveKey(reqJson, "carColor", "未包含carColor");
        Assert.jsonObjectHaveKey(reqJson, "psId", "未包含psId");
        Assert.jsonObjectHaveKey(reqJson, "storeId", "未包含storeId");
        Assert.jsonObjectHaveKey(reqJson, "carNumType", "未包含carNumType");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("psId"), "psId不能为空");

        if (!"H".equals(reqJson.getString("carNumType"))
                && !"S".equals(reqJson.getString("carNumType"))) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中sellOrFire值错误 ，出售为S 出租为H");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //添加小区楼
        parkingSpaceBMOImpl.sellParkingSpace(reqJson, context);
        parkingSpaceBMOImpl.modifySellParkingSpaceState(reqJson, context);
    }


    @Override
    public int getOrder() {
        return 0;
    }

    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }

    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }
}
