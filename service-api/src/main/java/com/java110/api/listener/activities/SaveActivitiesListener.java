package com.java110.api.listener.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.activities.IActivitiesBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.activitiesType.ActivitiesTypeDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.org.OrgCommunityDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IActivitiesTypeInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IOrgCommunityInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.po.activities.ActivitiesPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveActivitiesListener")
public class SaveActivitiesListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IActivitiesBMO activitiesBMOImpl;

    @Autowired
    private IActivitiesTypeInnerServiceSMO activitiesTypeInnerServiceSMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "title", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择活动类型");
        Assert.hasKeyAndValue(reqJson, "headerImg", "必填，请选择头部照片");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写活动内容");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写用户名称");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        if (!reqJson.containsKey("isMoreCommunity") || "N".equals(reqJson.getString("isMoreCommunity"))) {
            addActivities(context, reqJson);
            return;
        }

        List<ApiCommunityDataVo> communityDataVos = getCommunitys(reqJson);

        if (communityDataVos == null || communityDataVos.size() < 1) {
            return;
        }

        ActivitiesTypeDto activitiesTypeDto = new ActivitiesTypeDto();
        activitiesTypeDto.setCommunityId(reqJson.getString("communityId"));
        activitiesTypeDto.setTypeCd(reqJson.getString("typeCd"));
        List<ActivitiesTypeDto> oneActivitiesTypeDtos = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypes(activitiesTypeDto);

        Assert.listOnlyOne(oneActivitiesTypeDtos, "通知类型不存在");
        List<ActivitiesTypeDto> activitiesTypeDtos = null;
        for (ApiCommunityDataVo apiCommunityDataVo : communityDataVos) {
            reqJson.put("communityId", apiCommunityDataVo.getCommunityId());
            activitiesTypeDto = new ActivitiesTypeDto();
            activitiesTypeDto.setCommunityId(reqJson.getString("communityId"));
            activitiesTypeDto.setTypeName(oneActivitiesTypeDtos.get(0).getTypeName());
            activitiesTypeDtos = activitiesTypeInnerServiceSMOImpl.queryActivitiesTypes(activitiesTypeDto);
            if (activitiesTypeDtos == null || activitiesTypeDtos.size() < 1) {
                continue;
            }
            reqJson.put("typeCd", activitiesTypeDtos.get(0).getTypeCd());
            addActivities(context, reqJson);
        }
    }

    public List<ApiCommunityDataVo> getCommunitys(JSONObject reqJson) {

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

        return communitys;
    }

    public void addActivities(DataFlowContext context, JSONObject reqJson) {
        reqJson.put("activitiesId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_activitiesId));
        if (reqJson.containsKey("headerImg") && !StringUtils.isEmpty(reqJson.getString("headerImg"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("headerImg"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

            reqJson.put("headerImg", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            FileRelPo fileRelPo = new FileRelPo();
            fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            fileRelPo.setFileRealName(reqJson.getString("headerImg"));
            fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
            fileRelPo.setObjId(reqJson.getString("activitiesId"));
            fileRelPo.setSaveWay("table");
            fileRelPo.setRelTypeCd("70000");
            super.insert(context, fileRelPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);

        }
        ActivitiesPo activitiesPo = BeanConvertUtil.covertBean(reqJson, ActivitiesPo.class);
        activitiesPo.setReadCount("0");
        activitiesPo.setLikeCount("0");
        activitiesPo.setCollectCount("0");
        activitiesPo.setState("11000");
        //添加单元信息
        super.insert(context, activitiesPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ACTIVITIES);
    }


    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.ADD_ACTIVITIES;
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
