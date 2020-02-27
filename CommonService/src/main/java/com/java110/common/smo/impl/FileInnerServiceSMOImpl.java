package com.java110.common.smo.impl;

import com.java110.common.dao.IFileServiceDao;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.core.client.FtpUploadTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FileInnerServiceSMOImpl extends BaseServiceSMO implements IFileInnerServiceSMO {

    @Autowired
    private IFileServiceDao fileServiceDaoImpl;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;


    @Override
    public String saveFile(@RequestBody FileDto fileDto) {

        //int saveFileFlag = fileServiceDaoImpl.saveFile(BeanConvertUtil.beanCovertMap(fileDto));


        String fileName = ftpUploadTemplate.upload(fileDto.getContext(), java110Properties.getFtpServer(),
                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                java110Properties.getFtpUserPassword(), java110Properties.getFtpPath());

        return fileName;
    }

    @Override
    public List<FileDto> queryFiles(@RequestBody FileDto fileDto) {
        //return BeanConvertUtil.covertBeanList(fileServiceDaoImpl.getFiles(BeanConvertUtil.beanCovertMap(fileDto)), FileDto.class);
        List<FileDto> fileDtos = new ArrayList<>();
        String fileName = fileDto.getFileSaveName();
        String ftpPath = java110Properties.getFtpPath();
        if (fileName.contains("/")) {
            ftpPath += fileName.substring(0, fileName.lastIndexOf("/")+1);
            fileName = fileName.substring(fileName.lastIndexOf("/")+1, fileName.length());
        }
        byte[] fileImg = ftpUploadTemplate.downFileByte(ftpPath, fileName, java110Properties.getFtpServer(),
                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                java110Properties.getFtpUserPassword());

        String context = new BASE64Encoder().encode(fileImg);

        fileDto.setContext(context);
        fileDtos.add(fileDto);
        return fileDtos;
    }

    public IFileServiceDao getFileServiceDaoImpl() {
        return fileServiceDaoImpl;
    }

    public void setFileServiceDaoImpl(IFileServiceDao fileServiceDaoImpl) {
        this.fileServiceDaoImpl = fileServiceDaoImpl;
    }
}
