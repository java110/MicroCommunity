package com.java110.user.cmd.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.privilegeGroup.PrivilegeGroupDto;
import com.java110.dto.privilegeRel.PrivilegeRelDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeGroupV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeRelV1InnerServiceSMO;
import com.java110.po.privilegeGroup.PrivilegeGroupPo;
import com.java110.po.privilegeRel.PrivilegeRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "add.privilege.PrivilegeGroup")
public class AddPrivilegePrivilegeGroupCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeRelV1InnerServiceSMO privilegeRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "pgId", "请求报文中未包含角色");

        if (!reqJson.containsKey("pIds")) {
            throw new CmdException("未包含权限");
        }

        JSONArray pIds = reqJson.getJSONArray("pIds");

        if (pIds.size() < 1) {
            throw new CmdException("未包含权限");
        }


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


        JSONArray pIds = reqJson.getJSONArray("pIds");
        int errorCount = 0;
        JSONObject tmpPId = null;
        PrivilegeRelPo tmpPrivilegePo = null;
        int flag = 0;
        for (int pIdIndex = 0; pIdIndex < pIds.size(); pIdIndex++) {
            tmpPId = pIds.getJSONObject(pIdIndex);
            reqJson.put("pId", tmpPId.getString("pId"));
            tmpPrivilegePo = BeanConvertUtil.covertBean(reqJson, PrivilegeRelPo.class);
            //tmpPrivilegePo.setRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            flag = privilegeRelV1InnerServiceSMOImpl.savePrivilegeRel(tmpPrivilegePo);
            if (flag < 1) {
                errorCount++;
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("success", pIds.size() - errorCount);
        paramOut.put("error", errorCount);


        context.setResponseEntity(ResultVo.createResponseEntity(paramOut.toJSONString()));
    }
}
