package com.java110.api.importData;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.system.ComponentValidateResult;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 导入数据 清洗适配器
 * 数据预处理 封装校验等
 */
public interface IImportDataCleaningAdapt {

    /**
     * 清洗 数据
     *
     * @param workbook
     */
    List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception;
}
