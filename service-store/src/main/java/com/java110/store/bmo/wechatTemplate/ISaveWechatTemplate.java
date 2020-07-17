package com.java110.store.bmo.wechatTemplate;

import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import org.springframework.http.ResponseEntity;

public interface ISaveWechatTemplate {

    /**
     * 保存消息模板
     * @param wechatSmsTemplatePo
     * @return
     */
    ResponseEntity<String> save(WechatSmsTemplatePo wechatSmsTemplatePo);
}
