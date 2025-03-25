package com.java110.api.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName IAssetImportSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:13
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
public interface IExportRoomSMO {



     ResponseEntity<Object> exportCustomReportTableData(IPageData pd) throws Exception;
}
