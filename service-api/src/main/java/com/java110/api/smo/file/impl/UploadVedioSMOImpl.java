package com.java110.api.smo.file.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.client.CosUploadTemplate;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.context.IPageData;
import com.java110.api.smo.file.IUploadVedioSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.OSSUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("uploadVedioSMOImpl")
public class UploadVedioSMOImpl extends DefaultAbstractComponentSMO implements IUploadVedioSMO {
    private static final String ROOT_PATH = "hc/";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;

    @Override
    public ResponseEntity<Object> upload(IPageData pd, MultipartFile uploadFile) throws IOException {

        //JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if (uploadFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("上传文件超过1024兆");
        }

//        String fileName = ftpUploadTemplate.upload(uploadFile, java110Properties.getFtpServer(),
//                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
//                java110Properties.getFtpUserPassword(), "hc/");
//
        String fileName = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);
        if (StringUtil.isEmpty(ossSwitch) || !OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);

            fileName = ftpUploadTemplate.upload(uploadFile, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword, ROOT_PATH);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            fileName = cosUploadTemplate.upload(uploadFile, java110Properties.getFtpServer(),
                    java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                    java110Properties.getFtpUserPassword(), ROOT_PATH);
        } else {

            fileName = ossUploadTemplate.upload(uploadFile, java110Properties.getFtpServer(),
                    java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                    java110Properties.getFtpUserPassword(), ROOT_PATH);
        }
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
