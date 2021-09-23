package com.java110.core.event.cmd;

import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.entity.center.AppService;

import java.util.EventObject;

/**
 *
 * 服务事件
 * Created by wuxw on 2018/5/18.
 */
public class CmdEvent extends EventObject {

    private ICmdDataFlowContext cmdDataFlowContext;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CmdEvent(Object source, ICmdDataFlowContext cmdDataFlowContext) {
        super(source);
        this.cmdDataFlowContext = cmdDataFlowContext;

    }

    public ICmdDataFlowContext getCmdDataFlowContext() {
        return cmdDataFlowContext;
    }
}
