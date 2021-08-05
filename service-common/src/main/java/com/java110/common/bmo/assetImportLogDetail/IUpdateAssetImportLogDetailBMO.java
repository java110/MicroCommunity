package com.java110.common.bmo.assetImportLogDetail;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAssetImportLogDetailBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param assetImportLogDetailPo
     * @return
     */
    ResponseEntity<String> update(AssetImportLogDetailPo assetImportLogDetailPo);


}
