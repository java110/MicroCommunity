package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.common.IFileInnerServiceSMO;
import com.java110.core.smo.common.IFileRelInnerServiceSMO;
import com.java110.core.smo.user.IOwnerInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName EditOwnerListener
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:19
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/
@Java110Listener("editOwnerListener")
public class EditOwnerListener extends AbstractServiceApiPlusListener {

    private static Logger logger = LoggerFactory.getLogger(EditOwnerListener.class);

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_EDIT_OWNER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(reqJson, "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        // Assert.jsonObjectHaveKey(paramIn, "idCard", "请求报文中未包含身份证号");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        if (!reqJson.containsKey("ownerId") || "1001".equals(reqJson.getString("ownerTypeCd"))) {
            reqJson.put("ownerId", reqJson.getString("memberId"));
        }

        if (reqJson.containsKey("ownerPhoto") && !StringUtils.isEmpty(reqJson.getString("ownerPhoto"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("ownerPhoto"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("ownerPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            ownerBMOImpl.editOwnerPhoto(reqJson, context);

        }
        ownerBMOImpl.editOwner(reqJson, context);
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IFileInnerServiceSMO getFileInnerServiceSMOImpl() {
        return fileInnerServiceSMOImpl;
    }

    public void setFileInnerServiceSMOImpl(IFileInnerServiceSMO fileInnerServiceSMOImpl) {
        this.fileInnerServiceSMOImpl = fileInnerServiceSMOImpl;
    }

    public IFileRelInnerServiceSMO getFileRelInnerServiceSMOImpl() {
        return fileRelInnerServiceSMOImpl;
    }

    public void setFileRelInnerServiceSMOImpl(IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl) {
        this.fileRelInnerServiceSMOImpl = fileRelInnerServiceSMOImpl;
    }

    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }
}
