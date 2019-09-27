package com.java110.web.smo.addRoomBinding.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceCodeAddRoomBindingConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.web.smo.addRoomBinding.IAddRoomBindingBindingSMO;
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
@Service("addRoomBindingBindingSMOImpl")
public class AddRoomBindingBindingSMOImpl extends AbstractComponentSMO implements IAddRoomBindingBindingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        JSONArray infos = paramIn.getJSONArray("data");
        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyByFlowData(infos, "addRoomView", "roomNum", "必填，请填写房屋编号");
        Assert.hasKeyByFlowData(infos, "addRoomView", "layer", "必填，请填写房屋楼层");
        /*Assert.hasKeyByFlowData(infos, "addRoomView", "section", "必填，请填写房屋楼层");*/
        Assert.hasKeyByFlowData(infos, "addRoomView", "apartment", "必填，请选择房屋户型");
        Assert.hasKeyByFlowData(infos, "addRoomView", "builtUpArea", "必填，请填写房屋建筑面积");
       /* Assert.hasKeyByFlowData(infos, "addRoomView", "unitPrice", "必填，请填写房屋每平米单价");*/
        Assert.hasKeyByFlowData(infos, "addRoomView", "state", "必填，请选择房屋状态");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_ROOM);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/" + ServiceCodeAddRoomBindingConstant.BINDING_ADDROOMBINDING,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> bindingAddRoomBinding(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
