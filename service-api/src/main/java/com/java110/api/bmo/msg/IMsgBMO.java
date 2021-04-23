package com.java110.api.bmo.msg;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IMsgBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:59
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IMsgBMO extends IApiBaseBMO {
    public void addReadMsg(JSONObject paramInJson, DataFlowContext context);
}
