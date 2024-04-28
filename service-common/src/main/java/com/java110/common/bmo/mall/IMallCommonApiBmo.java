package com.java110.common.bmo.mall;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;

public interface IMallCommonApiBmo {

    void validate(ICmdDataFlowContext context, JSONObject reqJson);


    void doCmd(ICmdDataFlowContext context, JSONObject reqJson);
}
