package com.java110.api.listener.junkRequirement;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.junkRequirement.IJunkRequirementInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerAppUserInnerServiceSMO;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.junkRequirement.JunkRequirementDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeJunkRequirementConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.junkRequirement.ApiJunkRequirementDataVo;
import com.java110.vo.api.junkRequirement.ApiJunkRequirementVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listJunkRequirementsListener")
public class ListJunkRequirementsListener extends AbstractServiceApiListener {

    @Autowired
    private IJunkRequirementInnerServiceSMO junkRequirementInnerServiceSMOImpl;
    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeJunkRequirementConstant.LIST_JUNKREQUIREMENTS;
    }


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IJunkRequirementInnerServiceSMO getJunkRequirementInnerServiceSMOImpl() {
        return junkRequirementInnerServiceSMOImpl;
    }

    public void setJunkRequirementInnerServiceSMOImpl(IJunkRequirementInnerServiceSMO junkRequirementInnerServiceSMOImpl) {
        this.junkRequirementInnerServiceSMOImpl = junkRequirementInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JunkRequirementDto junkRequirementDto = BeanConvertUtil.covertBean(reqJson, JunkRequirementDto.class);

        int count = junkRequirementInnerServiceSMOImpl.queryJunkRequirementsCount(junkRequirementDto);

        List<ApiJunkRequirementDataVo> junkRequirements = null;

        if (count > 0) {
            junkRequirements = BeanConvertUtil.covertBeanList(junkRequirementInnerServiceSMOImpl.queryJunkRequirements(junkRequirementDto), ApiJunkRequirementDataVo.class);
            refreshPhotoAndOwners(junkRequirements);
        } else {
            junkRequirements = new ArrayList<>();
        }

        ApiJunkRequirementVo apiJunkRequirementVo = new ApiJunkRequirementVo();

        apiJunkRequirementVo.setTotal(count);
        apiJunkRequirementVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiJunkRequirementVo.setJunkRequirements(junkRequirements);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiJunkRequirementVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void refreshPhotoAndOwners(List<ApiJunkRequirementDataVo> junkRequirements) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;

        if (junkRequirements == null || junkRequirements.size() < 1) {
            return;
        }

        List<String> userIds = new ArrayList<>();
        for (ApiJunkRequirementDataVo junkRequirementDataVo : junkRequirements) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(junkRequirementDataVo.getJunkRequirementId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl("/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=" + junkRequirementDataVo.getCommunityId());
                photoVos.add(photoVo);
            }

            junkRequirementDataVo.setPhotos(photoVos);
            userIds.add(junkRequirementDataVo.getPublishUserId());

        }

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(junkRequirements.get(0).getCommunityId());
        ownerAppUserDto.setUserIds(userIds.toArray(new String[userIds.size()]));

        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            return;
        }

        for (OwnerAppUserDto tmpOwnerAppUserdto : ownerAppUserDtos) {
            for (ApiJunkRequirementDataVo apiJunkRequirementDataVo : junkRequirements) {
                if (tmpOwnerAppUserdto.getAppUserId().equals(apiJunkRequirementDataVo.getPublishUserId())) {
                    apiJunkRequirementDataVo.setMemberId(tmpOwnerAppUserdto.getMemberId());
                }
            }
        }
    }
}
