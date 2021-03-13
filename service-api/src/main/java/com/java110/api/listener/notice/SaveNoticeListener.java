package com.java110.api.listener.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.notice.INoticeBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveNoticeListener")
public class SaveNoticeListener extends AbstractServiceApiPlusListener {
    @Autowired
    private INoticeBMO noticeBMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;

    @Autowired
    private IOrgCommunityInnerServiceSMO orgCommunityInnerServiceSMOImpl;

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

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

        if (!reqJson.containsKey("isAll") || StringUtil.isEmpty(reqJson.getString("isAll"))) {
            noticeBMOImpl.addNotice(reqJson, context);
            return;
        }

        //查询当前员工 的小区

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

        List<ApiCommunityDataVo> communitys = null;
        if ("9999".equals(orgDtos.get(0).getBelongCommunityId())) {
            CommunityDto communityDto = new CommunityDto();
            communityDto.setMemberId(reqJson.getString("storeId"));
            communityDto.setAuditStatusCd(StateConstant.AGREE_AUDIT);
            communitys = BeanConvertUtil.covertBeanList(communityInnerServiceSMOImpl.queryCommunitys(communityDto), ApiCommunityDataVo.class);
        } else {
            String companyOrgId = orgDtos.get(0).getOrgId();
            OrgCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, OrgCommunityDto.class);
            orgCommunityDto.setOrgId(companyOrgId);

            List<OrgCommunityDto> orgCommunityDtos = orgCommunityInnerServiceSMOImpl.queryOrgCommunitys(orgCommunityDto);
            communitys = BeanConvertUtil.covertBeanList(orgCommunityDtos, ApiCommunityDataVo.class);
        }

        if (communitys == null || communitys.size() < 1) {
            return;
        }

        for (ApiCommunityDataVo apiCommunityDataVo : communitys) {
            reqJson.put("communityId", apiCommunityDataVo.getCommunityId());
            noticeBMOImpl.addNotice(reqJson, context);
        }


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
