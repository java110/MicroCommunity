package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 采购申请导出
 *
 * @author fqz
 * @date 2023-11-18 14:44
 */
@Service("purchaseApplyManage")
public class PurchaseApplyAdapt implements IExportDataAdapt {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("采购申请");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("申请单号");
        row.createCell(1).setCellValue("申请人");
        row.createCell(2).setCellValue("使用人");
        row.createCell(3).setCellValue("操作人");
        row.createCell(4).setCellValue("物品(规格)");
        row.createCell(5).setCellValue("申请时间");
        row.createCell(6).setCellValue("采购方式");
        row.createCell(7).setCellValue("审批状态");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getPurchaseApplyManage(sheet, reqJson);
        return workbook;
    }

    private void getPurchaseApplyManage(Sheet sheet, JSONObject reqJson) {
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);
        //采购申请所有记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewPurchaseApplyManage");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size() != 0 || (!StringUtil.isEmpty(reqJson.getString("applyOrderId")))) {
            purchaseApplyDto.setUserId("");
            purchaseApplyDto.setUserName("");
        }
        purchaseApplyDto.setPage(1);
        purchaseApplyDto.setRow(MAX_ROW);
        List<PurchaseApplyDto> purchaseApplyList = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplyAndDetails(purchaseApplyDto);
        appendData(purchaseApplyList, sheet);
    }

    private void appendData(List<PurchaseApplyDto> purchaseApplyList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < purchaseApplyList.size(); index++) {
            row = sheet.createRow(index + 1);
            PurchaseApplyDto purchaseApplyDto = purchaseApplyList.get(index);
            row.createCell(0).setCellValue(purchaseApplyDto.getApplyOrderId());
            row.createCell(1).setCellValue(purchaseApplyDto.getUserName());
            row.createCell(2).setCellValue(purchaseApplyDto.getEndUserName());
            row.createCell(3).setCellValue(purchaseApplyDto.getCreateUserName());
            List<PurchaseApplyDetailVo> applyDetailList = purchaseApplyDto.getPurchaseApplyDetailVo();
            if (applyDetailList == null || applyDetailList.size() < 1) {
                row.createCell(4).setCellValue("--");
            } else {
                StringBuffer resNames = new StringBuffer();
                Integer cursor = 0;
                for (PurchaseApplyDetailVo purchaseApplyDetailVo : applyDetailList) {
                    cursor++;
                    resNames.append(cursor + "：" + purchaseApplyDetailVo.getResName() + "(" + purchaseApplyDetailVo.getSpecName() + ") ");
                }
                row.createCell(4).setCellValue(resNames.toString());
            }
            row.createCell(5).setCellValue(purchaseApplyDto.getCreateTime());
            if (purchaseApplyDto.getWarehousingWay().equals("10000")) {
                row.createCell(6).setCellValue("直接入库");
            } else if (purchaseApplyDto.getWarehousingWay().equals("20000")) {
                row.createCell(6).setCellValue("采购入库");
            } else {
                row.createCell(6).setCellValue("紧急采购");
            }
            row.createCell(7).setCellValue(purchaseApplyDto.getStateName());
        }
    }
}
