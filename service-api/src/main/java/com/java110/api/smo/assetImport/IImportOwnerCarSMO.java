package com.java110.api.smo.assetImport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fqz
 * @date 2021-12-21
 */
public interface IImportOwnerCarSMO {

    /**
     * 导入excel数据
     *
     * @param pd         前台数据封装
     * @param uploadFile excel 文件
     * @return ResponseEntity
     */
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception;

}
