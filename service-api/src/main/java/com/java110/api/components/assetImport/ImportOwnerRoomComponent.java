package com.java110.api.components.assetImport;

import com.java110.api.smo.assetImport.IImportOwnerRoomSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加应用组件
 *
 * @author fqz
 * @date 2021-12-21
 */
@Component("importOwnerRoom")
public class ImportOwnerRoomComponent {

    private final static Logger logger = LoggerFactory.getLogger(ImportOwnerRoomComponent.class);

    @Autowired
    private IImportOwnerRoomSMO importOwnerRoomSMOImpl;


    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception {
        return importOwnerRoomSMOImpl.importExcelData(pd, uploadFile);
    }


}
