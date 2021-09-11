package com.java110.core.event.cmd;

import com.java110.core.event.app.order.Ordered;
import com.java110.utils.exception.CmdException;

import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface ServiceCmdListener extends EventListener, Ordered {

    /**
     * 执行指令
     * @param event
     * @throws Exception
     */
     void cmd(CmdEvent event) throws CmdException;
}
