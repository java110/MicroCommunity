package com.java110.api.listener.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.notice.INoticeInnerServiceSMO;
import com.java110.dto.notice.NoticeDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.notice.ApiNoticeDataVo;
import com.java110.vo.api.notice.ApiNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.BusinessTypeConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listNoticesListener")
public class ListNoticesListener extends AbstractServiceApiListener {

    @Autowired
    private INoticeInnerServiceSMO noticeInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_NOTICES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public INoticeInnerServiceSMO getNoticeInnerServiceSMOImpl() {
        return noticeInnerServiceSMOImpl;
    }

    public void setNoticeInnerServiceSMOImpl(INoticeInnerServiceSMO noticeInnerServiceSMOImpl) {
        this.noticeInnerServiceSMOImpl = noticeInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        NoticeDto noticeDto = BeanConvertUtil.covertBean(reqJson, NoticeDto.class);

        int count = noticeInnerServiceSMOImpl.queryNoticesCount(noticeDto);

        List<ApiNoticeDataVo> notices = null;

        if (count > 0) {
            notices = BeanConvertUtil.covertBeanList(noticeInnerServiceSMOImpl.queryNotices(noticeDto), ApiNoticeDataVo.class);
        } else {
            notices = new ArrayList<>();
        }

        ApiNoticeVo apiNoticeVo = new ApiNoticeVo();

        apiNoticeVo.setTotal(count);
        apiNoticeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiNoticeVo.setNotices(notices);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiNoticeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
