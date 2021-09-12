package com.java110.core.event.cmd;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractServiceCmdListener implements ServiceCmdListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractServiceCmdListener.class);


    /**
     * 分页信息校验
     *
     * @param reqJson
     */
    protected void validatePageInfo(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
    }

    @Override
    public void cmd(CmdEvent event) throws CmdException {
        //这里处理业务逻辑数据
        ICmdDataFlowContext dataFlowContext = event.getCmdDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();

        logger.debug("API服务 --- 请求参数为：{}", reqJson.toJSONString());

        validate(event, dataFlowContext,reqJson);

        doCmd(event, dataFlowContext, reqJson);

        //logger.debug("API服务 --- 返回报文信息：{}", dataFlowContext.getResponseEntity());
    }

    /**
     * 数据格式校验方法
     * @param event 事件对象
     * @param cmdDataFlowContext 请求报文数据
     */
    protected abstract void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson);


    /**
     * 业务处理类
     * @param event  事件对象
     * @param cmdDataFlowContext 数据上文对象
     * @param reqJson 请求报文
     */
    protected abstract void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException;


    @Override
    public int getOrder() {
        return 0;
    }

}
