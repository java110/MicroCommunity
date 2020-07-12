package com.java110.api.listener.inspectionTaskDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspectionTask.IInspectionTaskBMO;
import com.java110.api.bmo.inspectionTaskDetail.IInspectionTaskDetailBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskInnerServiceSMO;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.dto.inspectionTask.InspectionTaskDto;
import com.java110.dto.inspectionTaskDetail.InspectionTaskDetailDto;
import com.java110.entity.center.AppService;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeInspectionTaskDetailConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 保存巡检任务明细侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateInspectionTaskDetailListener")
public class UpdateInspectionTaskDetailListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionTaskDetailBMO inspectionTaskDetailBMOImpl;

    @Autowired
    private IInspectionTaskBMO inspectionTaskBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;


    @Autowired
    private IInspectionInnerServiceSMO inspectionInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "taskDetailId", "请求报文中未包含taskDetailId");
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "请求报文中未包含inspectionId");
        Assert.hasKeyAndValue(reqJson, "photos", "请求报文中未包含照片");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        if (reqJson.containsKey("photos")) {
            dealPhotos(businesses, reqJson, context);
        }

        //添加单元信息
        reqJson.put("state", "20200407");//巡检点完成
        inspectionTaskDetailBMOImpl.updateInspectionTaskDetail(reqJson, context);

        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setTaskId(reqJson.getString("taskId"));
        inspectionTaskDto.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDto.setState("20200405");
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);

        if (inspectionTaskDtos != null && inspectionTaskDtos.size() > 0) {
            reqJson.put("state", "20200406");
            inspectionTaskBMOImpl.updateInspectionTask(reqJson, context);
        }
        ResponseEntity<String> responseEntity = inspectionTaskDetailBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }


        //判断 巡检点是否都有巡检完
        if (inspectionTaskDtos == null || inspectionTaskDtos.size() == 0) {
            return;
        }

        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setCommunityId(reqJson.getString("communityId"));
        inspectionTaskDetailDto.setTaskId(reqJson.getString("taskId"));
        inspectionTaskDetailDto.setState("20200407");
        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);

        if (count > 0) {//说明还没有巡检完
            return;
        }
        businesses = new JSONArray();
        reqJson.put("state", "20200407");//巡检完成
        inspectionTaskBMOImpl.updateInspectionTask(reqJson, context);
        responseEntity = inspectionTaskDetailBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
    }


    private void dealPhotos(JSONArray businesses, JSONObject reqJson, DataFlowContext context) {
        JSONArray photos = reqJson.getJSONArray("photos");
        JSONObject photo = null;
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            photo = photos.getJSONObject(photoIndex);
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(photo.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            inspectionTaskBMOImpl.addPhoto(reqJson, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskDetailConstant.UPDATE_INSPECTIONTASKDETAIL;
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
