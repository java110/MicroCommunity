package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

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
public class ExitRoomListener extends AbstractServiceApiPlusListener {
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
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含ownerId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");


        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
        Assert.hasLength(reqJson.getString("storeId"), "storeId不能为空");
        //

        super.communityHasOwner(reqJson, communityInnerServiceSMOImpl);

        //校验物业费是否已经交清
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setIncomeObjId(reqJson.getString("storeId"));
        feeDto.setPayerObjId(reqJson.getString("roomId"));
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

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
//添加单元信息
        roomBMOImpl.exitRoom(reqJson, context);

        reqJson.put("state", "2002");
        //修改房屋状态
        roomBMOImpl.updateRoom(reqJson, context);

        //删除费用信息
        roomBMOImpl.exitPropertyFee(reqJson, context);

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
