package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.purchase.AllocationStorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IAllocationStorehouseInnerServiceSMO;
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
import java.util.Map;

/**
 * 调拨明细
 *
 * @author fqz
 * @date 2023-11-23 14:26
 */
@Service(value = "allocationStorehouseDetailed")
public class AllocationStorehouseDetailedAdapt implements IExportDataAdapt {

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("调拨明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("调拨单号");
        row.createCell(1).setCellValue("物品ID");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品名称");
        row.createCell(4).setCellValue("物品规格");
        row.createCell(5).setCellValue("固定物品");
        row.createCell(6).setCellValue("被调仓库原库存");
        row.createCell(7).setCellValue("调拨数量");
        row.createCell(8).setCellValue("被调仓库");
        row.createCell(9).setCellValue("目标仓库");
        row.createCell(10).setCellValue("申请人");
        row.createCell(11).setCellValue("调拨备注");
        row.createCell(12).setCellValue("状态");
        row.createCell(13).setCellValue("时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getAllocationStorehouseDetailed(sheet, reqJson);
        return workbook;
    }

    private void getAllocationStorehouseDetailed(Sheet sheet, JSONObject reqJson) {
        AllocationStorehouseDto allocationStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseDto.class);
        //调拨记录（调拨记录所有权限查看所有数据）
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewlistAllocationStorehouses");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        allocationStorehouseDto.setPage(1);
        allocationStorehouseDto.setRow(MAX_ROW);
        if (privileges.size() == 0) {
            allocationStorehouseDto.setStartUserId(reqJson.getString("userId"));
        }
        List<AllocationStorehouseDto> allocationStorehouseList = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        appendData(allocationStorehouseList, sheet);
    }

    private void appendData(List<AllocationStorehouseDto> allocationStorehouseList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < allocationStorehouseList.size(); index++) {
            row = sheet.createRow(index + 1);
            AllocationStorehouseDto allocationStorehouseDto = allocationStorehouseList.get(index);
            row.createCell(0).setCellValue(allocationStorehouseDto.getApplyId());
            row.createCell(1).setCellValue(allocationStorehouseDto.getResId());
            row.createCell(2).setCellValue(allocationStorehouseDto.getParentRstName() + ">" + allocationStorehouseDto.getRstName());
            row.createCell(3).setCellValue(allocationStorehouseDto.getResName());
            if (!StringUtil.isEmpty(allocationStorehouseDto.getSpecName())) {
                row.createCell(4).setCellValue(allocationStorehouseDto.getSpecName());
            } else {
                row.createCell(4).setCellValue("--");
            }
            row.createCell(5).setCellValue(allocationStorehouseDto.getIsFixedName());
            row.createCell(6).setCellValue(allocationStorehouseDto.getOriginalStock() + allocationStorehouseDto.getUnitCodeName());
            if (!StringUtil.isEmpty(allocationStorehouseDto.getApplyType()) && allocationStorehouseDto.getApplyType().equals("20000")) {
                row.createCell(7).setCellValue(allocationStorehouseDto.getStock() + allocationStorehouseDto.getMiniUnitCodeName());
                row.createCell(8).setCellValue(allocationStorehouseDto.getStartUserName());
            } else {
                row.createCell(7).setCellValue(allocationStorehouseDto.getStock() + allocationStorehouseDto.getUnitCodeName());
                row.createCell(8).setCellValue(allocationStorehouseDto.getShaName());
            }
            row.createCell(9).setCellValue(allocationStorehouseDto.getShzName());
            row.createCell(10).setCellValue(allocationStorehouseDto.getStartUserName());
            row.createCell(11).setCellValue(allocationStorehouseDto.getRemark());
            row.createCell(12).setCellValue(allocationStorehouseDto.getStateName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(13).setCellValue(format.format(allocationStorehouseDto.getCreateTime()));
        }
    }
}
