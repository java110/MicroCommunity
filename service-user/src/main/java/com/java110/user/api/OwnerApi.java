package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.owner.OwnerPo;
import com.java110.user.bmo.owner.IChangeOwnerPhone;
import com.java110.user.bmo.owner.IQueryTenants;
import com.java110.user.bmo.owner.IVisitorRecord;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ownerApi")
public class OwnerApi {

    @Autowired
    private IQueryTenants queryTenantsImpl;

    @Autowired
    private IVisitorRecord visitorRecordImpl;

    @Autowired
    private IChangeOwnerPhone changeOwnerPhoneImpl;

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

    /**
     * 变更 业主手机号
     *
     * @param reqJson
     * @return
     * @service /ownerApi/changeOwnerPhoto
     * @path /app/ownerApi/changeOwnerPhoto
     */
    @RequestMapping(value = "/changeOwnerPhone", method = RequestMethod.POST)
    public ResponseEntity<String> changeOwnerPhone(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson,"link","请求报文中未包含手机号");
        Assert.hasKeyAndValue(reqJson,"userId","请求报文中未包含用户ID");
        Assert.hasKeyAndValue(reqJson,"memberId","请求报文中未包含业主信息");
        Assert.hasKeyAndValue(reqJson,"communityId","请求报文中未包含小区信息");
        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setLink(reqJson.getString("link"));
        ownerPo.setMemberId(reqJson.getString("memberId"));
        ownerPo.setCommunityId(reqJson.getString("communityId"));
        ownerPo.setUserId(reqJson.getString("userId"));

        return changeOwnerPhoneImpl.change(ownerPo);
    }
}
