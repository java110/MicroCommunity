package com.java110.api.components.assetImport;

import com.java110.api.controller.component.CallComponentController;
import com.java110.api.smo.assetExport.IExportRoomSMO;
import com.java110.api.smo.assetImport.IImportResourceStoreSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加应用组件
 */
@Component("importResourceStore")
public class ImportResourceStoreComponent {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);


    @Autowired
    private IImportResourceStoreSMO importResourceStoreSMOImpl;

    @Autowired
    private IExportRoomSMO exportRoomSMOImpl;

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception {

        return importResourceStoreSMOImpl.importExcelData(pd, uploadFile);
    }

}
