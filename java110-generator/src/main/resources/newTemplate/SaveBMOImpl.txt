package com.java110.api.bmo.@@templateCode@@.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.@@templateCode@@.I@@TemplateCode@@BMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.@@templateCode@@.I@@TemplateCode@@InnerServiceSMO;
import com.java110.dto.@@templateCode@@.@@TemplateCode@@Dto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("save@@TemplateCode@@BMOImpl")
public class Save@@TemplateCode@@BMOImpl implements ISave@@TemplateCode@@BMO {

    @Autowired
    private I@@TemplateCode@@InnerServiceSMO @@templateCode@@InnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param @@templateCode@@Po
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(@@TemplateCode@@Po @@templateCode@@Po) {

        @@templateCode@@Po.set@@TemplateKey@@(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_@@templateKey@@));
        int flag = @@templateCode@@InnerServiceSMOImpl.save@@TemplateCode@@(@@templateCode@@Po);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
