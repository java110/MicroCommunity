package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visit.VisitSettingDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.visit.ApiVisitDataVo;
import com.java110.vo.api.visit.ApiVisitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "visit.listAdminVisits")
public class ListAdminVisitsCmd extends Cmd {

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMO;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);


        VisitDto visitDto = BeanConvertUtil.covertBean(reqJson, VisitDto.class);
        visitDto.setPhoneNumber(reqJson.getString("ownerTel"));

        int count = visitV1InnerServiceSMO.queryVisitsCount(visitDto);
        String visitorCode = "";

        List<ApiVisitDataVo> visits = new ArrayList<>();
        if (count > 0) {
            List<VisitDto> visitDtos = visitV1InnerServiceSMO.queryVisits(visitDto);
            for (VisitDto visit : visitDtos) {
                ApiVisitDataVo apiVisitDataVo = BeanConvertUtil.covertBean(visit, ApiVisitDataVo.class);
                apiVisitDataVo.setVisitorCode(visitorCode);
                if (!StringUtil.isEmpty(visit.getFileSaveName())) {
                    apiVisitDataVo.setUrl(visit.getFileSaveName());
                }
                visits.add(apiVisitDataVo);
            }
        } else {
            visits = new ArrayList<>();
        }

        //刷入流程ID
        refreshOwners(visits, reqJson);
        // 刷入人脸
        refreshPhoto(visits, reqJson);
        ApiVisitVo apiVisitVo = new ApiVisitVo();
        apiVisitVo.setTotal(count);
        apiVisitVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiVisitVo.setVisits(visits);
        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiVisitVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshOwners(List<ApiVisitDataVo> visits, JSONObject reqJson) {
        if (ListUtil.isNull(visits)) {
            return;
        }
        List<String> ownerIds = new ArrayList<>();
        for (ApiVisitDataVo apiVisitDataVo : visits) {
            ownerIds.add(apiVisitDataVo.getOwnerId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        for (ApiVisitDataVo apiVisitDataVo : visits) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (StringUtil.isEmpty(apiVisitDataVo.getOwnerId())) {
                    continue;
                }
                if (!apiVisitDataVo.getOwnerId().equals(tmpOwnerDto.getOwnerId())) {
                    continue;
                }
                apiVisitDataVo.setOwnerName(tmpOwnerDto.getName());
                apiVisitDataVo.setOwnerTel(tmpOwnerDto.getLink());
            }
        }
    }

    private void refreshPhoto(List<ApiVisitDataVo> visits, JSONObject reqJson) {
        List<String> vIds = new ArrayList<>();
        for (ApiVisitDataVo apiVisitDataVo : visits) {
            vIds.add(apiVisitDataVo.getvId());
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(vIds.toArray(new String[vIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
        for (ApiVisitDataVo apiVisitDataVo : visits) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!apiVisitDataVo.getvId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }
                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    apiVisitDataVo.setUrl(tmpFileRelDto.getFileSaveName());
                } else {
                    apiVisitDataVo.setUrl(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
    }

}
