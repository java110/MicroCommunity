package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
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
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerRoomRelDto;
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
 * @Description TODO 退屋信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("exitRoomListener")
public class ExitRoomListener extends AbstractServiceApiDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(ExitRoomListener.class);

    @Autowired
    private IRoomBMO roomBMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EXIT_ROOM;
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

        //添加单元信息
        businesses.add(roomBMOImpl.exitRoom(paramObj, dataFlowContext));

        paramObj.put("state","2002");
        //修改房屋状态
        businesses.add(roomBMOImpl.updateRoom(paramObj,dataFlowContext));

        //删除费用信息
        businesses.add(roomBMOImpl.exitPropertyFee(paramObj, dataFlowContext));



        ResponseEntity<String> responseEntity = roomBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);

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
        Assert.jsonObjectHaveKey(paramIn, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(paramIn, "storeId", "请求报文中未包含storeId节点");

        JSONObject paramObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramObj.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(paramObj.getString("roomId"), "roomId不能为空");
        Assert.hasLength(paramObj.getString("storeId"), "storeId不能为空");
        //

        super.communityHasOwner(paramObj, communityInnerServiceSMOImpl);

        //校验物业费是否已经交清
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(paramObj.getString("communityId"));
        feeDto.setIncomeObjId(paramObj.getString("storeId"));
        feeDto.setPayerObjId(paramObj.getString("roomId"));
        feeDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_PROPERTY);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() == 0) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未包含物业费，数据异常");
        }

        if (feeDtos.size() > 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "包含多条物业费，数据异常");
        }

        FeeDto feeDtoData = feeDtos.get(0);

        Calendar calc = Calendar.getInstance();
        calc.setTime(feeDtoData.getEndTime());
        calc.add(Calendar.DATE, 30);
        if (calc.getTime().getTime() < DateUtil.getCurrentDate().getTime()) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "你还有物业费没有缴清，请先缴清欠款");
        }


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

    public IOwnerRoomRelInnerServiceSMO getOwnerRoomRelInnerServiceSMOImpl() {
        return ownerRoomRelInnerServiceSMOImpl;
    }

    public void setOwnerRoomRelInnerServiceSMOImpl(IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl) {
        this.ownerRoomRelInnerServiceSMOImpl = ownerRoomRelInnerServiceSMOImpl;
    }

    public IFeeInnerServiceSMO getFeeInnerServiceSMOImpl() {
        return feeInnerServiceSMOImpl;
    }

    public void setFeeInnerServiceSMOImpl(IFeeInnerServiceSMO feeInnerServiceSMOImpl) {
        this.feeInnerServiceSMOImpl = feeInnerServiceSMOImpl;
    }
}
