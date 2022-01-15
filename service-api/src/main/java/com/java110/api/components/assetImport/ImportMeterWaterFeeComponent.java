package com.java110.api.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.api.controller.component.CallComponentController;
import com.java110.api.smo.assetExport.IExportMeterWaterSMO;
import com.java110.api.smo.assetImport.IImportMeterWaterFeeSMO;
import com.java110.core.context.PageData;
import com.java110.dto.app.AppDto;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加应用组件
 */
@Component("importMeterWaterFee")
public class ImportMeterWaterFeeComponent {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);


    @Autowired
    private IImportMeterWaterFeeSMO importMeterWaterFeeSMOImpl;

    @Autowired
    private IExportMeterWaterSMO exportMeterWaterSMOImpl;

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception {

        return importMeterWaterFeeSMOImpl.importExcelData(pd, uploadFile);
    }

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData2(IPageData pd, MultipartFile uploadFile) throws Exception {

        return importMeterWaterFeeSMOImpl.importExcelData2(pd, uploadFile);
    }

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<Object> exportData(IPageData pd) throws Exception {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(),pd.getToken(), pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), AppDto.WEB_APP_ID,pd.getHeaders());
        return exportMeterWaterSMOImpl.exportExcelData(newPd);
    }
    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<Object> exportData2(IPageData pd) throws Exception {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(),pd.getToken(), pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), AppDto.WEB_APP_ID,pd.getHeaders());
        return exportMeterWaterSMOImpl.exportExcelData2(newPd);
    }


}
