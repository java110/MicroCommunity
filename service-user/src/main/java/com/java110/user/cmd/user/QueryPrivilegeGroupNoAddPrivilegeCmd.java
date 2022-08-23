package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.language.Language;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "query.privilegeGroup.noAddPrivilege")
public class QueryPrivilegeGroupNoAddPrivilegeCmd extends Cmd {

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

            if(StoreDto.STORE_TYPE_ADMIN.equals(storeTypeCd)){
                reqJson.remove("communityId");
            }
        }else{
            storeTypeCd = reqJson.getString("storeTypeCd");
        }

        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.privilegeGroup.noAddPrivilege");
        JSONObject param = new JSONObject();
        //storeId=" + storeId + "&storeTypeCd=" + storeTypeCd + "&pgId=" + pgId + "&pName=" + pName+"&communityId="+communityId
        param.put("storeId", storeId);
        param.put("storeTypeCd", storeTypeCd);
        param.put("pgId", reqJson.getString("pgId"));
        param.put("pName",  reqJson.getString("pName"));
        param.put("communityId",  reqJson.getString("communityId"));
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> privileges = dataQuery.getResponseEntity();
        if (privileges.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(privileges);
            return ;
        }

        JSONObject resultObj = JSONObject.parseObject(privileges.getBody().toString());

        JSONArray privilegeArrays = resultObj.getJSONArray("privileges");
        JSONObject privilegeObj = null;

        JSONArray tmpPrivilegeArrays = new JSONArray();

        for (int privilegeIndex = 0; privilegeIndex < privilegeArrays.size(); privilegeIndex++) {
            privilegeObj = privilegeArrays.getJSONObject(privilegeIndex);
            hasSameData(privilegeObj, tmpPrivilegeArrays);
        }


        Object lang = context.getReqHeaders().get(CommonConstant.JAVA110_LANG);
        if (!StringUtil.isNullOrNone(lang) && !CommonConstant.LANG_ZH_CN.equals(lang)) {
            Language language = ApplicationContextFactory.getBean(lang.toString(), Language.class);
            if (language != null) {
                tmpPrivilegeArrays = language.getPrivilegeMenuDto(tmpPrivilegeArrays);
            }

        }


        context.setResponseEntity(new ResponseEntity<String>(tmpPrivilegeArrays.toJSONString(), HttpStatus.OK));
    }

    private void hasSameData(JSONObject privilegeObj, JSONArray tmpPrivilegeArrays) {
        JSONObject tmpPrivilegeObj = null;
        for (int tmpPrivilegeIndex = 0; tmpPrivilegeIndex < tmpPrivilegeArrays.size(); tmpPrivilegeIndex++) {
            tmpPrivilegeObj = tmpPrivilegeArrays.getJSONObject(tmpPrivilegeIndex);
            if (privilegeObj.getString("pId").equals(tmpPrivilegeObj.getString("pId"))) {
                if (!StringUtil.isEmpty(privilegeObj.getString("pgId"))) {
                    tmpPrivilegeArrays.remove(tmpPrivilegeIndex);
                    tmpPrivilegeArrays.add(privilegeObj);
                }
                return;
            }
        }
        tmpPrivilegeArrays.add(privilegeObj);
    }
}
