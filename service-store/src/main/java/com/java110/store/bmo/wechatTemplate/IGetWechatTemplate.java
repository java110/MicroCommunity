package com.java110.store.bmo.wechatTemplate;

import com.java110.dto.wechatSmsTemplate.WechatSmsTemplateDto;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IGetWechatTemplate {

    /**
     * 查询消息模板
     * @param wechatSmsTemplateDto
     * @return
     */
    ResponseEntity<String> get(WechatSmsTemplateDto wechatSmsTemplateDto);
}
