package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.po.store.StorePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "update.store.info")
public class UpdateStoreInfoCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
        Assert.listOnlyOne(storeDtos, "未找到需要修改的商户或多条数据");

        JSONObject businessStore = new JSONObject();
        businessStore.putAll(BeanConvertUtil.beanCovertMap(storeDtos.get(0)));
        businessStore.putAll(reqJson);
        StorePo storePo = BeanConvertUtil.covertBean(businessStore, StorePo.class);
        int flag = storeV1InnerServiceSMOImpl.updateStore(storePo);
        if (flag < 1) {
            throw new CmdException("修改商户失败");
        }
    }
}
