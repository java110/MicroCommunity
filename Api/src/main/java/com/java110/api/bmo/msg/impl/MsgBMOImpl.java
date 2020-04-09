package com.java110.api.bmo.msg.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.msg.IMsgBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
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


    public Object addReadMsg(JSONObject paramInJson, DataFlowContext context) {

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG_READ);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessMsgRead = new JSONObject();
        //businessApplicationKey.putAll(paramInJson);
        businessMsgRead.put("msgReadId", "-1");
        businessMsgRead.putAll(paramInJson);

        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessMsgRead", businessMsgRead);
        return business;
    }
}
