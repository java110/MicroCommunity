package com.java110.common.bmo.assetImportLogDetail;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetAssetImportLogDetailBMO {


    /**
     * 查询批量操作日志详情
     * add by wuxw
     * @param  assetImportLogDetailDto
     * @return
     */
    ResponseEntity<String> get(AssetImportLogDetailDto assetImportLogDetailDto);


}
