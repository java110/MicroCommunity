package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.room.ApplyRoomDiscountDto;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠申请导出
 *
 * @author fqz
 * @date 2023-12-14 13:52
 */
@Service(value = "applyRoomDiscount")
public class ApplyRoomDiscountAdapt implements IExportDataAdapt {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("优惠申请");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房屋(楼栋-单元-房屋)");
        row.createCell(1).setCellValue("折扣ID");
        row.createCell(2).setCellValue("折扣名称");
        row.createCell(3).setCellValue("申请类型");
        row.createCell(4).setCellValue("申请人");
        row.createCell(5).setCellValue("申请电话");
        row.createCell(6).setCellValue("开始时间");
        row.createCell(7).setCellValue("结束时间");
        row.createCell(8).setCellValue("状态");
        row.createCell(9).setCellValue("创建时间");
        row.createCell(10).setCellValue("使用状态");
        row.createCell(11).setCellValue("返还类型");
        row.createCell(12).setCellValue("返还金额");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getApplyRoomDiscount(sheet, reqJson);
        return workbook;
    }

    private void getApplyRoomDiscount(Sheet sheet, JSONObject reqJson) {
        ApplyRoomDiscountDto applyRoomDiscountDto = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountDto.class);
        applyRoomDiscountDto.setPage(1);
        applyRoomDiscountDto.setRow(MAX_ROW);
        List<ApplyRoomDiscountDto> applyRoomDiscounts = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
        appendData(applyRoomDiscounts, sheet);
    }

    private void appendData(List<ApplyRoomDiscountDto> applyRoomDiscounts, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < applyRoomDiscounts.size(); index++) {
            row = sheet.createRow(index + 1);
            ApplyRoomDiscountDto applyRoomDiscountDto = applyRoomDiscounts.get(index);
            row.createCell(0).setCellValue(applyRoomDiscountDto.getRoomName());
            row.createCell(1).setCellValue(applyRoomDiscountDto.getDiscountId());
            row.createCell(2).setCellValue(applyRoomDiscountDto.getDiscountName());
            row.createCell(3).setCellValue(applyRoomDiscountDto.getApplyTypeName());
            row.createCell(4).setCellValue(applyRoomDiscountDto.getCreateUserName());
            row.createCell(5).setCellValue(applyRoomDiscountDto.getCreateUserTel());
            row.createCell(6).setCellValue(applyRoomDiscountDto.getStartTime());
            row.createCell(7).setCellValue(applyRoomDiscountDto.getEndTime());
            row.createCell(8).setCellValue(applyRoomDiscountDto.getStateName());
            row.createCell(9).setCellValue(applyRoomDiscountDto.getCreateTime());
            if (!StringUtil.isEmpty(applyRoomDiscountDto.getInUse()) && applyRoomDiscountDto.getInUse().equals("0")) {
                row.createCell(10).setCellValue("未使用");
            } else {
                row.createCell(10).setCellValue("已使用");
            }
            if (!StringUtil.isEmpty(applyRoomDiscountDto.getDiscountId()) && applyRoomDiscountDto.getReturnWay().equals("1002")) {
                row.createCell(11).setCellValue("账户余额");
            } else if (!StringUtil.isEmpty(applyRoomDiscountDto.getDiscountId()) && !applyRoomDiscountDto.getReturnWay().equals("1002")) {
                row.createCell(11).setCellValue("折扣");
            } else {
                row.createCell(11).setCellValue("--");
            }
            if (!StringUtil.isEmpty(applyRoomDiscountDto.getReturnAmount())) {
                row.createCell(12).setCellValue(applyRoomDiscountDto.getReturnAmount());
            } else {
                row.createCell(12).setCellValue("--");
            }
        }
    }
}
