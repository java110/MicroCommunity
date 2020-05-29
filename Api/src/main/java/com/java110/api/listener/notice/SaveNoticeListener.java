package com.java110.api.listener.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.notice.INoticeBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveNoticeListener")
public class SaveNoticeListener extends AbstractServiceApiPlusListener {
    @Autowired
    private INoticeBMO noticeBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写标题");
        Assert.hasKeyAndValue(reqJson, "noticeTypeCd", "必填，请选择公告类型");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写公告内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必选，请填写开始时间 ");
        Assert.hasKeyAndValue(reqJson, "endTime", "必选，请填写结束时间 ");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        noticeBMOImpl.addNotice(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_NOTICE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
