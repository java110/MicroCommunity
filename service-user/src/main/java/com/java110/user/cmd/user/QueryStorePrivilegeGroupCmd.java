package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "query.store.privilegeGroup")
public class QueryStorePrivilegeGroupCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");

        if(StringUtil.isEmpty(userId)){
            userId = reqJson.getString("userId");
        }
        String storeTypeCd = "";
        if(!reqJson.containsKey("storeTypeCd") || StringUtil.isEmpty(reqJson.getString("storeTypeCd"))) {

            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeId);
            storeDto.setPage(1);
            storeDto.setRow(1);
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

            Assert.listOnlyOne(storeDtos, "商户不存在");
            storeTypeCd = storeDtos.get(0).getStoreTypeCd();
        }else{
            storeTypeCd = reqJson.getString("storeTypeCd");
        }

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.store.privilegeGroup");
        JSONObject param = new JSONObject();
        param.put("storeId", storeId);
        param.put("storeTypeCd", storeTypeCd);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> privilegeGroup = dataQuery.getResponseEntity();
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(privilegeGroup);
            return ;
        }

        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeGroupObj, "privilegeGroups", "查询菜单未返回privilegeGroups节点");

        JSONArray privilegeGroups = privilegeGroupObj.getJSONArray("privilegeGroups");

        context.setResponseEntity(new ResponseEntity<String>(privilegeGroups.toJSONString(), HttpStatus.OK));
    }
}
