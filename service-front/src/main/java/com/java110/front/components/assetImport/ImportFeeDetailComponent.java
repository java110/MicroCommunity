package com.java110.front.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.front.controller.CallComponentController;
import com.java110.front.smo.assetExport.IExportRoomSMO;
import com.java110.front.smo.assetImport.IImportFeeDetailSMO;
import com.java110.front.smo.assetImport.IImportRoomFeeSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加应用组件
 */
@Component("importFeeDetail")
public class ImportFeeDetailComponent {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);


    @Autowired
    private IImportFeeDetailSMO importFeeDetailSMOImpl;


    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception {

        return importFeeDetailSMOImpl.importExcelData(pd, uploadFile);
    }



}
