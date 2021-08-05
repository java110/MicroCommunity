package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.initializeBuildingUnit.IinitializeBuildingUnit;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName RoomApi
 * @Description TODO
 * @Author wuxw
 * @Date 2020/8/6 23:27
 * @Version 1.0
 * add by wuxw 2020/8/6
 **/

@RestController
@RequestMapping(value = "/InitializeBuildingUnit")
public class InitializeBuildingUnitApi {

    @Autowired
    private IinitializeBuildingUnit IinitializeBuildingUnitImpl;
    /**
     * 初始化
     *
     * @param reqJson
     * @return
     * @Service /InitializeBuildingUnit/deleteBuildingUnit
     * @path /app/InitializeBuildingUnit/deleteBuildingUnit
     */
    @RequestMapping(value = "/deleteBuildingUnit", method = RequestMethod.POST)
    public ResponseEntity<String> deleteBuildingUnit(@RequestHeader(value = "user-id") String userId,
                                                     @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        String communityId = reqJson.getString("communityId");
        String userPassword = reqJson.getString("devPassword");
        IinitializeBuildingUnitImpl.deleteBuildingUnit(communityId,userId,userPassword);
        return null;
    }
}
