package com.java110.web.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName IAssetImportSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:13
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
public interface IAssetExportSMO {

    /**
     * 导入excel数据
     * @param pd 前台数据封装
     * @return ResponseEntity
     */
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception;
}
