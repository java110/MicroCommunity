package com.java110.community.cmd.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailDataVo;
import com.java110.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailVo;
import com.java110.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionTaskDetail.listInspectionTaskDetails")
public class ListInspectionTaskDetailsCmd extends Cmd {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionTaskDetailDto inspectionTaskDetailDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailDto.class);
        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);
        List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails = null;
        if (count > 0) {
            inspectionTaskDetails = BeanConvertUtil.covertBeanList(inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto), ApiInspectionTaskDetailDataVo.class);
            refreshPhotos(inspectionTaskDetails);
        } else {
            inspectionTaskDetails = new ArrayList<>();
        }
        ApiInspectionTaskDetailVo apiInspectionTaskDetailVo = new ApiInspectionTaskDetailVo();
        apiInspectionTaskDetailVo.setTotal(count);
        apiInspectionTaskDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionTaskDetailVo.setInspectionTaskDetails(inspectionTaskDetails);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionTaskDetailVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void refreshPhotos(List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for (ApiInspectionTaskDetailDataVo inspectionTaskDetail : inspectionTaskDetails) {
            if(!"20200407".equals(inspectionTaskDetail.getState())){
                continue;
            }
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(inspectionTaskDetail.getTaskDetailId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName().startsWith("http")?tmpFileRelDto.getFileRealName():imgUrl+tmpFileRelDto.getFileRealName());
                photoVos.add(photoVo);
            }
            inspectionTaskDetail.setPhotos(photoVos);
        }
    }
}
