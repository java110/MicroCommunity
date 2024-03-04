package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.resource.ResourceStoreUseRecordDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreUseRecordInnerServiceSMO;
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
 * 使用记录导出
 *
 * @author fqz
 * @date 2023-11-28
 */
@Service(value = "resourceStoreUseRecordManage")
public class ResourceStoreUseRecordManage implements IExportDataAdapt {

    @Autowired
    private IResourceStoreUseRecordInnerServiceSMO resourceStoreUseRecordInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("使用记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("使用记录ID");
        row.createCell(1).setCellValue("维修工单编号");
        row.createCell(2).setCellValue("物品编号");
        row.createCell(3).setCellValue("物品类型");
        row.createCell(4).setCellValue("物品名称");
        row.createCell(5).setCellValue("物品规格");
        row.createCell(6).setCellValue("固定物品");
        row.createCell(7).setCellValue("使用类型");
        row.createCell(8).setCellValue("使用数量");
        row.createCell(9).setCellValue("物品价格");
        row.createCell(10).setCellValue("使用人ID");
        row.createCell(11).setCellValue("使用人");
        row.createCell(12).setCellValue("创建时间");
        row.createCell(13).setCellValue("备注");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getResourceStoreUseRecordManage(sheet, reqJson);
        return workbook;
    }

    private void getResourceStoreUseRecordManage(Sheet sheet, JSONObject reqJson) {
        ResourceStoreUseRecordDto resourceStoreUseRecordDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreUseRecordDto.class);
        //查询物品所有使用记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/allResourceStoreUseRecord");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() == 0) {
            resourceStoreUseRecordDto.setUserId(reqJson.getString("userId"));
            resourceStoreUseRecordDto.setUserName(reqJson.getString("userName"));
        } else {
            resourceStoreUseRecordDto.setUserId("");
            resourceStoreUseRecordDto.setUserName("");
        }
        resourceStoreUseRecordDto.setPage(1);
        resourceStoreUseRecordDto.setRow(MAX_ROW);
        List<ResourceStoreUseRecordDto> resourceStoreUseRecordList = resourceStoreUseRecordInnerServiceSMOImpl.queryResourceStoreUseRecords(resourceStoreUseRecordDto);
        appendData(resourceStoreUseRecordList, sheet);
    }

    private void appendData(List<ResourceStoreUseRecordDto> resourceStoreUseRecordList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < resourceStoreUseRecordList.size(); index++) {
            row = sheet.createRow(index + 1);
            ResourceStoreUseRecordDto resourceStoreUseRecordDto = resourceStoreUseRecordList.get(index);
            row.createCell(0).setCellValue(resourceStoreUseRecordDto.getRsurId());
            row.createCell(1).setCellValue(resourceStoreUseRecordDto.getRepairId());
            row.createCell(2).setCellValue(resourceStoreUseRecordDto.getResId());
            row.createCell(3).setCellValue(resourceStoreUseRecordDto.getParentRstName() + ">" + resourceStoreUseRecordDto.getRstName());
            if (!StringUtil.isEmpty(resourceStoreUseRecordDto.getResourceStoreName())) {
                row.createCell(4).setCellValue(resourceStoreUseRecordDto.getResourceStoreName());
            } else {
                row.createCell(4).setCellValue("--");
            }
            if (!StringUtil.isEmpty(resourceStoreUseRecordDto.getSpecName())) {
                row.createCell(5).setCellValue(resourceStoreUseRecordDto.getSpecName());
            } else {
                row.createCell(5).setCellValue("--");
            }
            row.createCell(6).setCellValue(resourceStoreUseRecordDto.getIsFixedName());
            row.createCell(7).setCellValue(resourceStoreUseRecordDto.getStateName());
            row.createCell(8).setCellValue(resourceStoreUseRecordDto.getQuantity() + resourceStoreUseRecordDto.getMiniUnitCodeName());
            if (!StringUtil.isEmpty(resourceStoreUseRecordDto.getUnitPrice())) {
                row.createCell(9).setCellValue(resourceStoreUseRecordDto.getUnitPrice());
            } else {
                row.createCell(9).setCellValue("--");
            }
            row.createCell(10).setCellValue(resourceStoreUseRecordDto.getCreateUserId());
            row.createCell(11).setCellValue(resourceStoreUseRecordDto.getCreateUserName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(12).setCellValue(format.format(resourceStoreUseRecordDto.getCreateTime()));
            row.createCell(13).setCellValue(resourceStoreUseRecordDto.getRemark());
        }
    }
}
