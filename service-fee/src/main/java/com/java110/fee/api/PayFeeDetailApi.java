package com.java110.fee.api;


import com.alibaba.fastjson.JSONObject;
import com.java110.fee.bmo.payFeeDetail.IImportPayFeeBMODetail;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payFeeDetail")
public class PayFeeDetailApi {

    @Autowired
    private IImportPayFeeBMODetail importPayFeeDetailImpl;


    /**
     * 微信保存消息模板
     *
     * @param reqJsonStr
     * @return
     * @serviceCode /payFeeDetail/importPayFeeDetail
     * @path /app/payFeeDetail/importPayFeeDetail
     */
    @RequestMapping(value = "/importPayFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveImportFeeDetail(@RequestBody String reqJsonStr) {
        JSONObject reqJson = JSONObject.parseObject(reqJsonStr);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含费用对象");
        Assert.hasKeyAndValue(reqJson, "batchId", "请求报文中未包含批次");

        return importPayFeeDetailImpl.importPayFeeDetail(reqJson);
    }
}
