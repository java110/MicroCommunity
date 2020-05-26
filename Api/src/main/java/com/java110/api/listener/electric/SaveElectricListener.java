package com.java110.api.listener.electric;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.po.ElectricPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import org.springframework.http.HttpMethod;

import java.util.Date;

/**
 * @ClassName SaveElectricListener
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 10:04
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class SaveElectricListener extends AbstractServiceApiPlusListener {

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.jsonObjectHaveKey(reqJson, "demoValue", "请求中未包含demoValue信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ElectricPo electricPo = new ElectricPo();

        electricPo.setId("123");
        electricPo.setStartTime(new Date());
        super.insert(context,electricPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT);


    }

    @Override
    public String getServiceCode() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }


}
