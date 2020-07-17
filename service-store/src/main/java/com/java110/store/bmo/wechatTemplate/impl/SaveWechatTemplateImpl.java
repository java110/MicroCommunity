package com.java110.store.bmo.wechatTemplate.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.store.bmo.wechatTemplate.ISaveWechatTemplate;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveWechatTemplateImpl")
public class SaveWechatTemplateImpl implements ISaveWechatTemplate {
    @Autowired
    private IWechatSmsTemplateInnerServiceSMO wechatSmsTemplateInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> save(WechatSmsTemplatePo wechatSmsTemplatePo) {
        wechatSmsTemplatePo.setTemplateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_weChatId));
        int flag = wechatSmsTemplateInnerServiceSMOImpl.saveWechatSmsTemplate(wechatSmsTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}
