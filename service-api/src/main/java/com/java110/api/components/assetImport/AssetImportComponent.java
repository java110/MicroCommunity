package com.java110.api.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.api.controller.component.CallComponentController;
import com.java110.api.smo.assetExport.IAssetExportSMO;
import com.java110.api.smo.assetImport.IAssetImportSMO;
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
@Component("assetImport")
public class AssetImportComponent {

    private final static Logger logger = LoggerFactory.getLogger(CallComponentController.class);


    @Autowired
    private IAssetImportSMO assetImportSMOImpl;

    @Autowired
    private IAssetExportSMO assetExportSMOImpl;

    /**
     * 资产导入方法 将Excel中的数据直接导入到表里
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception{
        return assetImportSMOImpl.importExcelData(pd,uploadFile);
    }

    /**
     * 资产导出
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public ResponseEntity<Object> exitCommunityData(IPageData pd) throws Exception {
        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(),pd.getToken(), pd.getReqData(), pd.getComponentCode(), pd.getComponentMethod(), "",
                pd.getSessionId(), AppDto.WEB_APP_ID,pd.getHeaders());
        return assetExportSMOImpl.exportExcelData(newPd);
    }

    public IAssetImportSMO getAssetImportSMOImpl() {
        return assetImportSMOImpl;
    }

    public void setAssetImportSMOImpl(IAssetImportSMO assetImportSMOImpl) {
        this.assetImportSMOImpl = assetImportSMOImpl;
    }

    public IAssetExportSMO getAssetExportSMOImpl() {
        return assetExportSMOImpl;
    }

    public void setAssetExportSMOImpl(IAssetExportSMO assetExportSMOImpl) {
        this.assetExportSMOImpl = assetExportSMOImpl;
    }
}
