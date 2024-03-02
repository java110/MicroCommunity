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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 物品领用导出
 *
 * @author fqz
 * @date 2023-11-20 15:33
 */
@Service(value = "itemOutManage")
public class ItemOutManage implements IExportDataAdapt {

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
        Sheet sheet = workbook.createSheet("物品领用");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("单号");
        row.createCell(1).setCellValue("物品");
        row.createCell(2).setCellValue("申请人");
        row.createCell(3).setCellValue("操作人");
        row.createCell(4).setCellValue("申请时间");
        row.createCell(5).setCellValue("状态");
        row.createCell(6).setCellValue("领用方式");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getItemOutManage(sheet, reqJson);
        return workbook;
    }

    private void getItemOutManage(Sheet sheet, JSONObject reqJson) {
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);
        List<Map> privileges = new ArrayList<>();
        if (purchaseApplyDto.getResOrderType().equals(PurchaseApplyDto.RES_ORDER_TYPE_ENTER)) {
            //采购申请所有记录权限
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewPurchaseApplyManage");
            basePrivilegeDto.setUserId(reqJson.getString("userId"));
            privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        }
        if (purchaseApplyDto.getResOrderType().equals(PurchaseApplyDto.RES_ORDER_TYPE_OUT)) {
            //物品领用所有记录权限
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllItemUse");
            basePrivilegeDto.setUserId(reqJson.getString("userId"));
            privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        }
        if (privileges.size() != 0 || (!StringUtil.isEmpty(reqJson.getString("applyOrderId")))) {
            purchaseApplyDto.setUserId("");
        }
        if (!StringUtil.isEmpty(reqJson.getString("applyUserName"))) {
            purchaseApplyDto.setUserName(reqJson.getString("applyUserName"));
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
            List<PurchaseApplyDetailVo> applyDetailList = purchaseApplyDto.getPurchaseApplyDetailVo();
            if (applyDetailList == null || applyDetailList.size() < 1) {
                row.createCell(1).setCellValue("--");
            } else {
                StringBuffer resNames = new StringBuffer();
                Integer cursor = 0;
                for (PurchaseApplyDetailVo purchaseApplyDetailVo : applyDetailList) {
                    cursor++;
                    resNames.append(cursor + "：" + purchaseApplyDetailVo.getResName() + "(" + purchaseApplyDetailVo.getSpecName() + ") ");
                }
                row.createCell(1).setCellValue(resNames.toString());
            }
            row.createCell(2).setCellValue(purchaseApplyDto.getUserName());
            row.createCell(3).setCellValue(purchaseApplyDto.getCreateUserName());
            row.createCell(4).setCellValue(purchaseApplyDto.getCreateTime());
            row.createCell(5).setCellValue(purchaseApplyDto.getStateName());
            if (!StringUtil.isEmpty(purchaseApplyDto.getWarehousingWay()) && purchaseApplyDto.getWarehousingWay().equals("10000")) {
                row.createCell(6).setCellValue("直接出库");
            } else {
                row.createCell(6).setCellValue("审核出库");
            }
        }
    }
}
