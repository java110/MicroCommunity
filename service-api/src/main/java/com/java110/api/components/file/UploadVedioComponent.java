package com.java110.api.components.file;

import com.java110.core.context.IPageData;
import com.java110.api.smo.file.IUploadVedioSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 应用组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("uploadVedio")
public class UploadVedioComponent {

    @Autowired
    private IUploadVedioSMO uploadVedioSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<Object> upload(IPageData pd,MultipartFile uploadFile) throws IOException {
        return uploadVedioSMOImpl.upload(pd,uploadFile);
    }

    public IUploadVedioSMO getUploadVedioSMOImpl() {
        return uploadVedioSMOImpl;
    }

    public void setUploadVedioSMOImpl(IUploadVedioSMO uploadVedioSMOImpl) {
        this.uploadVedioSMOImpl = uploadVedioSMOImpl;
    }
}
