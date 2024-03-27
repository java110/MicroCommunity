package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.purchase.PurchaseApplyDetailDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 出入库明细导出
 *
 * @author fqz
 * @date 2023-11-18 09:04
 */
@Service("purchaseApplyDetailManage")
public class PurchaseApplyDetailAdapt implements IExportDataAdapt {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("出入库明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("申请单号");
        row.createCell(1).setCellValue("申请人");
        row.createCell(2).setCellValue("使用人");
        row.createCell(3).setCellValue("出入库类型");
        row.createCell(4).setCellValue("物品类型");
        row.createCell(5).setCellValue("物品名称");
        row.createCell(6).setCellValue("物品规格");
        row.createCell(7).setCellValue("固定物品");
        row.createCell(8).setCellValue("供应商");
        row.createCell(9).setCellValue("物品仓库");
        row.createCell(10).setCellValue("采购/出库方式");
        row.createCell(11).setCellValue("申请数量");
        row.createCell(12).setCellValue("采购数量");
        row.createCell(13).setCellValue("采购价格");
        row.createCell(14).setCellValue("总价");
        row.createCell(15).setCellValue("申请备注");
        row.createCell(16).setCellValue("状态");
        row.createCell(17).setCellValue("创建时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getPurchaseApplyDetailManage(sheet, reqJson);
        return workbook;
    }

    private void getPurchaseApplyDetailManage(Sheet sheet, JSONObject reqJson) {
        PurchaseApplyDetailDto purchaseApplyDetailDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDetailDto.class);
        //报修待办查看所有出入库记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewAllPurchaseApplyDetail");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges != null && privileges.size() > 0) {
            purchaseApplyDetailDto.setUserId("");
            purchaseApplyDetailDto.setUserName("");
        }
        purchaseApplyDetailDto.setPage(1);
        purchaseApplyDetailDto.setRow(MAX_ROW);
        List<PurchaseApplyDetailDto> purchaseApplyDetails = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        appendData(purchaseApplyDetails, sheet);
    }

    private void appendData(List<PurchaseApplyDetailDto> purchaseApplyDetails, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < purchaseApplyDetails.size(); index++) {
            row = sheet.createRow(index + 1);
            PurchaseApplyDetailDto purchaseApplyDetailDto = purchaseApplyDetails.get(index);
            row.createCell(0).setCellValue(purchaseApplyDetailDto.getApplyOrderId());
            row.createCell(1).setCellValue(purchaseApplyDetailDto.getUserName());
            row.createCell(2).setCellValue(purchaseApplyDetailDto.getEndUserName());
            row.createCell(3).setCellValue(purchaseApplyDetailDto.getResOrderTypeName());
            row.createCell(4).setCellValue(purchaseApplyDetailDto.getParentRstName() + ">" + purchaseApplyDetailDto.getRstName());
            row.createCell(5).setCellValue(purchaseApplyDetailDto.getResName());
            if (!StringUtil.isEmpty(purchaseApplyDetailDto.getSpecName())) {
                row.createCell(6).setCellValue(purchaseApplyDetailDto.getSpecName());
            } else {
                row.createCell(6).setCellValue("--");
            }
            row.createCell(7).setCellValue(purchaseApplyDetailDto.getIsFixedName());
            if (!StringUtil.isEmpty(purchaseApplyDetailDto.getSupplierName())) {
                row.createCell(8).setCellValue(purchaseApplyDetailDto.getSupplierName());
            } else {
                row.createCell(8).setCellValue("--");
            }
            row.createCell(9).setCellValue(purchaseApplyDetailDto.getShName());
            row.createCell(10).setCellValue(purchaseApplyDetailDto.getWarehousingWayName() + purchaseApplyDetailDto.getResOrderTypeName());
            row.createCell(11).setCellValue(purchaseApplyDetailDto.getQuantity() + purchaseApplyDetailDto.getUnitCodeName());
            if (!StringUtil.isEmpty(purchaseApplyDetailDto.getPurchaseQuantity())) {
                row.createCell(12).setCellValue(purchaseApplyDetailDto.getPurchaseQuantity() + purchaseApplyDetailDto.getUnitCodeName());
            } else {
                row.createCell(12).setCellValue("--");
            }
            if (!StringUtil.isEmpty(purchaseApplyDetailDto.getPrice())) {
                row.createCell(13).setCellValue("￥" + purchaseApplyDetailDto.getPrice());
            } else {
                row.createCell(13).setCellValue("￥0.00");
            }
            if (!StringUtil.isEmpty(purchaseApplyDetailDto.getPurchaseQuantity()) && !StringUtil.isEmpty(purchaseApplyDetailDto.getPrice())) {
                BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailDto.getPurchaseQuantity());
                BigDecimal price = new BigDecimal(purchaseApplyDetailDto.getPrice());
                BigDecimal multiply = purchaseQuantity.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
                row.createCell(14).setCellValue("￥" + multiply);
            } else {
                row.createCell(14).setCellValue("￥0.00");
            }
            row.createCell(15).setCellValue(purchaseApplyDetailDto.getRemark());
            row.createCell(16).setCellValue(purchaseApplyDetailDto.getStateName());
            row.createCell(17).setCellValue(purchaseApplyDetailDto.getCreateTime());
        }
    }
}
