package com.java110.api.components.meterWaterImport;

import com.java110.core.context.IPageData;
import com.java110.api.smo.assetExport.IAssetExportSMO;
import com.java110.api.smo.meterWaterImport.IMeterWaterImportSMO;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 费用导入
 */
@Component("feeImport")
public class MeterWaterImportComponent {

    private final static Logger logger = LoggerFactory.getLogger(MeterWaterImportComponent.class);


    @Autowired
    private IMeterWaterImportSMO meterWaterImportSMOmpl;

    @Autowired
    private IAssetExportSMO assetExportSMOImpl;

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception{

        return meterWaterImportSMOmpl.importExcelData(pd,uploadFile);
    }

    /**
     * 资产导出
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public ResponseEntity<Object> exitCommunityData(IPageData pd) throws Exception {
        return assetExportSMOImpl.exportExcelData(pd);
    }


    public IMeterWaterImportSMO getMeterWaterImportSMOmpl() {
        return meterWaterImportSMOmpl;
    }

    public void setMeterWaterImportSMOmpl(IMeterWaterImportSMO meterWaterImportSMOmpl) {
        this.meterWaterImportSMOmpl = meterWaterImportSMOmpl;
    }

    public IAssetExportSMO getAssetExportSMOImpl() {
        return assetExportSMOImpl;
    }

    public void setAssetExportSMOImpl(IAssetExportSMO assetExportSMOImpl) {
        this.assetExportSMOImpl = assetExportSMOImpl;
    }
}
