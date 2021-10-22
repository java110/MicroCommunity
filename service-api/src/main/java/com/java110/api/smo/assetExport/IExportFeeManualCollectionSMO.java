package com.java110.api.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName IAssetImportSMO
 * @Description TODO 费用导出
 * @Author wuxw
 * @Date 2019/9/23 23:13
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
public interface IExportFeeManualCollectionSMO {

    /**
     * 导入excel数据
     * @param pd 前台数据封装
     * @return ResponseEntity
     */
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception;

    /**
     * 导出催缴单
     * @param pd
     * @return
     */
    ResponseEntity<Object> downloadCollectionLetterOrder(IPageData pd);
}
