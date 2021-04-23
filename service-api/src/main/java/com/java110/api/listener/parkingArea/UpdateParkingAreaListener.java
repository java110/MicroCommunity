package com.java110.api.listener.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingArea.IParkingAreaBMO;
import com.java110.api.bmo.parkingAreaAttr.IParkingAreaAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeParkingAreaConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存停车场侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateParkingAreaListener")
public class UpdateParkingAreaListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IParkingAreaBMO parkingAreaBMOImpl;

    @Autowired
    private IParkingAreaAttrBMO parkingAreaAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "paId", "停车场ID不能为空");
        Assert.hasKeyAndValue(reqJson, "num", "必填，请填写停车场编号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择停车场类型");

        Assert.judgeAttrValue(reqJson);

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        parkingAreaBMOImpl.updateParkingArea(reqJson, context);

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }

        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                parkingAreaAttrBMOImpl.addParkingAreaAttr(attr, context);
                continue;
            }
            parkingAreaAttrBMOImpl.updateParkingAreaAttr(attr, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeParkingAreaConstant.UPDATE_PARKINGAREA;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


}
