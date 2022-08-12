package com.java110.user.cmd.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.privilegeRel.PrivilegeRelDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeRelV1InnerServiceSMO;
import com.java110.po.privilegeRel.PrivilegeRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "delete.privilege.PrivilegeGroup")
public class DeletePrivilegePrivilegeGroupCmd extends Cmd {

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
        PrivilegeRelDto privilegeRelDto = null;
        int flag = 0;
        List<PrivilegeRelDto> privilegeRelDtos = null;
        for (int pIdIndex = 0; pIdIndex < pIds.size(); pIdIndex++) {
            tmpPId = pIds.getJSONObject(pIdIndex);
            reqJson.put("pId", tmpPId.getString("pId"));

            privilegeRelDto = BeanConvertUtil.covertBean(reqJson, PrivilegeRelDto.class);
            privilegeRelDtos = privilegeRelV1InnerServiceSMOImpl.queryPrivilegeRels(privilegeRelDto);

            if(privilegeRelDtos == null || privilegeRelDtos.size()<1){
                continue;
            }
            tmpPrivilegePo = new PrivilegeRelPo();
            tmpPrivilegePo.setRelId(privilegeRelDtos.get(0).getRelId());
            flag = privilegeRelV1InnerServiceSMOImpl.deletePrivilegeRel(tmpPrivilegePo);
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
