package com.java110.api.controller.app.file;

import com.java110.core.client.FileUploadTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.userDownloadFile.UserDownloadFileDto;
import com.java110.intf.job.IUserDownloadFileV1InnerServiceSMO;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 用户 下载中心中下载
 */
@RestController
@RequestMapping(value = "/app/file/userfile")
public class UserDownloadFileController {
    private final static Logger logger = LoggerFactory.getLogger(UserDownloadFileController.class);

    @Autowired
    private IUserDownloadFileV1InnerServiceSMO userDownloadFileV1InnerServiceSMOImpl;

    @Autowired
    private FileUploadTemplate fileUploadTemplate;


    @RequestMapping(path = "/download/{downloadId}", method = RequestMethod.GET)
    public void download(@PathVariable String downloadId, HttpServletRequest request, HttpServletResponse response) {

        logger.debug("用户开始下载文件" + downloadId);

        String userId = request.getHeader("user-id");

        UserDownloadFileDto userDownloadFileDto = new UserDownloadFileDto();
        userDownloadFileDto.setDownloadId(downloadId);
        userDownloadFileDto.setDownloadUserId(userId);
        List<UserDownloadFileDto> userDownloadFileDtos = userDownloadFileV1InnerServiceSMOImpl.queryUserDownloadFiles(userDownloadFileDto);
        Assert.listOnlyOne(userDownloadFileDtos, "文件不存在");
        String tempUrl = userDownloadFileDtos.get(0).getTempUrl();
        String fileName = userDownloadFileDtos.get(0).getFileTypeName()+tempUrl.substring(tempUrl.lastIndexOf("/"));

        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("application/octet-stream");

        InputStream is = null;
        BufferedInputStream bis = null;
        byte[] buffer = new byte[1024];

        try {
            is = fileUploadTemplate.downloadFile(userDownloadFileDtos.get(0).getTempUrl());
            bis = new BufferedInputStream(is);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
