package com.java110.api.smo.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.machine.IEditMachineTranslateSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加设备同步服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtMachineTranslateSMOImpl")
public class EditMachineTranslateSMOImpl extends DefaultAbstractComponentSMO implements IEditMachineTranslateSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "machineTranslateId", "同步ID不能为空");
        Assert.hasKeyAndValue(paramIn, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(paramIn, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(paramIn, "typeCd", "必填，请选择对象类型");
        Assert.hasKeyAndValue(paramIn, "objName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(paramIn, "objId", "必填，请填写对象Id");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请选择状态");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_MACHINE_TRANSLATE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "machineTranslate.updateMachineTranslate",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateMachineTranslate(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
