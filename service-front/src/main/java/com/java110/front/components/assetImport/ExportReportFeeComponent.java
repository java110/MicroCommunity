package com.java110.front.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.front.smo.assetExport.IExportFeeManualCollectionSMO;
import com.java110.front.smo.assetExport.IExportReportFeeSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加应用组件
 */
@Component("exportReportFee")
public class ExportReportFeeComponent {

    private final static Logger logger = LoggerFactory.getLogger(ExportReportFeeComponent.class);

    @Autowired
    private IExportReportFeeSMO exportExcelData;

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<Object> exportData(IPageData pd) throws Exception {

        return exportExcelData.exportExcelData(pd);
    }


}
