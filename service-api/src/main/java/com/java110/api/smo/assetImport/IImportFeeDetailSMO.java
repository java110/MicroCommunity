package com.java110.api.smo.assetImport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName IImportFeeDetailSMO
 * @Description TODO 导入之前缴费历史
 * @Author wuxw
 * @Date 2019/9/23 23:13
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
public interface IImportFeeDetailSMO {

    /**
     * 导入excel数据
     * @param pd 前台数据封装
     * @param uploadFile excel 文件
     * @return ResponseEntity
     */
    public ResponseEntity<String> importExcelData(IPageData pd,  MultipartFile uploadFile) throws Exception;
}
