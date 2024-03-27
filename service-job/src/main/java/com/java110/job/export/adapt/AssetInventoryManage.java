package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.purchase.AssetInventoryDto;
import com.java110.intf.store.IAssetInventoryV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 盘点管理
 *
 * @author fqz
 * @date 2023-11-24 15:48
 */
@Service(value = "assetInventoryManage")
public class AssetInventoryManage implements IExportDataAdapt {

    @Autowired
    private IAssetInventoryV1InnerServiceSMO assetInventoryV1InnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("调拨明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("盘点单号");
        row.createCell(1).setCellValue("盘点名称");
        row.createCell(2).setCellValue("盘点时间");
        row.createCell(3).setCellValue("盘点仓库");
        row.createCell(4).setCellValue("盘点人");
        row.createCell(5).setCellValue("状态");
        row.createCell(6).setCellValue("创建时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getAssetInventoryManage(sheet, reqJson);
        return workbook;
    }

    private void getAssetInventoryManage(Sheet sheet, JSONObject reqJson) {
        AssetInventoryDto assetInventoryDto = BeanConvertUtil.covertBean(reqJson, AssetInventoryDto.class);
        assetInventoryDto.setPage(1);
        assetInventoryDto.setRow(MAX_ROW);
        List<AssetInventoryDto> assetInventoryList = assetInventoryV1InnerServiceSMOImpl.queryAssetInventorys(assetInventoryDto);
        appendData(assetInventoryList, sheet);
    }

    private void appendData(List<AssetInventoryDto> assetInventoryList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < assetInventoryList.size(); index++) {
            row = sheet.createRow(index + 1);
            AssetInventoryDto allocationStorehouseDto = assetInventoryList.get(index);
            row.createCell(0).setCellValue(allocationStorehouseDto.getAiId());
            row.createCell(1).setCellValue(allocationStorehouseDto.getName());
            row.createCell(2).setCellValue(allocationStorehouseDto.getInvTime());
            row.createCell(3).setCellValue(allocationStorehouseDto.getShName());
            row.createCell(4).setCellValue(allocationStorehouseDto.getStaffName());
            row.createCell(5).setCellValue(allocationStorehouseDto.getStateName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(6).setCellValue(dateFormat.format(allocationStorehouseDto.getCreateTime()));
        }
    }
}
