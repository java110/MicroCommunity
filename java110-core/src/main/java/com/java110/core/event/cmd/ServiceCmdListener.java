package com.java110.core.event.cmd;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.app.order.Ordered;
import com.java110.utils.exception.CmdException;

import java.text.ParseException;
import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface ServiceCmdListener extends EventListener, Ordered {
    

    /**
     * 数据格式校验方法
     *
     * @param event              事件对象
     * @param context 请求报文数据
     */
    void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException;


    /**
     * 执行指令
     *
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     */
    void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException;
}
