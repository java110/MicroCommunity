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

@Service("get@@TemplateCode@@BMOImpl")
public class Get@@TemplateCode@@BMOImpl implements IGet@@TemplateCode@@BMO {

    @Autowired
    private I@@TemplateCode@@InnerServiceSMO @@templateCode@@InnerServiceSMOImpl;

    /**
     *
     *
     * @param  @@templateCode@@Dto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(@@TemplateCode@@Dto @@templateCode@@Dto) {


        int count = @@templateCode@@InnerServiceSMOImpl.query@@TemplateCode@@sCount(@@templateCode@@Dto);

        List<@@TemplateCode@@Dto> @@templateCode@@Dtos = null;
        if (count > 0) {
            @@templateCode@@Dtos = @@templateCode@@InnerServiceSMOImpl.query@@TemplateCode@@s(@@templateCode@@Dto);
        } else {
            @@templateCode@@Dtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) @@templateCode@@Dto.getRow()), count, @@templateCode@@Dtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
