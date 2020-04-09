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
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
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
import java.util.List;

/**
 * @ClassName SaveUnitListener
 * @Description TODO 退停车位信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("exitParkingSpaceListener")
public class ExitParkingSpaceListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(ExitParkingSpaceListener.class);
    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EXIT_PARKING_SPACE;
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

        validateHasSellParkingSpace(paramObj);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        //退出 业主和停车位之间关系
        businesses.add(parkingSpaceBMOImpl.exitParkingSpace(paramObj, dataFlowContext));

        //将车位状态改为空闲状态
        businesses.add(parkingSpaceBMOImpl.modifyParkingSpaceState(paramObj));


        //删除费用信息
        businesses.add(parkingSpaceBMOImpl.exitParkingSpaceFee(paramObj, dataFlowContext));


        ResponseEntity<String> responseEntity = parkingSpaceBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "ownerId", "请求报文中未包含ownerId节点");
        Assert.jsonObjectHaveKey(paramIn, "psId", "请求报文中未包含psId节点");
        Assert.jsonObjectHaveKey(paramIn, "storeId", "请求报文中未包含storeId节点");

        JSONObject paramObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramObj.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(paramObj.getString("psId"), "psId不能为空");
        Assert.hasLength(paramObj.getString("storeId"), "storeId不能为空");
        //

        super.communityHasOwner(paramObj, communityInnerServiceSMOImpl);


    }


    /**
     * 校验是否存在实例数据
     *
     * @param paramObj 请参数 转成json对象
     */
    private void validateHasSellParkingSpace(JSONObject paramObj) {

        //校验物业费是否已经交清
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setPsId(paramObj.getString("psId"));
        ownerCarDto.setOwnerId(paramObj.getString("ownerId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "该停车位没被出售过（出租过） 或 被多次出售过（出租过）");
        }

        ownerCarDto = ownerCarDtos.get(0);

        Calendar calc = Calendar.getInstance();
        calc.setTime(ownerCarDto.getCreateTime());
        calc.add(Calendar.DATE, 7);

        if (calc.getTime().getTime() < DateUtil.getCurrentDate().getTime()) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "只有在7天内才能退款，现已经过期，无法退款");
        }

        paramObj.put("ownerCarDto", ownerCarDto);
    }


    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IOwnerCarInnerServiceSMO getOwnerCarInnerServiceSMOImpl() {
        return ownerCarInnerServiceSMOImpl;
    }

    public void setOwnerCarInnerServiceSMOImpl(IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl) {
        this.ownerCarInnerServiceSMOImpl = ownerCarInnerServiceSMOImpl;
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
}
