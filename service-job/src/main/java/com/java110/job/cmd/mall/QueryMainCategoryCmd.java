package com.java110.job.cmd.mall;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.job.mall.ISendMall;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.UrlParamToJsonUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "mall.queryMainCategory")
public class QueryMainCategoryCmd extends Cmd {

    @Autowired
    private ISendMall sendMallImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String mallSwitch = MappingCache.getValue("MALL", "MALL_SWITCH");
        if (!"ON".equals(mallSwitch)) {
            throw new CmdException("商城系统未部署");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String param = UrlParamToJsonUtil.jsonToUrlParam(reqJson);
        ResultVo resultVo = sendMallImpl.get("/mall/api/productCategory.queryMainCategory?" + param);

        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
