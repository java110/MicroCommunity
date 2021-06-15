package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.car.IQueryCar;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/car")
public class CarApi {

    @Autowired
    private IQueryCar queryCarImpl;

    @RequestMapping(value = "/carInfo", method = RequestMethod.POST)
    public ResponseEntity<String> carInfo(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "code", "未包含小区ID");
        return queryCarImpl.queryCarInfo(reqJson);
    }
}
