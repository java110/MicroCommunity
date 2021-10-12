package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.bmo.tempCarFeeConfig.ITempCarFeeConfigBMO;
import com.java110.api.bmo.tempCarFeeConfigAttr.ITempCarFeeConfigAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存临时车收费标准侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateTempCarFeeConfigListener")
public class UpdateTempCarFeeConfigListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITempCarFeeConfigBMO tempCarFeeConfigBMOImpl;

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrBMO tempCarFeeConfigAttrBMOImpl;

    @Autowired
    private IFeeBMO feeBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setConfigId(reqJson.getString("configId"));
        tempCarFeeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);

        Assert.listOnlyOne(tempCarFeeConfigDtos, "临时车收费标准不存在");

        tempCarFeeConfigBMOImpl.updateTempCarFeeConfig(reqJson, context);

        //补费用项数据
        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setCommunityId(reqJson.getString("communityId"));
        payFeeConfigPo.setConfigId(tempCarFeeConfigDtos.get(0).getFeeConfigId());
        payFeeConfigPo.setEndTime(reqJson.getString("endTime"));
        payFeeConfigPo.setStartTime(reqJson.getString("startTime"));
        payFeeConfigPo.setFeeName(reqJson.getString("feeName"));
        feeBMOImpl.updateFeeConfig(BeanConvertUtil.beanCovertJson(payFeeConfigPo), context);


        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("ruleId", reqJson.getString("ruleId"));
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                tempCarFeeConfigAttrBMOImpl.addTempCarFeeConfigAttr(attr, context);
                continue;
            }
            tempCarFeeConfigAttrBMOImpl.updateTempCarFeeConfigAttr(attr, context);
        }

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigConstant.UPDATE_TEMPCARFEECONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
