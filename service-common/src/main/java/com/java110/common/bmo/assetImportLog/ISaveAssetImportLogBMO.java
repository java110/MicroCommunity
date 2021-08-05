package com.java110.common.bmo.assetImportLog;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.assetImportLog.AssetImportLogPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAssetImportLogBMO {


    /**
     * 添加批量操作日志
     * add by wuxw
     * @param reqJson
     * @return
     */
    ResponseEntity<String> save(JSONObject reqJson);


}
