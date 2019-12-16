package com.java110.web.smo.file.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.FtpUpload;
import com.java110.web.smo.file.IAddFileSMO;
import com.java110.web.smo.file.IUploadVedioSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("uploadVedioSMOImpl")
public class UploadVedioSMOImpl extends BaseComponentSMO implements IUploadVedioSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Java110Properties java110Properties;


    @Override
    public ResponseEntity<Object> upload(IPageData pd, MultipartFile uploadFile) throws IOException {

        //JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if (uploadFile.getSize() > 200 * 1024 * 1024) {
            throw new IllegalArgumentException("上传文件超过200兆");
        }

        String fileName = FtpUpload.upload(uploadFile, java110Properties.getFtpServer(),
                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                java110Properties.getFtpUserPassword(), java110Properties.getFtpPath());
        JSONObject outParam = new JSONObject();
        outParam.put("fileName", uploadFile.getOriginalFilename());
        outParam.put("realFileName", fileName);

        return new ResponseEntity<Object>(outParam.toJSONString(), HttpStatus.OK);

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Java110Properties getJava110Properties() {
        return java110Properties;
    }

    public void setJava110Properties(Java110Properties java110Properties) {
        this.java110Properties = java110Properties;
    }
}
