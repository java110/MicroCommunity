package com.java110.api.smo.file;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 添加沃文件接口
 *
 * add by wuxw 2019-06-30
 */
public interface IUploadVedioSMO {

    /**
     * 添加文件
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<Object> upload(IPageData pd, MultipartFile uploadFile) throws IOException;
}
