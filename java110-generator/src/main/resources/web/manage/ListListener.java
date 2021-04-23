package com.java110.api.listener.@@templateCode@@;

import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.@@templateCode@@.I@@TemplateCode@@InnerServiceSMO;
import com.java110.dto.@@templateCode@@.@@TemplateCode@@Dto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.@@templateCode@@.Api@@TemplateCode@@DataVo;
import com.java110.vo.api.@@templateCode@@.Api@@TemplateCode@@Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCode@@TemplateCode@@Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("list@@TemplateCode@@sListener")
public class List@@TemplateCode@@sListener extends AbstractServiceApiListener {

    @Autowired
    private I@@TemplateCode@@InnerServiceSMO @@templateCode@@InnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCode@@TemplateCode@@Constant.LIST_@@TEMPLATECODE@@S;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public I@@TemplateCode@@InnerServiceSMO get@@TemplateCode@@InnerServiceSMOImpl() {
        return @@templateCode@@InnerServiceSMOImpl;
    }

    public void set@@TemplateCode@@InnerServiceSMOImpl(I@@TemplateCode@@InnerServiceSMO @@templateCode@@InnerServiceSMOImpl) {
        this.@@templateCode@@InnerServiceSMOImpl = @@templateCode@@InnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        @@TemplateCode@@Dto @@templateCode@@Dto = BeanConvertUtil.covertBean(reqJson, @@TemplateCode@@Dto.class);

        int count = @@templateCode@@InnerServiceSMOImpl.query@@TemplateCode@@sCount(@@templateCode@@Dto);

        List<Api@@TemplateCode@@DataVo> @@templateCode@@s = null;

        if (count > 0) {
            @@templateCode@@s = BeanConvertUtil.covertBeanList(@@templateCode@@InnerServiceSMOImpl.query@@TemplateCode@@s(@@templateCode@@Dto), Api@@TemplateCode@@DataVo.class);
        } else {
            @@templateCode@@s = new ArrayList<>();
        }

        Api@@TemplateCode@@Vo api@@TemplateCode@@Vo = new Api@@TemplateCode@@Vo();

        api@@TemplateCode@@Vo.setTotal(count);
        api@@TemplateCode@@Vo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        api@@TemplateCode@@Vo.set@@TemplateCode@@s(@@templateCode@@s);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(api@@TemplateCode@@Vo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
