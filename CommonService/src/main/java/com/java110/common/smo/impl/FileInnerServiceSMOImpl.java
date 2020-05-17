package com.java110.common.smo.impl;

import com.java110.common.dao.IFileServiceDao;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.client.JSchFtpUploadTemplate;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.core.client.FtpUploadTemplate;
import com.java110.utils.util.Base64Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FileInnerServiceSMOImpl extends BaseServiceSMO implements IFileInnerServiceSMO {

    @Autowired
    private IFileServiceDao fileServiceDaoImpl;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private FtpUploadTemplate ftpUploadTemplate;

    @Autowired
    private JSchFtpUploadTemplate jSchFtpUploadTemplate;


    @Override
    public String saveFile(@RequestBody FileDto fileDto) {

        //int saveFileFlag = fileServiceDaoImpl.saveFile(BeanConvertUtil.beanCovertMap(fileDto));


        String fileName = ftpUploadTemplate.upload(fileDto.getContext(), java110Properties.getFtpServer(),
                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                java110Properties.getFtpUserPassword(), java110Properties.getFtpPath());

//        String fileName = jSchFtpUploadTemplate.upload(fileDto.getContext(), java110Properties.getFtpServer(),
//                java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
//                java110Properties.getFtpUserPassword(), java110Properties.getFtpPath());
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
        try {
            File file = new File("/home/hc/img/"+ UUID.randomUUID().toString()+".jpg");
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();// 能创建多级目录
            }
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write(fileImg);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
       //String context = new BASE64Encoder().encode(fileImg);
        String context = Base64Convert.byteToBase64(fileImg);

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
