package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerDto;
import com.java110.po.owner.OwnerPo;
import com.java110.user.bmo.owner.*;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ownerApi")
public class OwnerApi {

    @Autowired
    private IQueryTenants queryTenantsImpl;

    @Autowired
    private IVisitorRecord visitorRecordImpl;

    @Autowired
    private IChangeOwnerPhone changeOwnerPhoneImpl;


    @Autowired
    private IComprehensiveQuery comprehensiveQueryImpl;

    @Autowired
    private IQueryShopsHireLog queryShopsHireLogImpl;

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
     * @service /ownerApi/changeOwnerPhone
     * @path /app/ownerApi/changeOwnerPhoto
     */
    @RequestMapping(value = "/changeOwnerPhone", method = RequestMethod.POST)
    public ResponseEntity<String> changeOwnerPhone(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "link", "请求报文中未包含手机号");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户ID");
        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含业主信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setLink(reqJson.getString("link"));
        ownerPo.setMemberId(reqJson.getString("memberId"));
        ownerPo.setCommunityId(reqJson.getString("communityId"));
        ownerPo.setUserId(reqJson.getString("userId"));

        return changeOwnerPhoneImpl.change(ownerPo);
    }

    /**
     * 综合查询
     *
     * @param communityId 小区ID
     * @return
     * @service /ownerApi/comprehensiveQuery
     * @path /app/ownerApi/comprehensiveQuery
     */
    @RequestMapping(value = "/comprehensiveQuery", method = RequestMethod.GET)
    public ResponseEntity<String> comprehensiveQuery(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "searchValue") String searchValue,
                                                     @RequestParam(value = "searchType") String searchType,
                                                     @RequestHeader(value = "user-id") String userId,
                                                     @RequestHeader(value = "store-id") String storeId) {
        return comprehensiveQueryImpl.query(communityId, searchValue, searchType, userId,storeId);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /ownerApi/queryShopsHireLog
     * @path /app/ownerApi/queryShopsHireLog
     */
    @RequestMapping(value = "/queryShopsHireLog", method = RequestMethod.GET)
    public ResponseEntity<String> queryShopsHireLog(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "roomId") String roomId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setPage(page);
        ownerDto.setRow(row);
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(roomId);
        return queryShopsHireLogImpl.query(ownerDto);
    }
}
