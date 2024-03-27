package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.PayFeeDetailDto;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缴费清单导出
 *
 * @author fqz
 * @date 2023-11-13
 */
@Service("reportPayFee")
public class ReportPayFeeAdapt implements IExportDataAdapt {

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("缴费清单");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用类型");
        row.createCell(1).setCellValue("费用项目");
        row.createCell(2).setCellValue("付费方");
        row.createCell(3).setCellValue("缴费ID");
        row.createCell(4).setCellValue("支付方式");
        row.createCell(5).setCellValue("付费周期(单位:月)");
        row.createCell(6).setCellValue("应付金额(单位:元)");
        row.createCell(7).setCellValue("实付金额(单位:元)");
        row.createCell(8).setCellValue("操作人");
        row.createCell(9).setCellValue("缴费时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getReportPayFeeManage(sheet, reqJson);
        return workbook;
    }

    private void getReportPayFeeManage(Sheet sheet, JSONObject reqJson) {
        PayFeeDetailDto payFeeDetailDto = BeanConvertUtil.covertBean(reqJson, PayFeeDetailDto.class);
        payFeeDetailDto.setPage(1);
        payFeeDetailDto.setRow(MAX_ROW);
        List<PayFeeDetailDto> payFeeDetailDtos = payFeeDetailV1InnerServiceSMOImpl.queryPayFeeDetailNew(payFeeDetailDto);
        appendData(payFeeDetailDtos, sheet);
    }

    private void appendData(List<PayFeeDetailDto> payFeeDetailDtos, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < payFeeDetailDtos.size(); index++) {
            row = sheet.createRow(index + 1);
            PayFeeDetailDto payFeeDetailDto = payFeeDetailDtos.get(index);
            row.createCell(0).setCellValue(payFeeDetailDto.getFeeTypeCdName());
            row.createCell(1).setCellValue(payFeeDetailDto.getFeeName());
            row.createCell(2).setCellValue(payFeeDetailDto.getPayObjName());
            row.createCell(3).setCellValue(payFeeDetailDto.getDetailId());
            row.createCell(4).setCellValue(payFeeDetailDto.getPrimeRate());
            row.createCell(5).setCellValue(payFeeDetailDto.getCycles());
            row.createCell(6).setCellValue(payFeeDetailDto.getReceivableAmount());
            row.createCell(7).setCellValue(payFeeDetailDto.getReceivedAmount());
            row.createCell(8).setCellValue(payFeeDetailDto.getUserName());
            row.createCell(9).setCellValue(payFeeDetailDto.getCreateTime());
        }
    }
}
