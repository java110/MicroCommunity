package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@Java110Cmd(serviceCode = "query.staff.byName")
public class QueryStaffByNameCmd extends Cmd {

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId节点");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject resultJson = JSONObject.parseObject("{\"total:\":10,\"datas\":[]}");
        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode(ServiceCodeConstant.SERVICE_CODE_QUERY_USER_BY_NAME);

        dataQuery.setRequestParams(reqJson);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }

        String useIds = getUserIds(responseEntity);
        if (StringUtil.isEmpty(useIds)) {
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONArray userInfos = getUserInfos(responseEntity);
        JSONObject paramIn = new JSONObject();
        paramIn.put("userIds", useIds);
        paramIn.put("storeId", reqJson.getString("storeId"));


        dataQuery = new DataQuery();
        dataQuery.setServiceCode(ServiceCodeConstant.SERVICE_CODE_QUERY_STOREUSER_BYUSERIDS);

        dataQuery.setRequestParams(paramIn);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        responseEntity = dataQuery.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }

        resultJson.put("datas", getStaffUsers(userInfos, responseEntity));
        responseEntity = new ResponseEntity<String>(resultJson.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }

    /**
     * 查询商户员工
     *
     * @param userInfos      用户信息
     * @param responseEntity 商户返回的用户ID信息
     * @return
     */
    private JSONArray getStaffUsers(JSONArray userInfos, ResponseEntity<String> responseEntity) {


        JSONObject storeUserInfo = null;
        JSONArray newStaffUsers = new JSONArray();
        JSONArray storeUsers = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("storeUsers");
        if (storeUsers == null || storeUsers.size() < 1) {
            return newStaffUsers;
        }

        for (int storeUserIndex = 0; storeUserIndex < storeUsers.size(); storeUserIndex++) {
            storeUserInfo = storeUsers.getJSONObject(storeUserIndex);

            for (int userIndex = 0; userIndex < userInfos.size(); userIndex++) {
                if (userInfos.getJSONObject(userIndex).getString("userId").equals(storeUserInfo.getString("userId"))) {
                    newStaffUsers.add(userInfos.getJSONObject(userIndex));
                }
            }
        }


        return newStaffUsers;
    }

    /**
     * 获取用ID
     * 如：
     * 123,456,567
     *
     * @param responseEntity
     * @return
     */
    private String getUserIds(ResponseEntity<String> responseEntity) {
        JSONObject userInfo = null;
        String userId = "";
        JSONArray resultInfo = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("users");
        if (resultInfo == null || resultInfo.size() < 1) {
            return userId;
        }

        for (int userIndex = 0; userIndex < resultInfo.size(); userIndex++) {
            userInfo = resultInfo.getJSONObject(userIndex);
            userId += (userInfo.getString("userId") + ",");
        }

        userId = userId.length() > 0 ? userId.substring(0, userId.lastIndexOf(",")) : userId;

        return userId;
    }


    /**
     * 获取用户
     *
     * @param responseEntity
     * @return
     */
    private JSONArray getUserInfos(ResponseEntity<String> responseEntity) {
        JSONArray resultInfo = JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("users");
        if (resultInfo == null || resultInfo.size() < 1) {
            return null;
        }

        return resultInfo;
    }


}
