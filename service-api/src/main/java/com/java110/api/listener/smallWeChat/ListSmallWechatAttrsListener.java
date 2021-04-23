package com.java110.api.listener.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.utils.constant.ServiceCodeSmallWechatAttrConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listSmallWechatAttrsListener")
public class ListSmallWechatAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeSmallWechatAttrConstant.LIST_SMALLWECHATATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ISmallWechatAttrInnerServiceSMO getSmallWechatAttrInnerServiceSMOImpl() {
        return smallWechatAttrInnerServiceSMOImpl;
    }

    public void setSmallWechatAttrInnerServiceSMOImpl(ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl) {
        this.smallWechatAttrInnerServiceSMOImpl = smallWechatAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "wechatId", "微信ID");
        //super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        SmallWechatAttrDto smallWechatAttrDto = BeanConvertUtil.covertBean(reqJson, SmallWechatAttrDto.class);

        int count = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrsCount(smallWechatAttrDto);

        List<SmallWechatAttrDto> smallWechatAttrDtos = null;

        if (count > 0) {
            smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        } else {
            smallWechatAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, smallWechatAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
