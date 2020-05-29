package com.java110.api.bmo.msg.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.msg.IMsgBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.message.MsgReadPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName MsgBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:00
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("msgBMOImpl")
public class MsgBMOImpl extends ApiBaseBMO implements IMsgBMO {


    public void addReadMsg(JSONObject paramInJson, DataFlowContext context) {

        JSONObject businessMsgRead = new JSONObject();
        businessMsgRead.put("msgReadId", "-1");
        businessMsgRead.putAll(paramInJson);
        MsgReadPo msgReadPo = BeanConvertUtil.covertBean(businessMsgRead, MsgReadPo.class);

        super.insert(context, msgReadPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG_READ);
    }
}
