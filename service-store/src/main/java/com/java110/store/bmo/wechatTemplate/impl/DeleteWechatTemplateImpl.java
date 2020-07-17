package com.java110.store.bmo.wechatTemplate.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.store.bmo.wechatTemplate.IDeleteWechatTemplate;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteWechatTemplateImpl")
public class DeleteWechatTemplateImpl implements IDeleteWechatTemplate {
    @Autowired
    private IWechatSmsTemplateInnerServiceSMO wechatSmsTemplateInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(WechatSmsTemplatePo wechatSmsTemplatePo) {
        int flag = wechatSmsTemplateInnerServiceSMOImpl.deleteWechatSmsTemplate(wechatSmsTemplatePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }
}
