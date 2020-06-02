package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.applicationKey.IApplicationKeyBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerRoomRelInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName ApplicationKeyListener
 * @Description 钥匙申请类
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("applicationKeyListener")
public class ApplicationKeyListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IApplicationKeyBMO applicationKeyBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    private static Logger logger = LoggerFactory.getLogger(ApplicationKeyListener.class);

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_APPLICATION_KEY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含房屋信息");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        //Assert.jsonObjectHaveKey(paramIn, "ownerTypeCd", "请求报文中未包含sex"); //这个不需要 这个直接写成钥匙申请，临时人员
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.jsonObjectHaveKey(reqJson, "idCard", "请求报文中未包含身份证号");
        Assert.jsonObjectHaveKey(reqJson, "ownerPhoto", "请求报文中未包含照片信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        logger.debug("ServiceDataFlowEvent : {}", event);


        //添加小区楼
        applicationKeyBMOImpl.addMember(reqJson,context);


        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(reqJson.getString("ownerPhoto"));
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(reqJson.getString("communityId"));
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        reqJson.put("ownerPhotoId", fileDto.getFileId());
        reqJson.put("fileSaveName", fileName);

        applicationKeyBMOImpl.addOwnerKeyPhoto(reqJson, context);
    }


    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }

    public IOwnerRoomRelInnerServiceSMO getOwnerRoomRelInnerServiceSMOImpl() {
        return ownerRoomRelInnerServiceSMOImpl;
    }

    public void setOwnerRoomRelInnerServiceSMOImpl(IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl) {
        this.ownerRoomRelInnerServiceSMOImpl = ownerRoomRelInnerServiceSMOImpl;
    }
}
