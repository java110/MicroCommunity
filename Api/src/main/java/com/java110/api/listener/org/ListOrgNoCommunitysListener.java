package com.java110.api.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.org.IOrgCommunityInnerServiceSMO;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOrgConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.org.ApiOrgCommunityDataVo;
import com.java110.vo.api.org.ApiOrgCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listOrgCommunitysListener")
public class ListOrgNoCommunitysListener extends AbstractServiceApiListener {

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOrgConstant.LIST_ORG_NO_COMMUNITYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }



    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "orgId", "必填，请填写组织ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OrgCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, OrgCommunityDto.class);

        int count = orgCommunityInnerServiceSMOImpl.queryOrgCommunitysCount(orgCommunityDto);

        List<ApiOrgCommunityDataVo> orgCommunitys = null;

        if (count > 0) {
            orgCommunitys = BeanConvertUtil.covertBeanList(orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto), ApiOrgCommunityDataVo.class);
        } else {
            orgCommunitys = new ArrayList<>();
        }

        ApiOrgCommunityVo apiOrgVo = new ApiOrgCommunityVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgCommunitys(orgCommunitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }


    public IOrgCommunityInnerServiceSMO getOrgCommunityInnerServiceSMOImpl() {
        return orgCommunityInnerServiceSMOImpl;
    }

    public void setOrgCommunityInnerServiceSMOImpl(IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl) {
        this.orgCommunityInnerServiceSMOImpl = orgCommunityInnerServiceSMOImpl;
    }
}
