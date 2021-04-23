package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.wechatSmsTemplate.WechatSmsTemplateDto;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.store.bmo.wechatTemplate.IDeleteWechatTemplate;
import com.java110.store.bmo.wechatTemplate.IGetWechatTemplate;
import com.java110.store.bmo.wechatTemplate.ISaveWechatTemplate;
import com.java110.store.bmo.wechatTemplate.IUpdateWechatTemplate;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wechat")
public class WechatApi {

    @Autowired
    private ISaveWechatTemplate saveWechatTemplateImpl;
    @Autowired
    private IUpdateWechatTemplate updateWechatTemplateImpl;
    @Autowired
    private IDeleteWechatTemplate deleteWechatTemplateImpl;

    @Autowired
    private IGetWechatTemplate getWechatTemplateImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveWechatTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> saveWechatTemplate(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "templateType", "模板类型不能为空");
        Assert.hasKeyAndValue(reqJson, "smsTemplateId", "微信模板ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "wechatId", "微信ID不能为空");
        WechatSmsTemplatePo wechatSmsTemplatePo = BeanConvertUtil.covertBean(reqJson, WechatSmsTemplatePo.class);
        return saveWechatTemplateImpl.save(wechatSmsTemplatePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateWechatTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> updateWechatTemplate(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "templateType", "模板类型不能为空");
        Assert.hasKeyAndValue(reqJson, "templateId", "模板ID不能为空");
        Assert.hasKeyAndValue(reqJson, "smsTemplateId", "微信模板ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "wechatId", "微信ID不能为空");
        WechatSmsTemplatePo wechatSmsTemplatePo = BeanConvertUtil.covertBean(reqJson, WechatSmsTemplatePo.class);
        return updateWechatTemplateImpl.update(wechatSmsTemplatePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteWechatTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> deleteWechatTemplate(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "templateId", "模板ID不能为空");
        WechatSmsTemplatePo wechatSmsTemplatePo = BeanConvertUtil.covertBean(reqJson, WechatSmsTemplatePo.class);
        return deleteWechatTemplateImpl.delete(wechatSmsTemplatePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryWechatTemplate", method = RequestMethod.GET)
    public ResponseEntity<String> queryWechatTemplate(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row,
                                                      @RequestParam(value = "templateId", required = false) String templateId,
                                                      @RequestParam(value = "wechatId", required = false) String wechatId,
                                                      @RequestParam(value = "templateType", required = false) String templateType,
                                                      @RequestParam(value = "smsTempalateId", required = false) String smsTemplateId) {
        WechatSmsTemplateDto wechatSmsTemplateDto = new WechatSmsTemplateDto();
        wechatSmsTemplateDto.setPage(page);
        wechatSmsTemplateDto.setRow(row);
        wechatSmsTemplateDto.setCommunityId(communityId);
        wechatSmsTemplateDto.setSmsTemplateId(smsTemplateId);
        wechatSmsTemplateDto.setTemplateType(templateType);
        wechatSmsTemplateDto.setTemplateId(templateId);
        wechatSmsTemplateDto.setWechatId(wechatId);
        return getWechatTemplateImpl.get(wechatSmsTemplateDto);
    }
}
