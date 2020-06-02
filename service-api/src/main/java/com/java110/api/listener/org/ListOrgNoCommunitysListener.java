package com.java110.api.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.org.IOrgCommunityInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOrgConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listOrgNoCommunitysListener")
public class ListOrgNoCommunitysListener extends AbstractServiceApiListener {

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

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

        List<OrgCommunityDto> orgCommunityDtos = orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto);
        List<String> communityIds = new ArrayList<>();
        for(OrgCommunityDto tmpOrgCommunityDto : orgCommunityDtos){
            communityIds.add(tmpOrgCommunityDto.getCommunityId());
        }
        CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);;
        if(communityIds.size()>0) {
            communityDto.setNotInCommunityId(communityIds.toArray(new String[communityIds.size()]));
        }
        communityDto.setAuditStatusCd("1100");
        communityDto.setMemberId(reqJson.getString("storeId"));
        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<ApiCommunityDataVo> communitys = null;

        if (count > 0) {
            communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);
        } else {
            communitys = new ArrayList<>();
        }

        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }


    public IOrgCommunityInnerServiceSMO getOrgCommunityInnerServiceSMOImpl() {
        return orgCommunityInnerServiceSMOImpl;
    }

    public void setOrgCommunityInnerServiceSMOImpl(IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl) {
        this.orgCommunityInnerServiceSMOImpl = orgCommunityInnerServiceSMOImpl;
    }
}
