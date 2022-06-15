package com.java110.dev.cmd.cache;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 保存编码映射处理类
 */
@Java110Cmd(serviceCode = "flush.center.cache")
public class FlushCacheCmd extends Cmd {

    @Autowired
    IDevServiceCacheSMO devServiceCacheSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        devServiceCacheSMOImpl.flush(reqJson.toJavaObject(Map.class));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
