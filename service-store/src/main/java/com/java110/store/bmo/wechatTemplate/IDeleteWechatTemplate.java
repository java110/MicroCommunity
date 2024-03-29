package com.java110.store.bmo.wechatTemplate;

import com.java110.po.wechat.WechatSmsTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteWechatTemplate {

    /**
     * 保存消息模板
     * @param wechatSmsTemplatePo
     * @return
     */
    ResponseEntity<String> delete(WechatSmsTemplatePo wechatSmsTemplatePo);
}
