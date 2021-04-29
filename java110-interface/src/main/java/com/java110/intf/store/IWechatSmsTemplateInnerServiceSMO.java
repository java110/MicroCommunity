package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.wechatSmsTemplate.WechatSmsTemplateDto;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWechatSmsTemplateInnerServiceSMO
 * @Description 微信消息模板接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/wechatSmsTemplateApi")
public interface IWechatSmsTemplateInnerServiceSMO {


    @RequestMapping(value = "/saveWechatSmsTemplate", method = RequestMethod.POST)
    public int saveWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo);

    @RequestMapping(value = "/updateWechatSmsTemplate", method = RequestMethod.POST)
    public int updateWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo);

    @RequestMapping(value = "/deleteWechatSmsTemplate", method = RequestMethod.POST)
    public int deleteWechatSmsTemplate(@RequestBody WechatSmsTemplatePo wechatSmsTemplatePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param wechatSmsTemplateDto 数据对象分享
     * @return WechatSmsTemplateDto 对象数据
     */
    @RequestMapping(value = "/queryWechatSmsTemplates", method = RequestMethod.POST)
    List<WechatSmsTemplateDto> queryWechatSmsTemplates(@RequestBody WechatSmsTemplateDto wechatSmsTemplateDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param wechatSmsTemplateDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWechatSmsTemplatesCount", method = RequestMethod.POST)
    int queryWechatSmsTemplatesCount(@RequestBody WechatSmsTemplateDto wechatSmsTemplateDto);
}
