package com.java110.store.cmd.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.store.IResourceStoreTypeV1InnerServiceSMO;
import com.java110.po.resourceStoreType.ResourceStoreTypePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "resourceStoreType.updateResourceStoreType")
public class UpdateResourceStoreTypeCmd extends Cmd {

    @Autowired
    private IResourceStoreTypeV1InnerServiceSMO resourceStoreTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "rstId", "请求报文中未包含rstId");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypePo.class);
        int flag = resourceStoreTypeV1InnerServiceSMOImpl.updateResourceStoreType(resourceStoreTypePo);
        if (flag < 1) {
            throw new IllegalArgumentException("保存类型失败");
        }
    }
}
