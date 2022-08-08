package com.java110.store.cmd.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import com.java110.intf.store.IResourceStoreTypeV1InnerServiceSMO;
import com.java110.po.resourceStoreType.ResourceStoreTypePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;

@Java110Cmd(serviceCode = "resourceStoreType.saveResourceStoreType")
public class SaveResourceStoreTypeCmd extends Cmd {

    @Autowired
    private IResourceStoreTypeV1InnerServiceSMO resourceStoreTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        //Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        ResourceStoreTypeDto resourceStoreTypeDto = new ResourceStoreTypeDto();
        resourceStoreTypeDto.setStoreId(reqJson.getString("storeId"));
        resourceStoreTypeDto.setName(reqJson.getString("name"));
        int flag = resourceStoreTypeV1InnerServiceSMOImpl.queryResourceStoreTypesCount(resourceStoreTypeDto);

        if (flag > 0) {
            throw new IllegalArgumentException("类型名称已存在");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        reqJson.put("rstId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rstId));
        reqJson.put("createTime", new Date());
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypePo.class);
        int flag = resourceStoreTypeV1InnerServiceSMOImpl.saveResourceStoreType(resourceStoreTypePo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存类型失败");
        }

    }
}
