package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IGetCommunityStoreInfoSMO;
import com.java110.api.smo.IPrivilegeServiceSMO;
import com.java110.core.language.Language;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("privilegeServiceSMOImpl")
public class PrivilegeServiceSMOImpl extends DefaultAbstractComponentSMO implements IPrivilegeServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IGetCommunityStoreInfoSMO getCommunityStoreInfoSMOImpl;

    /**
     * 查询 权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listPrivilegeGroup(IPageData pd) {

        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        String storeTypeCd = storeInfoObj.getString("storeTypeCd");

        //根据商户ID查询 权限组信息


        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, "",
                "query.store.privilegeGroup?storeId=" + storeId + "&storeTypeCd=" + storeTypeCd, HttpMethod.GET);
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            return privilegeGroup;
        }

        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeGroupObj, "privilegeGroups", "查询菜单未返回privilegeGroups节点");

        JSONArray privilegeGroups = privilegeGroupObj.getJSONArray("privilegeGroups");

        return new ResponseEntity<String>(privilegeGroups.toJSONString(), HttpStatus.OK);
    }

    /**
     * 查询权限
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> loadListPrivilege(IPageData pd) {
        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());
        Assert.jsonObjectHaveKey(privilegeInfoObj, "pgId", "请求报文中未包含pgId 节点");

        String pgId = privilegeInfoObj.getString("pgId");
        String name = privilegeInfoObj.getString("name");

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, "",
                "query.privilege.byPgId?pgId=" + pgId + "&name=" + name, HttpMethod.GET);
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            return privilegeGroup;
        }

        JSONObject privilegeObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeObj, "privileges", "查询菜单未返回privileges节点");

        JSONArray privileges = privilegeObj.getJSONArray("privileges");

        return new ResponseEntity<String>(privileges.toJSONString(), HttpStatus.OK);

    }

    /**
     * 保存权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> savePrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "name", "请求报文中未包含权限组名称 节点");
        Assert.jsonObjectHaveKey(privilegeInfoObj, "description", "请求报文中未包含权限组描述 节点");

        Assert.hasLength(privilegeInfoObj.getString("name"), "请求报文中权限组名称不能为空");


        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        String storeTypeCd = storeInfoObj.getString("storeTypeCd");
        privilegeInfoObj.put("storeId", storeId);
        privilegeInfoObj.put("storeTypeCd", storeTypeCd);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, privilegeInfoObj.toJSONString(),
                "save.privilegeGroup.info", HttpMethod.POST);
        return privilegeGroup;
    }

    /**
     * 保存权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> editPrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "name", "请求报文中未包含权限组名称 节点");
        Assert.hasKeyAndValue(privilegeInfoObj, "pgId", "请求报文中未包含权限组ID 节点");
        Assert.jsonObjectHaveKey(privilegeInfoObj, "description", "请求报文中未包含权限组描述 节点");

        Assert.hasLength(privilegeInfoObj.getString("name"), "请求报文中权限组名称不能为空");


        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        String storeTypeCd = storeInfoObj.getString("storeTypeCd");
        privilegeInfoObj.put("storeId", storeId);
        privilegeInfoObj.put("storeTypeCd", storeTypeCd);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, privilegeInfoObj.toJSONString(),
                "edit.privilegeGroup.info", HttpMethod.POST);
        return privilegeGroup;
    }

    /**
     * 删除权限组
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> deletePrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "pgId", "请求报文中未包含权限组ID 节点");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        String storeTypeCd = storeInfoObj.getString("storeTypeCd");
        privilegeInfoObj.put("storeId", storeId);
        privilegeInfoObj.put("storeTypeCd", storeTypeCd);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, privilegeInfoObj.toJSONString(),
                "delete.privilegeGroup.info", HttpMethod.POST);
        return privilegeGroup;
    }

    @Override
    public ResponseEntity<String> listNoAddPrivilege(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "pgId", "请求报文中未包含权限组ID 节点");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        String storeTypeCd = storeInfoObj.getString("storeTypeCd");
        String pgId = privilegeInfoObj.getString("pgId");
        String pName = privilegeInfoObj.getString("pName");
        String communityId = privilegeInfoObj.getString("communityId");

        ResponseEntity<String> privileges = super.callCenterService(restTemplate, pd, "",
                "query.privilegeGroup.noAddPrivilege?storeId=" + storeId + "&storeTypeCd=" + storeTypeCd + "&pgId=" + pgId + "&pName=" + pName+"&communityId="+communityId, HttpMethod.GET);

        if (privileges.getStatusCode() != HttpStatus.OK) {
            return privileges;
        }


        JSONObject resultObj = JSONObject.parseObject(privileges.getBody().toString());

        JSONArray privilegeArrays = resultObj.getJSONArray("privileges");
        JSONObject privilegeObj = null;

        JSONArray tmpPrivilegeArrays = new JSONArray();

        for (int privilegeIndex = 0; privilegeIndex < privilegeArrays.size(); privilegeIndex++) {
            privilegeObj = privilegeArrays.getJSONObject(privilegeIndex);
            hasSameData(privilegeObj, tmpPrivilegeArrays);
        }


        Object lang = pd.getHeaders().get(CommonConstant.JAVA110_LANG);
        if (!StringUtil.isNullOrNone(lang) && !CommonConstant.LANG_ZH_CN.equals(lang)) {
            Language language = ApplicationContextFactory.getBean(lang.toString(), Language.class);
            if (language != null) {
                tmpPrivilegeArrays = language.getPrivilegeMenuDto(tmpPrivilegeArrays);
            }

        }
        return new ResponseEntity<String>(tmpPrivilegeArrays.toJSONString(), HttpStatus.OK);
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

    @Override
    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "pgId", "请求报文中未包含权限组ID 节点");
        //Assert.jsonObjectHaveKey(privilegeInfoObj,"pId","请求报文中未包含权限ID 节点");
        if (!privilegeInfoObj.containsKey("pIds") || privilegeInfoObj.getJSONArray("pIds").size() < 1) {
            throw new IllegalArgumentException("请求报文中未包含权限");
        }

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        privilegeInfoObj.put("storeId", storeId);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, privilegeInfoObj.toJSONString(),
                "add.privilege.PrivilegeGroup", HttpMethod.POST);
        return privilegeGroup;
    }

    /**
     * 删除权限
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> deletePrivilegeFromPrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj, "pgId", "请求报文中未包含权限组ID 节点");
        //Assert.jsonObjectHaveKey(privilegeInfoObj, "pId", "请求报文中未包含权限ID 节点");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        privilegeInfoObj.put("storeId", storeId);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, privilegeInfoObj.toJSONString(),
                "delete.privilege.PrivilegeGroup", HttpMethod.POST);
        return privilegeGroup;
    }

    /**
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listStaffPrivileges(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());
        //Assert.jsonObjectHaveKey(privilegeInfoObj, "staffId", "请求报文中未包含员工ID 节点");

        if (!privilegeInfoObj.containsKey("staffId")) {
            privilegeInfoObj.put("staffId", pd.getUserId());
        }

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd, restTemplate);

        if (storeInfo.getStatusCode() != HttpStatus.OK) {
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String storeId = storeInfoObj.getString("storeId");
        privilegeInfoObj.put("storeId", storeId);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate, pd, "",
                "query.user.privilege?userId=" + privilegeInfoObj.getString("staffId") + "&domain=" + storeInfoObj.getString("storeTypeCd"), HttpMethod.GET);

//        ResultVo resultVo = getCommunityStoreInfoSMOImpl.getUserPrivileges(pd, privilegeInfoObj.getString("staffId"), storeInfoObj.getString("storeTypeCd"), restTemplate);
//        ResponseEntity<String> privilegeGroup = new ResponseEntity<>(resultVo.getMsg(), resultVo.getCode() == ResultVo.CODE_OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            return privilegeGroup;
        }
        JSONObject resultObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        JSONArray privileges = resultObj.getJSONArray("privileges");

        JSONArray tmpPrivilegeArrays = new JSONArray();

        JSONObject privilegeObj = null;
        for (int privilegeIndex = 0; privilegeIndex < privileges.size(); privilegeIndex++) {
            privilegeObj = privileges.getJSONObject(privilegeIndex);
            hasSameData(privilegeObj, tmpPrivilegeArrays);
        }

        JSONObject resObj = new JSONObject();
        resObj.put("datas", privileges);

        return new ResponseEntity<String>(resObj.toJSONString(), HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
