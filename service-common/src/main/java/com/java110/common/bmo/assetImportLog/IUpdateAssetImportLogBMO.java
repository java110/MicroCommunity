package com.java110.common.bmo.assetImportLog;
import com.java110.po.assetImportLog.AssetImportLogPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAssetImportLogBMO {


    /**
     * 修改批量操作日志
     * add by wuxw
     * @param assetImportLogPo
     * @return
     */
    ResponseEntity<String> update(AssetImportLogPo assetImportLogPo);


}
