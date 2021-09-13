package com.java110.dev.cmd.cache;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.service.context.DataQuery;
import com.java110.service.context.DataQueryFactory;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 保存编码映射处理类
 */
@Java110Cmd(serviceCode = "flush.center.cache")
public class FlushCacheCmd extends AbstractServiceCmdListener {

    @Autowired
    IDevServiceCacheSMO devServiceCacheSMOImpl;

    @Override
    protected void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    @Java110Transactional
    protected void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        devServiceCacheSMOImpl.flush(reqJson.toJavaObject(Map.class));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
