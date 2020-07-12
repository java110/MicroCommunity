package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.StateConstant;
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
@Java110Listener("listMyEnteredCommunitysListener")
public class ListMyEnteredCommunitysListener extends AbstractServiceApiListener {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_MY_ENTERED_COMMUNITYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ICommunityInnerServiceSMO getCommunityInnerServiceSMOImpl() {
        return communityInnerServiceSMOImpl;
    }

    public void setCommunityInnerServiceSMOImpl(ICommunityInnerServiceSMO communityInnerServiceSMOImpl) {
        this.communityInnerServiceSMOImpl = communityInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户信息");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {



        //1.0 先查询 员工对应的部门
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStoreId(reqJson.getString("storeId"));
        orgStaffRelDto.setStaffId(reqJson.getString("userId"));
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelInnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);

        Assert.listOnlyOne(orgStaffRelDtos, "未查询到相应员工对应的部门信息或查询到多条");

        //2.0 再根据 部门对应的 小区ID查询小区信息
        OrgDto orgDto = new OrgDto();
        orgDto.setOrgId(orgStaffRelDtos.get(0).getParentOrgId());
        orgDto.setStoreId(reqJson.getString("storeId"));
        orgDto.setOrgLevel("2");
        List<OrgDto> orgDtos = orgInnerServiceSMOImpl.queryOrgs(orgDto);

        Assert.listOnlyOne(orgDtos, "根据组织ID未查询到员工对应部门信息或查询到多条数据");

        int count = 0;
        List<ApiCommunityDataVo> communitys = null;
        if ("9999".equals(orgDtos.get(0).getBelongCommunityId())) {
            CommunityDto communityDto = BeanConvertUtil.covertBean(reqJson, CommunityDto.class);
            communityDto.setMemberId(reqJson.getString("storeId"));
            communityDto.setAuditStatusCd(StateConstant.AGREE_AUDIT);
            if(reqJson.containsKey("communityName")){
                communityDto.setName(reqJson.getString("communityName"));
            }
            count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);
            if (count > 0) {
                communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);
            } else {
                communitys = new ArrayList<>();
            }
        } else {
            String companyOrgId = orgDtos.get(0).getOrgId();
            OrgCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, OrgCommunityDto.class);
            orgCommunityDto.setOrgId(companyOrgId);
            count = orgCommunityInnerServiceSMOImpl.queryOrgCommunitysCount(orgCommunityDto);
            if (count > 0) {
                List<OrgCommunityDto> orgCommunityDtos = orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto);
                communitys = BeanConvertUtil.covertBeanList(orgCommunityDtos, ApiCommunityDataVo.class);
                for (OrgCommunityDto tmpOrgCommunityDto : orgCommunityDtos) {
                    for (ApiCommunityDataVo tmpApiCommunityDataVo : communitys) {
                        if (tmpOrgCommunityDto.getCommunityId().equals(tmpApiCommunityDataVo.getCommunityId())) {
                            tmpApiCommunityDataVo.setName(tmpOrgCommunityDto.getCommunityName());
                        }
                    }
                }
            } else {
                communitys = new ArrayList<>();
            }
        }


        ApiCommunityVo apiCommunityVo = new ApiCommunityVo();

        apiCommunityVo.setTotal(count);
        apiCommunityVo.setRecords(1);
        apiCommunityVo.setCommunitys(communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommunityVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IOrgStaffRelInnerServiceSMO getOrgStaffRelInnerServiceSMOImpl() {
        return orgStaffRelInnerServiceSMOImpl;
    }

    public void setOrgStaffRelInnerServiceSMOImpl(IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl) {
        this.orgStaffRelInnerServiceSMOImpl = orgStaffRelInnerServiceSMOImpl;
    }

    public IOrgInnerServiceSMO getOrgInnerServiceSMOImpl() {
        return orgInnerServiceSMOImpl;
    }

    public void setOrgInnerServiceSMOImpl(IOrgInnerServiceSMO orgInnerServiceSMOImpl) {
        this.orgInnerServiceSMOImpl = orgInnerServiceSMOImpl;
    }
}
