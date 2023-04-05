package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.visit.VisitDto;
import com.java110.dto.visitSetting.VisitSettingDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
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

@Java110CmdDoc(title = "查询访客",
        description = "供pc端查询访客人员",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/visit.listVisits",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "visit.listVisits",
        seq = 22
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int", length = 11, remark = "分页信息"),
        @Java110ParamDoc(name = "row", type = "int", length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "visits", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "visits", name = "vId", type = "String", remark = "访客ID"),
                @Java110ParamDoc(parentNodeName = "visits", name = "vName", type = "String", remark = "访客名称"),
                @Java110ParamDoc(parentNodeName = "visits", name = "visitGender", type = "String", remark = "访客性别"),
                @Java110ParamDoc(parentNodeName = "visits", name = "phoneNumber", type = "String", remark = "手机号"),
                @Java110ParamDoc(parentNodeName = "visits", name = "visitTime", type = "String", remark = "访问时间"),})

@Java110ExampleDoc(
        reqBody = "ttp://localhost:3000/app/visit.listVisits?page=1&row=10&communityId=2022121921870161",
        resBody = "{\"page\":0,\"records\":0,\"rows\":0,\"total\":0,\"visits\":[]}"
)
@Java110Cmd(serviceCode = "visit.listVisits")
public class ListVisitsCmd extends Cmd {

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
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (reqJson.containsKey("addVisitType") && !StringUtil.isEmpty(reqJson.getString("addVisitType"))
                && "initAddVisitParameter".equals(reqJson.getString("addVisitType"))) {
            //预约车免费时长
            String freeTime = "";
            String numStr = "";
            if (visitSettingDtos != null && visitSettingDtos.size() == 1) {
                freeTime = visitSettingDtos.get(0).getCarFreeTime();
                numStr = visitSettingDtos.get(0).getVisitNumber();
            }
            /*String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
            String numStr = CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER);*/
            int number = 999;
            if (StringUtil.isInteger(numStr)) {
                number = Integer.parseInt(numStr);
            }
            Map initAddVisitParameter = new HashMap();
            initAddVisitParameter.put("freeTime", freeTime);
            initAddVisitParameter.put("freeTimes", number);
            //业主端获取访客登记相关配置参数
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(initAddVisitParameter), HttpStatus.OK);
        } else {
            VisitDto visitDto = BeanConvertUtil.covertBean(reqJson, VisitDto.class);
            if ("PC".equals(reqJson.getString("channel"))) {
                visitDto.setCreateUserId("");
            }
            int count = visitV1InnerServiceSMO.queryVisitsCount(visitDto);
            String visitorCode = "";
            if (visitSettingDtos != null && visitSettingDtos.size() == 1) {
                visitorCode = visitSettingDtos.get(0).getVisitorCode();
            }
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
            if (visits == null || visits.size() < 1) {
                return;
            }
            //刷入流程ID
            refreshOwners(visits, reqJson);
            //刷入流程ID
            refreshSetting(visits, reqJson);
            // 刷入人脸
            refreshPhoto(visits, reqJson);
            ApiVisitVo apiVisitVo = new ApiVisitVo();
            apiVisitVo.setTotal(count);
            apiVisitVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
            apiVisitVo.setVisits(visits);
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiVisitVo), HttpStatus.OK);
        }
        context.setResponseEntity(responseEntity);
    }

    private void refreshOwners(List<ApiVisitDataVo> visits, JSONObject reqJson) {
        if (visits == null || visits.size() < 1) {
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
       List<OwnerDto> ownerDtos =  ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        for(ApiVisitDataVo apiVisitDataVo: visits){
            for(OwnerDto tmpOwnerDto : ownerDtos){
                if(StringUtil.isEmpty(apiVisitDataVo.getOwnerId())){
                    continue;
                }
                if(!apiVisitDataVo.getOwnerId().equals(tmpOwnerDto.getOwnerId())){
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

    private void refreshSetting(List<ApiVisitDataVo> visits, JSONObject reqJson) {
        VisitSettingDto visitSettingDto = new VisitSettingDto();
        visitSettingDto.setCommunityId(reqJson.getString("communityId"));
        List<VisitSettingDto> visitSettingDtos = visitSettingV1InnerServiceSMOImpl.queryVisitSettings(visitSettingDto);
        if (visitSettingDtos == null || visitSettingDtos.size() < 1) {
            return;
        }
        if (visits == null || visits.size() < 1) {
            return;
        }
        for (ApiVisitDataVo visitDataVo : visits) {
            visitDataVo.setFlowId(visitSettingDtos.get(0).getFlowId());
        }
    }
}
