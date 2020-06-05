package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.applicationKey.IApplicationKeyBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.common.IFileInnerServiceSMO;
import com.java110.core.smo.common.IFileRelInnerServiceSMO;
import com.java110.core.smo.common.IApplicationKeyInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存钥匙申请侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateApplicationKeyListener")
public class UpdateApplicationKeyListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IApplicationKeyBMO applicationKeyBMOImpl;

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(reqJson, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(reqJson, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(reqJson, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "必填，位置不能为空");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "必填，未选择位置对象");
        Assert.hasKeyAndValue(reqJson, "typeFlag", "必填，未选择钥匙类型");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //添加钥匙信息
        applicationKeyBMOImpl.updateApplicationKey(reqJson, context);

        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(reqJson.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("applicationKeyPhotoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            applicationKeyBMOImpl.editApplicationKeyPhoto(reqJson, context);

        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.UPDATE_APPLICATIONKEY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
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
}
