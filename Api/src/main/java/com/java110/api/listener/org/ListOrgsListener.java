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
 * 查询小区侦听类
 */
@Java110Listener("listOrgsListener")
public class ListOrgsListener extends AbstractServiceApiListener {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOrgConstant.LIST_ORGS;
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
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);

        int count = orgInnerServiceSMOImpl.queryOrgsCount(orgDto);

        List<ApiOrgDataVo> orgs = null;

        if (count > 0) {
            orgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryOrgs(orgDto), ApiOrgDataVo.class);
        } else {
            orgs = new ArrayList<>();
        }

        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgs(orgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
