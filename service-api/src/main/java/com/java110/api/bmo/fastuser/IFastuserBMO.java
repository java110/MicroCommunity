package com.java110.api.bmo.fastuser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IFastuserBMO extends IApiBaseBMO {



    /**
     * 添加活动
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     JSONObject addFastuser(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
