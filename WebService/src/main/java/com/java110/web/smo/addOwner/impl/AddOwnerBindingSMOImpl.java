package com.java110.web.smo.addOwner.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceCodeAddOwnerBindingConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.web.smo.addOwner.IAddOwnerBindingSMO;
import org.springframework.web.client.RestTemplate;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addOwnerBindingSMOImpl")
public class AddOwnerBindingSMOImpl extends AbstractComponentSMO implements IAddOwnerBindingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        JSONArray infos = paramIn.getJSONArray("data");
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");

        if (infos.size() != 3) {
            throw new IllegalArgumentException("数据被篡改");
        }

        Assert.hasKeyByFlowData(infos, "viewFloorInfo", "floorId", "必填，未选择楼栋");
        Assert.hasKeyByFlowData(infos, "sellRoomSelectRoom", "roomId", "必填，未选择房屋");
        Assert.hasKeyByFlowData(infos, "addOwnerBinding", "name", "必填，名称不能为空");
        Assert.hasKeyByFlowData(infos, "addOwnerBinding", "sex", "必填，请选择性别");
        Assert.hasKeyByFlowData(infos, "addOwnerBinding", "age", "必填，请填写年龄");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.ADD_OWNER);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONArray infos = paramIn.getJSONArray("data");
        //JSONObject viewFloorInfo = getObj(infos, "viewFloorInfo");
        JSONObject sellRoomSelectRoom = getObj(infos, "sellRoomSelectRoom");
        JSONObject addOwner = getObj(infos, "addOwner");

        String communityId = paramIn.getString("communityId");
        addOwner.put("ownerTypeCd", "1001");
        addOwner.put("roomId", sellRoomSelectRoom.getString("roomId"));
        addOwner.put("communityId", communityId);
        addOwner.put("userId", pd.getUserId());
        addOwner.put("storeId", result.getStoreId());
        addOwner.put("state", "2002");
        responseEntity = this.callCenterService(restTemplate, pd, addOwner.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/owner.saveOwner",
                HttpMethod.POST);
        return responseEntity;
    }


    private JSONObject getObj(JSONArray infos, String flowComponent) {

        JSONObject serviceInfo = null;

        for (int infoIndex = 0; infoIndex < infos.size(); infoIndex++) {

            Assert.hasKeyAndValue(infos.getJSONObject(infoIndex), "flowComponent", "未包含服务流程组件名称");

            if (flowComponent.equals(infos.getJSONObject(infoIndex).getString("flowComponent"))) {
                serviceInfo = infos.getJSONObject(infoIndex);
                Assert.notNull(serviceInfo, "未包含服务信息");
                return serviceInfo;
            }
        }

        throw new IllegalArgumentException("未找到组件编码为【" + flowComponent + "】数据");
    }


    @Override
    public ResponseEntity<String> bindingAddOwner(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
