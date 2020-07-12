package com.java110.api.listener.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.file.FileRelDto;
import com.java110.utils.constant.ServiceCodeComplaintConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.complaint.ApiComplaintDataVo;
import com.java110.vo.api.complaint.ApiComplaintVo;
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
@Java110Listener("listComplaintsListener")
public class ListComplaintsListener extends AbstractServiceApiListener {

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;
    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeComplaintConstant.LIST_COMPLAINTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IComplaintInnerServiceSMO getComplaintInnerServiceSMOImpl() {
        return complaintInnerServiceSMOImpl;
    }

    public void setComplaintInnerServiceSMOImpl(IComplaintInnerServiceSMO complaintInnerServiceSMOImpl) {
        this.complaintInnerServiceSMOImpl = complaintInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);

        int count = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);

        List<ApiComplaintDataVo> complaints = null;

        if (count > 0) {
            List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);
            complaintDtos = freshCurrentUser(complaintDtos);
            complaints = BeanConvertUtil.covertBeanList(complaintDtos, ApiComplaintDataVo.class);
            refreshPhotos(complaints);
        } else {
            complaints = new ArrayList<>();
        }

        ApiComplaintVo apiComplaintVo = new ApiComplaintVo();

        apiComplaintVo.setTotal(count);
        apiComplaintVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiComplaintVo.setComplaints(complaints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiComplaintVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private List<ComplaintDto> freshCurrentUser(List<ComplaintDto> complaintDtos) {
        List<ComplaintDto> tmpComplaintDtos = new ArrayList<>();
        for (ComplaintDto complaintDto : complaintDtos) {
            complaintDto = complaintUserInnerServiceSMOImpl.getTaskCurrentUser(complaintDto);
            tmpComplaintDtos.add(complaintDto);
        }

        return tmpComplaintDtos;
    }

    private void refreshPhotos(List<ApiComplaintDataVo> complaints) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        for (ApiComplaintDataVo complaintDataVo : complaints) {

            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(complaintDataVo.getComplaintId());
            fileRelDto.setRelTypeCd("13000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl("/callComponent/download/getFile/file?fileId=" + tmpFileRelDto.getFileRealName() + "&communityId=" + complaintDataVo.getCommunityId());
                photoVos.add(photoVo);
            }
            complaintDataVo.setPhotos(photoVos);
        }
    }

}
