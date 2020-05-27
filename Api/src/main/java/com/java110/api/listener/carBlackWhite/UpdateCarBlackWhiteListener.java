package com.java110.api.listener.carBlackWhite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.carBlackWhite.ICarBlackWhiteBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeCarBlackWhiteConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存黑白名单侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateCarBlackWhiteListener")
public class UpdateCarBlackWhiteListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICarBlackWhiteBMO carBlackWhiteBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "bwId", "黑白名单ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        Assert.hasKeyAndValue(reqJson, "blackWhite", "必填，请填写名单类型");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

       carBlackWhiteBMOImpl.updateCarBlackWhite(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeCarBlackWhiteConstant.UPDATE_CARBLACKWHITE;
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
