package com.java110.api.listener.junkRequirement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.junkRequirement.IJunkRequirementBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeJunkRequirementConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveJunkRequirementListener")
public class SaveJunkRequirementListener extends AbstractServiceApiListener {

    @Autowired
    private IJunkRequirementBMO junkRequirementBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
        Assert.hasKeyAndValue(reqJson, "classification", "请求报文中未包含classification");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
        Assert.hasKeyAndValue(reqJson, "referencePrice", "请求报文中未包含referencePrice");
        Assert.hasKeyAndValue(reqJson, "publishUserId", "请求报文中未包含publishUserId");
        Assert.hasKeyAndValue(reqJson, "publishUserName", "请求报文中未包含publishUserName");
        Assert.hasKeyAndValue(reqJson, "publishUserLink", "请求报文中未包含publishUserLink");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //reqJson.put("state", "12001");
        reqJson.put("state", "13001");
        String junkRequirementId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_junkRequirementId);
        reqJson.put("junkRequirementId",junkRequirementId);

        if (reqJson.containsKey("photos")) {
            dealPhotos(businesses,reqJson,context);
        }

        //添加单元信息
        businesses.add(junkRequirementBMOImpl.addJunkRequirement(reqJson, context));

        ResponseEntity<String> responseEntity = junkRequirementBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }

    private void dealPhotos(JSONArray businesses, JSONObject reqJson,DataFlowContext context) {
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

            businesses.add(junkRequirementBMOImpl.addPhoto(reqJson, context));
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeJunkRequirementConstant.ADD_JUNKREQUIREMENT;
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
