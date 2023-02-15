package com.java110.core.client;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.OSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class FileUploadTemplate {

    private static final String ROOT_PATH = "hc/";


    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private JSchFtpUploadTemplate jSchFtpUploadTemplate;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;


    public String saveFile(InputStream inputStream,String fileName){

        String newfileName = ROOT_PATH+ fileName;

        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);

        if (OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            newfileName = ossUploadTemplate.upload(inputStream, newfileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            newfileName = cosUploadTemplate.upload(inputStream, newfileName);
        } else {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);

            newfileName = ftpUploadTemplate.upload(inputStream, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword, newfileName);
        }
        return newfileName;
    }

    public InputStream downloadFile(String fileName){

        InputStream inputStream = null;

        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, OSSUtil.OSS_SWITCH);

        if (OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
            inputStream = ossUploadTemplate.download(fileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            inputStream = cosUploadTemplate.download(fileName);
        } else {
            String ftpServer = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_SERVER);
            int ftpPort = Integer.parseInt(MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_PORT));
            String ftpUserName = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERNAME);
            String ftpUserPassword = MappingCache.getValue(FtpUploadTemplate.FTP_DOMAIN, FtpUploadTemplate.FTP_USERPASSWORD);

            inputStream = ftpUploadTemplate.download(fileName, ftpServer,
                    ftpPort, ftpUserName,
                    ftpUserPassword);
        }

        return inputStream;
    }
}
