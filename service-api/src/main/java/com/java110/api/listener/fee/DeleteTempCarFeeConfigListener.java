package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.tempCarFeeConfig.ITempCarFeeConfigBMO;
import com.java110.api.bmo.tempCarFeeConfigAttr.ITempCarFeeConfigAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteTempCarFeeConfigListener")
public class DeleteTempCarFeeConfigListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITempCarFeeConfigBMO tempCarFeeConfigBMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrBMO tempCarFeeConfigAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        tempCarFeeConfigBMOImpl.deleteTempCarFeeConfig(reqJson, context);

        JSONArray attrs = reqJson.getJSONArray("tempCarFeeConfigAttrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("ruleId", reqJson.getString("ruleId"));
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                continue;
            }
            tempCarFeeConfigAttrBMOImpl.deleteTempCarFeeConfigAttr(attr, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigConstant.DELETE_TEMPCARFEECONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
