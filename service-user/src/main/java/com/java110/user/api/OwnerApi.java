package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.user.bmo.owner.IQueryTenants;
import com.java110.user.bmo.owner.IVisitorRecord;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ownerApi")
public class OwnerApi {

    @Autowired
    private IQueryTenants queryTenantsImpl;

    @Autowired
    private IVisitorRecord visitorRecordImpl;

    @RequestMapping(value = "/tenants")
    public ResponseEntity<String> tenants(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "code", "小区编码不能为空");
        return queryTenantsImpl.query(reqJson);
    }

    @RequestMapping(value = "/visitorRecord")
    public ResponseEntity<String> visitorRecord(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "code", "小区编码不能为空");
        return visitorRecordImpl.query(reqJson);
    }
}
