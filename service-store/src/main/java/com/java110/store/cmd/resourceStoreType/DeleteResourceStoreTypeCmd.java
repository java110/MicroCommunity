package com.java110.store.cmd.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.intf.store.IResourceStoreTypeV1InnerServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreType.ResourceStoreTypePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStoreType.deleteResourceStoreType")
public class DeleteResourceStoreTypeCmd extends Cmd {

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Autowired
    private IResourceStoreTypeV1InnerServiceSMO resourceStoreTypeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "rstId", "rstId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStorePo resourceStorePo = new ResourceStorePo();
        resourceStorePo.setRstId(reqJson.getString("rstId"));
        //根据类型id物品信息表
        List<ResourceStorePo> resourceStores = resourceStoreServiceSMOImpl.getResourceStores(resourceStorePo);
        Assert.listIsNull(resourceStores, "物品信息中该类型正在使用，不能删除！");
        ResourceStoreTypePo resourceStoreTypePo = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypePo.class);

        int flag = resourceStoreTypeV1InnerServiceSMOImpl.deleteResourceStoreType(resourceStoreTypePo);

        if (flag < 1) {
            throw new CmdException("删除失败");
        }
    }
}
