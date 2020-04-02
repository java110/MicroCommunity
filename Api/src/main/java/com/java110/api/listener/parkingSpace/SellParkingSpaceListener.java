package com.java110.api.listener.parkingSpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.utils.constant.*;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SaveParkingSpaceListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("sellParkingSpaceListener")
public class SellParkingSpaceListener extends AbstractServiceApiDataFlowListener {


    private static Logger logger = LoggerFactory.getLogger(SellParkingSpaceListener.class);

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SELL_PARKING_SPACE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        String feeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId);
        paramObj.put("feeId", feeId);

        //添加小区楼
        businesses.add(parkingSpaceBMOImpl.sellParkingSpace(paramObj));

        businesses.add(parkingSpaceBMOImpl.modifySellParkingSpaceState(paramObj));

        //计算 费用信息
        parkingSpaceBMOImpl.computeFeeInfo(paramObj);
        //添加物业费用信息
        businesses.add(parkingSpaceBMOImpl.addParkingSpaceFee(paramObj, dataFlowContext));

        businesses.add(parkingSpaceBMOImpl.addFeeDetail(paramObj, dataFlowContext));




        ResponseEntity<String> responseEntity = parkingSpaceBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 数据校验
     * <p>
     * name:'',
     * age:'',
     * link:'',
     * sex:'',
     * remark:''
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(paramIn, "ownerId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(paramIn, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(paramIn, "carBrand", "请求报文中未包含carBrand");
        Assert.jsonObjectHaveKey(paramIn, "carType", "请求报文中未包含carType");
        Assert.jsonObjectHaveKey(paramIn, "carColor", "未包含carColor");
        Assert.jsonObjectHaveKey(paramIn, "psId", "未包含psId");
        Assert.jsonObjectHaveKey(paramIn, "storeId", "未包含storeId");
        Assert.jsonObjectHaveKey(paramIn, "receivedAmount", "未包含receivedAmount");
        Assert.jsonObjectHaveKey(paramIn, "sellOrHire", "未包含sellOrHire");

        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramInObj.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(paramInObj.getString("psId"), "psId不能为空");
        Assert.isMoney(paramInObj.getString("receivedAmount"), "不是有效的实收金额");

        if (!"H".equals(paramInObj.getString("sellOrHire"))
                && !"S".equals(paramInObj.getString("sellOrHire"))) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中sellOrFire值错误 ，出售为S 出租为H");
        }
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
