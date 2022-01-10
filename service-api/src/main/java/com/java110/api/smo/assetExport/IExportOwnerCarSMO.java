package com.java110.api.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author fqz
 * @date 2021-12-21
 */
public interface IExportOwnerCarSMO {

    /**
     * 导入excel数据
     *
     * @param pd 前台数据封装
     * @return ResponseEntity
     */
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception;

}
