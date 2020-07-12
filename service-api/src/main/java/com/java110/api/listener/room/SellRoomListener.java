package com.java110.api.listener.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.room.IRoomBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName SaveUnitListener
 * @Description TODO 售卖房屋信息
 * @Author wuxw
 * @Date 2019/5/3 11:54
 * @Version 1.0
 * add by wuxw 2019/5/3
 **/
@Java110Listener("sellRoomListener")
public class SellRoomListener extends AbstractServiceApiPlusListener {
    private static Logger logger = LoggerFactory.getLogger(SellRoomListener.class);

    @Autowired
    private IRoomBMO roomBMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SELL_ROOM;
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
        Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
        Assert.hasLength(reqJson.getString("state"), "state不能为空");

        super.communityHasOwner(reqJson, communityInnerServiceSMOImpl);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //添加单元信息
        roomBMOImpl.sellRoom(reqJson, context);

        //更新房屋信息为售出
        roomBMOImpl.updateShellRoom(reqJson, context);

        //添加物业费用信息
        roomBMOImpl.addPropertyFee(reqJson, context);
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

    public IUnitInnerServiceSMO getUnitInnerServiceSMOImpl() {
        return unitInnerServiceSMOImpl;
    }

    public void setUnitInnerServiceSMOImpl(IUnitInnerServiceSMO unitInnerServiceSMOImpl) {
        this.unitInnerServiceSMOImpl = unitInnerServiceSMOImpl;
    }
}
