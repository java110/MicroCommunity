package com.java110.community.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.Assert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoomApi
 * @Description TODO
 * @Author wuxw
 * @Date 2020/8/6 23:27
 * @Version 1.0
 * add by wuxw 2020/8/6
 **/

@RestController
@RequestMapping(value = "/room")
public class RoomApi {

    /**
     * 查询房屋信息
     *
     * @param reqJson
     * @return
     * @Service /room/getRoom
     * @path /app/room/getRoom
     */
    @RequestMapping(value = "/getRoom", method = RequestMethod.POST)
    public ResponseEntity<String> getRoom(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "code", "未包含小区ID");
        return null;
    }
}
