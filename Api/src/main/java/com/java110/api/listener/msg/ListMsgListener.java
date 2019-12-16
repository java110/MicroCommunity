package com.java110.api.listener.msg;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.common.IMsgInnerServiceSMO;
import com.java110.core.smo.notice.INoticeInnerServiceSMO;
import com.java110.dto.msg.MsgDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.msg.ApiMsgDataVo;
import com.java110.vo.api.msg.ApiMsgVo;
import com.java110.vo.api.notice.ApiNoticeDataVo;
import com.java110.vo.api.notice.ApiNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMsgListener")
public class ListMsgListener extends AbstractServiceApiListener {

    @Autowired
    private IMsgInnerServiceSMO msgInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_MSGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IMsgInnerServiceSMO getMsgInnerServiceSMOImpl() {
        return msgInnerServiceSMOImpl;
    }

    public void setMsgInnerServiceSMOImpl(IMsgInnerServiceSMO msgInnerServiceSMOImpl) {
        this.msgInnerServiceSMOImpl = msgInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MsgDto msgDto = BeanConvertUtil.covertBean(reqJson, MsgDto.class);
        String[] viewObjIds = new String[]{"9999", reqJson.getString("communityId"), reqJson.getString("storeId"), reqJson.getString("userId")};
        msgDto.setViewObjIds(viewObjIds);
        int count = msgInnerServiceSMOImpl.queryMsgsCount(msgDto);

        List<ApiMsgDataVo> msgs = null;

        if (count > 0) {
            msgs = BeanConvertUtil.covertBeanList(msgInnerServiceSMOImpl.queryMsgs(msgDto), ApiMsgDataVo.class);
        } else {
            msgs = new ArrayList<>();
        }

        ApiMsgVo apiMsgVo = new ApiMsgVo();

        apiMsgVo.setTotal(count);
        apiMsgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMsgVo.setMsgs(msgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMsgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
