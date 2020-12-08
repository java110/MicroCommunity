package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.attrValue.IDeleteAttrValueBMO;
import com.java110.common.bmo.attrValue.IGetAttrValueBMO;
import com.java110.common.bmo.attrValue.ISaveAttrValueBMO;
import com.java110.common.bmo.attrValue.IUpdateAttrValueBMO;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.po.attrValue.AttrValuePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/machine")
public class MachineApi {

    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/openDoor
     * @path /app/machine/openDoor
     */
    @RequestMapping(value = "/openDoor", method = RequestMethod.POST)
    public ResponseEntity<String> saveAttrValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
        Assert.hasKeyAndValue(reqJson, "userType", "请求报文中未包含用户类型");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");

        return machineOpenDoorBMOImpl.openDoor(reqJson);
    }


}