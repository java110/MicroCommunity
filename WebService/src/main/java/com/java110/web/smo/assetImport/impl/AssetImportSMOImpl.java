package com.java110.web.smo.assetImport.impl;

import com.java110.common.util.ImportExcelUtils;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.assetImport.IAssetImportSMO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("assetImportSMOImpl")
public class AssetImportSMOImpl extends BaseComponentSMO implements IAssetImportSMO {
    private final static Logger logger = LoggerFactory.getLogger(AssetImportSMOImpl.class);

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception{
        InputStream is = uploadFile.getInputStream();

        Workbook workbook = null;  //工作簿
        Sheet sheet = null;         //工作表
        String[] headers = null;   //表头信息

        workbook = ImportExcelUtils.createWorkbook(uploadFile);

        sheet = ImportExcelUtils.getSheet(workbook, "楼栋单元");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);

        logger.debug("请求参数为：%s", pd.getReqData());
        return new ResponseEntity<String>("成功", HttpStatus.OK);
    }
}
