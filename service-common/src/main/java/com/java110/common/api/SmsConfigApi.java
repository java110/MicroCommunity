package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.smsConfig.IDeleteSmsConfigBMO;
import com.java110.common.bmo.smsConfig.IGetSmsConfigBMO;
import com.java110.common.bmo.smsConfig.ISaveSmsConfigBMO;
import com.java110.common.bmo.smsConfig.IUpdateSmsConfigBMO;
import com.java110.dto.smsConfig.SmsConfigDto;
import com.java110.po.smsConfig.SmsConfigPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/smsConfig")
public class SmsConfigApi {

    @Autowired
    private ISaveSmsConfigBMO saveSmsConfigBMOImpl;
    @Autowired
    private IUpdateSmsConfigBMO updateSmsConfigBMOImpl;
    @Autowired
    private IDeleteSmsConfigBMO deleteSmsConfigBMOImpl;

    @Autowired
    private IGetSmsConfigBMO getSmsConfigBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /smsConfig/saveSmsConfig
     * @path /app/smsConfig/saveSmsConfig
     */
    @RequestMapping(value = "/saveSmsConfig", method = RequestMethod.POST)
    public ResponseEntity<String> saveSmsConfig(@RequestHeader(value = "store-id") String storeId,
                                                @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "smsType", "请求报文中未包含smsType");
        Assert.hasKeyAndValue(reqJson, "smsBusi", "请求报文中未包含smsBusi");
        Assert.hasKeyAndValue(reqJson, "templateCode", "请求报文中未包含templateCode");
        Assert.hasKeyAndValue(reqJson, "signName", "请求报文中未包含signName");
        Assert.hasKeyAndValue(reqJson, "accessSecret", "请求报文中未包含accessSecret");
        Assert.hasKeyAndValue(reqJson, "accessKeyId", "请求报文中未包含accessKeyId");
        Assert.hasKeyAndValue(reqJson, "region", "请求报文中未包含region");
        Assert.hasKeyAndValue(reqJson, "logSwitch", "请求报文中未包含logSwitch");


        SmsConfigPo smsConfigPo = BeanConvertUtil.covertBean(reqJson, SmsConfigPo.class);
        smsConfigPo.setStoreId(storeId);
        return saveSmsConfigBMOImpl.save(smsConfigPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /smsConfig/updateSmsConfig
     * @path /app/smsConfig/updateSmsConfig
     */
    @RequestMapping(value = "/updateSmsConfig", method = RequestMethod.POST)
    public ResponseEntity<String> updateSmsConfig(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "smsType", "请求报文中未包含smsType");
        Assert.hasKeyAndValue(reqJson, "smsBusi", "请求报文中未包含smsBusi");
        Assert.hasKeyAndValue(reqJson, "templateCode", "请求报文中未包含templateCode");
        Assert.hasKeyAndValue(reqJson, "signName", "请求报文中未包含signName");
        Assert.hasKeyAndValue(reqJson, "accessSecret", "请求报文中未包含accessSecret");
        Assert.hasKeyAndValue(reqJson, "accessKeyId", "请求报文中未包含accessKeyId");
        Assert.hasKeyAndValue(reqJson, "region", "请求报文中未包含region");
        Assert.hasKeyAndValue(reqJson, "logSwitch", "请求报文中未包含logSwitch");
        Assert.hasKeyAndValue(reqJson, "smsId", "smsId不能为空");


        SmsConfigPo smsConfigPo = BeanConvertUtil.covertBean(reqJson, SmsConfigPo.class);
        return updateSmsConfigBMOImpl.update(smsConfigPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /smsConfig/deleteSmsConfig
     * @path /app/smsConfig/deleteSmsConfig
     */
    @RequestMapping(value = "/deleteSmsConfig", method = RequestMethod.POST)
    public ResponseEntity<String> deleteSmsConfig(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "smsId", "smsId不能为空");


        SmsConfigPo smsConfigPo = BeanConvertUtil.covertBean(reqJson, SmsConfigPo.class);
        return deleteSmsConfigBMOImpl.delete(smsConfigPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param objId 小区ID
     * @return
     * @serviceCode /smsConfig/querySmsConfig
     * @path /app/smsConfig/querySmsConfig
     */
    @RequestMapping(value = "/querySmsConfig", method = RequestMethod.GET)
    public ResponseEntity<String> querySmsConfig(@RequestParam(value = "objId") String objId,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "row") int row) {
        SmsConfigDto smsConfigDto = new SmsConfigDto();
        smsConfigDto.setPage(page);
        smsConfigDto.setRow(row);
        smsConfigDto.setObjId(objId);
        return getSmsConfigBMOImpl.get(smsConfigDto);
    }
}
