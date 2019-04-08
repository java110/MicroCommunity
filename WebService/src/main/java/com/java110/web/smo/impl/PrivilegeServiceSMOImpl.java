package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("privilegeServiceSMOImpl")
public class PrivilegeServiceSMOImpl extends BaseComponentSMO implements IPrivilegeServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 权限组
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listPrivilegeGroup(IPageData pd) {

        Assert.hasLength(pd.getUserId(),"用户未登录请先登录");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd,restTemplate);

        if(storeInfo.getStatusCode() != HttpStatus.OK){
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String  storeId = storeInfoObj.getString("storeId");
        String  storeTypeCd = storeInfoObj.getString("storeTypeCd");

        //根据商户ID查询 权限组信息


        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.store.privilegeGroup?storeId="+storeId +"&storeTypeCd="+storeTypeCd, HttpMethod.GET);
        if(privilegeGroup.getStatusCode() != HttpStatus.OK){
            return privilegeGroup;
        }

        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeGroupObj,"privilegeGroups","查询菜单未返回privilegeGroups节点");

        JSONArray privilegeGroups = privilegeGroupObj.getJSONArray("privilegeGroups");

        return new ResponseEntity<String>(privilegeGroups.toJSONString(),HttpStatus.OK);
    }

    /**
     * 查询权限
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> loadListPrivilege(IPageData pd) {
        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());
        Assert.jsonObjectHaveKey(privilegeInfoObj,"pgId","请求报文中未包含pgId 节点");

        String pgId = privilegeInfoObj.getString("pgId");

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.privilege.byPgId?pgId="+pgId , HttpMethod.GET);
        if(privilegeGroup.getStatusCode() != HttpStatus.OK){
            return privilegeGroup;
        }

        JSONObject privilegeObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeObj,"privileges","查询菜单未返回privileges节点");

        JSONArray privileges = privilegeObj.getJSONArray("privileges");

        return new ResponseEntity<String>(privileges.toJSONString(),HttpStatus.OK);

    }

    /**
     * 保存权限组
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> savePrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(),"用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj,"name","请求报文中未包含权限组名称 节点");
        Assert.jsonObjectHaveKey(privilegeInfoObj,"description","请求报文中未包含权限组描述 节点");

        Assert.hasLength(privilegeInfoObj.getString("name"),"请求报文中权限组名称不能为空");



        ResponseEntity<String> storeInfo = super.getStoreInfo(pd,restTemplate);

        if(storeInfo.getStatusCode() != HttpStatus.OK){
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String  storeId = storeInfoObj.getString("storeId");
        String  storeTypeCd = storeInfoObj.getString("storeTypeCd");
        privilegeInfoObj.put("storeId",storeId);
        privilegeInfoObj.put("storeTypeCd",storeTypeCd);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate,pd,privilegeInfoObj.toJSONString(),
                ServiceConstant.SERVICE_API_URL+"/api/save.privilegeGroup.info" , HttpMethod.POST);
        return privilegeGroup;
    }

    /**
     * 删除权限组
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> deletePrivilegeGroup(IPageData pd) {
        Assert.hasLength(pd.getUserId(),"用户未登录请先登录");

        JSONObject privilegeInfoObj = JSONObject.parseObject(pd.getReqData());

        Assert.jsonObjectHaveKey(privilegeInfoObj,"pgId","请求报文中未包含权限组ID 节点");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd,restTemplate);

        if(storeInfo.getStatusCode() != HttpStatus.OK){
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String  storeId = storeInfoObj.getString("storeId");
        String  storeTypeCd = storeInfoObj.getString("storeTypeCd");
        privilegeInfoObj.put("storeId",storeId);
        privilegeInfoObj.put("storeTypeCd",storeTypeCd);

        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate,pd,privilegeInfoObj.toJSONString(),
                ServiceConstant.SERVICE_API_URL+"/api/delete.privilegeGroup.info" , HttpMethod.POST);
        return privilegeGroup;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
