package com.java110.user.cmd.privilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.privilegeGroup.PrivilegeGroupDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeGroupV1InnerServiceSMO;
import com.java110.po.privilegeGroup.PrivilegeGroupPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "save.privilegeGroup.info")
public class SavePrivilegeGroupInfoCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeGroupV1InnerServiceSMO privilegeGroupV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name节点");
        String storeId = context.getReqHeaders().get("store-id");
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setPage(1);
        storeDto.setRow(1);
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        Assert.listOnlyOne(storeDtos, "商户不存在");
        reqJson.put("storeId", storeDtos.get(0).getStoreId());
        reqJson.put("storeTypeCd", storeDtos.get(0).getStoreTypeCd());
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        reqJson.put("pgId", GenerateCodeFactory.getPgId());
        PrivilegeGroupPo privilegeGroupPo = BeanConvertUtil.covertBean(reqJson, PrivilegeGroupPo.class);
        privilegeGroupPo.setDomain(reqJson.getString("storeTypeCd"));
        privilegeGroupPo.setRoleType(PrivilegeGroupDto.ROLE_TYPE_USER);
        int flag = privilegeGroupV1InnerServiceSMOImpl.savePrivilegeGroup(privilegeGroupPo);
        if (flag < 1) {
            throw new CmdException("保存失败");
        }

    }
}
