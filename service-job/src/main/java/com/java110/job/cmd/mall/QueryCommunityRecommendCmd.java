package com.java110.job.cmd.mall;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.job.mall.ISendMall;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.CacheUtil;
import com.java110.utils.util.UrlParamToJsonUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 查询小区推荐
 */
@Java110Cmd(serviceCode = "mall.queryCommunityRecommend")
public class QueryCommunityRecommendCmd extends Cmd {
    @Autowired
    private ISendMall sendMallImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONArray data = new JSONArray();
        JSONObject recommend = new JSONObject();
        recommend.put("logo", "http://demo.homecommunity.cn/h5/images/noPhoto.jpg");
        recommend.put("prodName", "暂无推荐");
        recommend.put("url","NULL");
        recommend.put("price","0");
        data.add(recommend);
        data.add(recommend);
        String mallSwitch = MappingCache.getValue("MALL", "MALL_SWITCH");
        if (!"ON".equals(mallSwitch)) {
            context.setResponseEntity(ResultVo.createResponseEntity(data));
            return;
        }
        reqJson.put("mallApiCode", "queryCommunityRecommendBmoImpl");
        ResultVo resultVo = sendMallImpl.post("/mall/api/common.openCommonApi", reqJson);
        if (resultVo.getCode() != ResultVo.CODE_OK) {
            context.setResponseEntity(ResultVo.createResponseEntity(data));
            return;
        }
        if(resultVo.getTotal() <1){
            context.setResponseEntity(ResultVo.createResponseEntity(data));
            return;
        }
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
