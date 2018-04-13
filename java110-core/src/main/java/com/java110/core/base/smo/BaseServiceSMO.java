package com.java110.core.base.smo;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.AppBase;
import com.java110.core.context.AppContext;
import com.java110.feign.base.IPrimaryKeyService;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * 所有服务端的基类
 * 1、报文分装
 * 2、报文解析
 * Created by wuxw on 2017/2/28.
 */
public class BaseServiceSMO extends AppBase {


    /**
     * 主键生成
     * @param iPrimaryKeyService 主键生成服务对象
     * @param type 主键类型 如 OL_ID , CUST_ID
     * @return
     * @throws Exception
     */
    protected String queryPrimaryKey(IPrimaryKeyService iPrimaryKeyService,String type) throws Exception{
        JSONObject data = new JSONObject();
        data.put("type",type);
        //生成的ID
        String targetId = "-1";
        //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
        String custIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
        JSONObject custIdJSONTmp = JSONObject.parseObject(custIdJSONStr);
        if(custIdJSONTmp.containsKey("RESULT_CODE")
                && ProtocolUtil.RETURN_MSG_SUCCESS.equals(custIdJSONTmp.getString("RESULT_CODE"))
                && custIdJSONTmp.containsKey("RESULT_INFO")){
            //从接口生成olId
            targetId =  custIdJSONTmp.getJSONObject("RESULT_INFO").getString(type);
        }
        if("-1".equals(targetId)) {
            throw new RuntimeException("调用主键生成服务服务失败，" + custIdJSONStr);
        }

        return targetId;
    }


    /**
     * 创建上下文对象
     * @return
     */
    protected AppContext createApplicationContext(){
        return AppContext.newInstance();
    }





}
