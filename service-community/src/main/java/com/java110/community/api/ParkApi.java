package com.java110.community.api;


import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.park.IQueryParkSpace;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ParkAPI")
public class ParkApi {

    @Autowired
    private IQueryParkSpace queryParkSpaceImpl;

    /**
     * @path /ParkAPI/PackSpace
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/PackSpace", method = RequestMethod.POST)
    public ResponseEntity<String> packSpace(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "code", "未包含小区ID");
        return queryParkSpaceImpl.query(reqJson);
    }
}
