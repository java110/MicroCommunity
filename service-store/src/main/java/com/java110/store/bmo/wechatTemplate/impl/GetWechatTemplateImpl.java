package com.java110.store.bmo.wechatTemplate.impl;

import com.java110.dto.wechatSmsTemplate.WechatSmsTemplateDto;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.store.bmo.wechatTemplate.IGetWechatTemplate;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getWechatTemplateImpl")
public class GetWechatTemplateImpl implements IGetWechatTemplate {
    @Autowired
    private IWechatSmsTemplateInnerServiceSMO wechatSmsTemplateInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> get(WechatSmsTemplateDto wechatSmsTemplateDto) {
        int count = wechatSmsTemplateInnerServiceSMOImpl.queryWechatSmsTemplatesCount(wechatSmsTemplateDto);

        List<WechatSmsTemplateDto> wechatSmsTemplateDtos = null;
        if (count > 0) {
            wechatSmsTemplateDtos = wechatSmsTemplateInnerServiceSMOImpl.queryWechatSmsTemplates(wechatSmsTemplateDto);
        } else {
            wechatSmsTemplateDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) wechatSmsTemplateDto.getRow()), count, wechatSmsTemplateDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }
}
