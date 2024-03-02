package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.purchase.AllocationStorehouseApplyDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 调拨申请导出
 *
 * @author fqz
 * @date 2023-11-20 14:07
 */
@Service(value = "allocationStorehouseManage")
public class AllocationStorehouseManage implements IExportDataAdapt {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("调拨申请");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("调拨/退还");
        row.createCell(2).setCellValue("申请人");
        row.createCell(3).setCellValue("状态");
        row.createCell(4).setCellValue("类型");
        row.createCell(5).setCellValue("时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getAllocationStorehouseManage(sheet, reqJson);
        return workbook;
    }

    private void getAllocationStorehouseManage(Sheet sheet, JSONObject reqJson) {
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseApplyDto.class);
        if (!StringUtil.isEmpty(allocationStorehouseApplyDto.getApplyId())) { //单个信息查询时不做控制
            return;
        }
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewlistAllocationStorehouses");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            allocationStorehouseApplyDto.setStartUserId(reqJson.getString("userId"));
        }
        allocationStorehouseApplyDto.setPage(1);
        allocationStorehouseApplyDto.setRow(MAX_ROW);
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyList = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        appendData(allocationStorehouseApplyList, sheet);
    }

    private void appendData(List<AllocationStorehouseApplyDto> allocationStorehouseApplyList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < allocationStorehouseApplyList.size(); index++) {
            row = sheet.createRow(index + 1);
            AllocationStorehouseApplyDto allocationStorehouseApplyDto = allocationStorehouseApplyList.get(index);
            row.createCell(0).setCellValue(allocationStorehouseApplyDto.getApplyId());
            row.createCell(1).setCellValue(allocationStorehouseApplyDto.getApplyCount());
            row.createCell(2).setCellValue(allocationStorehouseApplyDto.getStartUserName());
            row.createCell(3).setCellValue(allocationStorehouseApplyDto.getStateName());
            row.createCell(4).setCellValue(allocationStorehouseApplyDto.getApplyTypeName());
            row.createCell(5).setCellValue(allocationStorehouseApplyDto.getCreateTime());
        }
    }
}
