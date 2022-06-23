package com.java110.core.event.cmd;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;



public abstract class Cmd implements ServiceCmdListener {

    private static Logger logger = LoggerFactory.getLogger(Cmd.class);

    protected static final int MAX_ROW = 10000;


    /**
     * 分页信息校验
     *
     * @param reqJson
     */
    protected void validatePageInfo(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }
//
//    @Override
//
//    public void cmd(CmdEvent event) throws CmdException {
//        //这里处理业务逻辑数据
//        ICmdDataFlowContext dataFlowContext = event.getCmdDataFlowContext();
//        //获取请求数据
//        JSONObject reqJson = dataFlowContext.getReqJson();
//
//        logger.debug("API服务 --- 请求参数为：{}", reqJson.toJSONString());
//
//        validate(event, dataFlowContext,reqJson);
//
//        doCmd(event, dataFlowContext, reqJson);
//
//        //logger.debug("API服务 --- 返回报文信息：{}", dataFlowContext.getResponseEntity());
//    }



    @Override
    public int getOrder() {
        return 0;
    }

}
