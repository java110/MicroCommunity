package com.java110.common.bmo.assetImportLog;
import com.java110.dto.log.AssetImportLogDto;
import org.springframework.http.ResponseEntity;
public interface IGetAssetImportLogBMO {


    /**
     * 查询批量操作日志
     * add by wuxw
     * @param  assetImportLogDto
     * @return
     */
    ResponseEntity<String> get(AssetImportLogDto assetImportLogDto);


}
