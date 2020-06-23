package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveCommunityListener")
public class SaveCommunityListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICommunityBMO communityBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "nearbyLandmarks", "必填，请填写小区附近地标");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        communityBMOImpl.addCommunity(reqJson, context);
        communityBMOImpl.addCommunityMembers(reqJson, context);
        //产生物业费配置信息
        communityBMOImpl.addFeeConfigProperty(reqJson, context);
//        communityBMOImpl.addFeeConfigParkingSpace(reqJson, context); // 地上出售
//       communityBMOImpl.addFeeConfigParkingSpaceUpSell(reqJson, context); // 地上出售
//        communityBMOImpl.addFeeConfigParkingSpaceDownSell(reqJson, context); // 地下出售
//        communityBMOImpl.addFeeConfigParkingSpaceUpHire(reqJson, context);//地上出租
//        communityBMOImpl.addFeeConfigParkingSpaceDownHire(reqJson, context);//地下出租
        communityBMOImpl.addFeeConfigParkingSpaceTemp(reqJson, context);//地下出租

    }


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_COMMUNITY;
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
