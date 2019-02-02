package com.java110.code.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.code.smo.IPrimaryKeyServiceSMO;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.feign.base.IPrimaryKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主键信息查询
 * Created by wuxw on 2017/4/5.
 */
//@RestController
public class PrimaryKeyServiceRest extends BaseController implements IPrimaryKeyService {

    protected final static Logger logger = LoggerFactory.getLogger(PrimaryKeyServiceRest.class);

    @Autowired
    IPrimaryKeyServiceSMO iPrimaryKeyServiceSMO;

    /**
     * 查询主键
     * 协议 :
     * {'type':'CUST'}
     * 返回：
     * <p>
     * 成功
     * {'RESULT_CODE':'0000','RESULT_MSG':'成功','RESULT_INFO':{'CUST':'710170410001'}}
     * 失败
     * {'RESULT_CODE':'1999','RESULT_MSG':'失败原因','RESULT_INFO':{}}
     *
     * @param data
     * @return
     */
    @RequestMapping("/primaryKeyService/queryPrimaryKey")
    public String queryPrimaryKey(@RequestParam("data") String data) {

        JSONObject requestJson = null;

        String responseParam = "";
        try {
            requestJson = this.simpleValidateJSON(data);

            if (requestJson == null || !requestJson.containsKey("type")) {
                throw new IllegalArgumentException("查询主键入参data[" + data + "]出错");
            }

            requestJson.put("name", requestJson.getString("type"));
            JSONObject returnPrimaryKey = iPrimaryKeyServiceSMO.queryPrimaryKey(requestJson);
            String targetId = returnPrimaryKey.getString("targetId");

            returnPrimaryKey.clear();

            returnPrimaryKey.put(requestJson.getString("type"), targetId);

            responseParam = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_SUCCESS, "成功", returnPrimaryKey);
        } catch (Exception e) {
            LoggerEngine.error("查询主键失败,请求参数为【" + data + "】 ", e);
            responseParam = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR, "查询主键失败：" + e, null);
        } finally {
            return responseParam;
        }
    }


    public IPrimaryKeyServiceSMO getiPrimaryKeyServiceSMO() {
        return iPrimaryKeyServiceSMO;
    }

    public void setiPrimaryKeyServiceSMO(IPrimaryKeyServiceSMO iPrimaryKeyServiceSMO) {
        this.iPrimaryKeyServiceSMO = iPrimaryKeyServiceSMO;
    }
}
