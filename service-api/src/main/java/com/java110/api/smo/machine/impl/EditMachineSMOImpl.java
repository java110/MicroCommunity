package com.java110.api.smo.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.machine.IEditMachineSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加设备服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtMachineSMOImpl")
public class EditMachineSMOImpl extends DefaultAbstractComponentSMO implements IEditMachineSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "machineId", "设备ID不能为空");
        Assert.hasKeyAndValue(paramIn, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(paramIn, "machineVersion", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(paramIn, "machineName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(paramIn, "machineTypeCd", "必填，请选择设备类型");
        Assert.hasKeyAndValue(paramIn, "direction", "必填，请选择设备方向");
        Assert.hasKeyAndValue(paramIn, "authCode", "必填，请填写鉴权编码");
        Assert.hasKeyAndValue(paramIn, "locationTypeCd", "必填，请选择位置类型");
        Assert.hasKeyAndValue(paramIn, "locationObjId", "必填，请填写位置对象ID");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_MACHINE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "machine.updateMachine",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateMachine(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
