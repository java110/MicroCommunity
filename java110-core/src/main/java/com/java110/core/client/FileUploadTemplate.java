package com.java110.core.client;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class FileUploadTemplate {


    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private JSchFtpUploadTemplate jSchFtpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;


    public String saveFile(InputStream inputStream,String fileName){

        String ossSwitch = MappingCache.getValue(OSSUtil.DOMAIN, OSSUtil.OSS_SWITCH);

        if (OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            fileName = ossUploadTemplate.upload(inputStream, fileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            fileName = cosUploadTemplate.upload(inputStream, fileName);
        } else {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);

            fileName = ftpUploadTemplate.upload(inputStream, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword, fileName);
        }
        return fileName;
    }
}
