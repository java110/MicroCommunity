package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.purchase.AllocationUserStorehouseDto;
import com.java110.intf.store.IAllocationUserStorehouseInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 转增记录导出
 *
 * @author fqz
 * @date 2023-11-28
 */
@Service(value = "allocationUserStorehouseManage")
public class AllocationUserStorehouseManage implements IExportDataAdapt {

    @Autowired
    private IAllocationUserStorehouseInnerServiceSMO allocationUserStorehouseInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("调拨明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("转增ID");
        row.createCell(1).setCellValue("物品资源ID");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品名称");
        row.createCell(4).setCellValue("物品规格");
        row.createCell(5).setCellValue("固定物品");
        row.createCell(6).setCellValue("转增人ID");
        row.createCell(7).setCellValue("转增人");
        row.createCell(8).setCellValue("转增对象ID");
        row.createCell(9).setCellValue("转增对象");
        row.createCell(10).setCellValue("原有库存");
        row.createCell(11).setCellValue("转增数量");
        row.createCell(12).setCellValue("创建时间");
        row.createCell(13).setCellValue("备注");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getAllocationUserStorehouseManage(sheet, reqJson);
        return workbook;
    }

    private void getAllocationUserStorehouseManage(Sheet sheet, JSONObject reqJson) {
        AllocationUserStorehouseDto allocationUserStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationUserStorehouseDto.class);
        allocationUserStorehouseDto.setPage(1);
        allocationUserStorehouseDto.setRow(MAX_ROW);
        List<AllocationUserStorehouseDto> allocationUserStorehouseList = allocationUserStorehouseInnerServiceSMOImpl.queryAllocationUserStorehouses(allocationUserStorehouseDto);
        appendData(allocationUserStorehouseList, sheet);
    }

    private void appendData(List<AllocationUserStorehouseDto> allocationUserStorehouseList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < allocationUserStorehouseList.size(); index++) {
            row = sheet.createRow(index + 1);
            AllocationUserStorehouseDto allocationUserStorehouseDto = allocationUserStorehouseList.get(index);
            row.createCell(0).setCellValue(allocationUserStorehouseDto.getAusId());
            row.createCell(1).setCellValue(allocationUserStorehouseDto.getResId());
            row.createCell(2).setCellValue(allocationUserStorehouseDto.getParentRstName() + ">" + allocationUserStorehouseDto.getRstName());
            row.createCell(3).setCellValue(allocationUserStorehouseDto.getResName());
            if (!StringUtil.isEmpty(allocationUserStorehouseDto.getSpecName())) {
                row.createCell(4).setCellValue(allocationUserStorehouseDto.getSpecName());
            } else {
                row.createCell(4).setCellValue("--");
            }
            row.createCell(5).setCellValue(allocationUserStorehouseDto.getIsFixedName());
            row.createCell(6).setCellValue(allocationUserStorehouseDto.getStartUserId());
            row.createCell(7).setCellValue(allocationUserStorehouseDto.getStartUserName());
            row.createCell(8).setCellValue(allocationUserStorehouseDto.getAcceptUserId());
            row.createCell(9).setCellValue(allocationUserStorehouseDto.getAcceptUserName());
            row.createCell(10).setCellValue(allocationUserStorehouseDto.getStock() + allocationUserStorehouseDto.getUnitCodeName());
            row.createCell(11).setCellValue(allocationUserStorehouseDto.getGiveQuantity() + allocationUserStorehouseDto.getMiniUnitCodeName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(12).setCellValue(format.format(allocationUserStorehouseDto.getCreateTime()));
            row.createCell(13).setCellValue(allocationUserStorehouseDto.getRemark());
        }
    }
}
