package com.java110.job.export;

import com.java110.dto.data.ExportDataDto;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出数据适配器
 */
public interface IExportDataAdapt {

    /**
     * 导出数据 excel
     * @param exportDataDto
     */
    SXSSFWorkbook exportData(ExportDataDto exportDataDto);
}
