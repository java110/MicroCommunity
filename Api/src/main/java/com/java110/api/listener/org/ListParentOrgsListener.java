package com.java110.api.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.org.IOrgInnerServiceSMO;
import com.java110.dto.org.OrgDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOrgConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.org.ApiOrgDataVo;
import com.java110.vo.api.org.ApiOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询上级组织侦听类
 */
@Java110Listener("listParentOrgsListener")
public class ListParentOrgsListener extends AbstractServiceApiListener {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOrgConstant.LIST_PARENT_ORGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IOrgInnerServiceSMO getOrgInnerServiceSMOImpl() {
        return orgInnerServiceSMOImpl;
    }

    public void setOrgInnerServiceSMOImpl(IOrgInnerServiceSMO orgInnerServiceSMOImpl) {
        this.orgInnerServiceSMOImpl = orgInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);



        List<ApiOrgDataVo> parentOrgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryParentOrgs(orgDto), ApiOrgDataVo.class);


        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setOrgs(parentOrgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
