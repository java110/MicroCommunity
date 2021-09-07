package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.hcGovTranslate.IDeleteHcGovTranslateBMO;
import com.java110.common.bmo.hcGovTranslate.IGetHcGovTranslateBMO;
import com.java110.common.bmo.hcGovTranslate.ISaveHcGovTranslateBMO;
import com.java110.common.bmo.hcGovTranslate.IUpdateHcGovTranslateBMO;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;
import com.java110.po.hcGovTranslate.HcGovTranslatePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/hcGovTranslate")
public class HcGovTranslateApi {

    @Autowired
    private ISaveHcGovTranslateBMO saveHcGovTranslateBMOImpl;
    @Autowired
    private IUpdateHcGovTranslateBMO updateHcGovTranslateBMOImpl;
    @Autowired
    private IDeleteHcGovTranslateBMO deleteHcGovTranslateBMOImpl;

    @Autowired
    private IGetHcGovTranslateBMO getHcGovTranslateBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /hcGovTranslate/saveHcGovTranslate
     * @path /app/hcGovTranslate/saveHcGovTranslate
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveHcGovTranslate", method = RequestMethod.POST)
    public ResponseEntity<String> saveHcGovTranslate(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "serviceCode", "请求报文中未包含serviceCode");
        Assert.hasKeyAndValue(reqJson, "reqTime", "请求报文中未包含reqTime");
        Assert.hasKeyAndValue(reqJson, "sign", "请求报文中未包含sign");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "请求报文中未包含extCommunityId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "code", "请求报文中未包含code");
        Assert.hasKeyAndValue(reqJson, "govTopic", "请求报文中未包含govTopic");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "sendCount", "请求报文中未包含sendCount");
        Assert.hasKeyAndValue(reqJson, "updateTime", "请求报文中未包含updateTime");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");


        HcGovTranslatePo hcGovTranslatePo = BeanConvertUtil.covertBean(reqJson, HcGovTranslatePo.class);
        return saveHcGovTranslateBMOImpl.save(hcGovTranslatePo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /hcGovTranslate/updateHcGovTranslate
     * @path /app/hcGovTranslate/updateHcGovTranslate
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateHcGovTranslate", method = RequestMethod.POST)
    public ResponseEntity<String> updateHcGovTranslate(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tranId", "请求报文中未包含tranId");
        Assert.hasKeyAndValue(reqJson, "serviceCode", "请求报文中未包含serviceCode");
        Assert.hasKeyAndValue(reqJson, "reqTime", "请求报文中未包含reqTime");
        Assert.hasKeyAndValue(reqJson, "sign", "请求报文中未包含sign");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "请求报文中未包含extCommunityId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "code", "请求报文中未包含code");
        Assert.hasKeyAndValue(reqJson, "govTopic", "请求报文中未包含govTopic");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "sendCount", "请求报文中未包含sendCount");
        Assert.hasKeyAndValue(reqJson, "updateTime", "请求报文中未包含updateTime");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "tranId", "tranId不能为空");


        HcGovTranslatePo hcGovTranslatePo = BeanConvertUtil.covertBean(reqJson, HcGovTranslatePo.class);
        return updateHcGovTranslateBMOImpl.update(hcGovTranslatePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /hcGovTranslate/deleteHcGovTranslate
     * @path /app/hcGovTranslate/deleteHcGovTranslate
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteHcGovTranslate", method = RequestMethod.POST)
    public ResponseEntity<String> deleteHcGovTranslate(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "tranId", "tranId不能为空");


        HcGovTranslatePo hcGovTranslatePo = BeanConvertUtil.covertBean(reqJson, HcGovTranslatePo.class);
        return deleteHcGovTranslateBMOImpl.delete(hcGovTranslatePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /hcGovTranslate/queryHcGovTranslate
     * @path /app/hcGovTranslate/queryHcGovTranslate
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryHcGovTranslate", method = RequestMethod.GET)
    public ResponseEntity<String> queryHcGovTranslate(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        HcGovTranslateDto hcGovTranslateDto = new HcGovTranslateDto();
        hcGovTranslateDto.setPage(page);
        hcGovTranslateDto.setRow(row);
        hcGovTranslateDto.setCommunityId(communityId);
        return getHcGovTranslateBMOImpl.get(hcGovTranslateDto);
    }
}
