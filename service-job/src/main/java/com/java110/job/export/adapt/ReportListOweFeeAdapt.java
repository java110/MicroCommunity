package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.reportFee.ReportOweFeeItemDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("reportListOweFee")
public class ReportListOweFeeAdapt implements IExportDataAdapt {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        JSONObject reqJson = exportDataDto.getReqJson();
        String configIds = reqJson.getString("configIds");
        //查询楼栋信息
        List<ReportOweFeeDto> oweFees = this.getReportListOweFees(reqJson);
        if (ListUtil.isNull(oweFees)) {
            return workbook;
        }
        //获取费用项
        List<FeeConfigDto> feeConfigDtos = getFeeConfigs(oweFees);
        Sheet sheet = workbook.createSheet("欠费清单");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("收费对象");
        row.createCell(1).setCellValue("业主名称");
        row.createCell(2).setCellValue("手机号");
        row.createCell(3).setCellValue("开始时间");
        row.createCell(4).setCellValue("结束时间");
        if (!StringUtil.isEmpty(configIds)) {
            for (int feeConfigIndex = 0; feeConfigIndex < feeConfigDtos.size(); feeConfigIndex++) {
                row.createCell(5 + feeConfigIndex).setCellValue(feeConfigDtos.get(feeConfigIndex).getFeeName());
            }
            row.createCell(5 + feeConfigDtos.size()).setCellValue("合计");
        } else {
            row.createCell(5).setCellValue("合计");
        }


        ReportOweFeeDto reportOweFeeDto = null;
        for (int roomIndex = 0; roomIndex < oweFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            reportOweFeeDto = oweFees.get(roomIndex);
            row.createCell(0).setCellValue(reportOweFeeDto.getPayerObjName());
            row.createCell(1).setCellValue(reportOweFeeDto.getOwnerName());
            row.createCell(2).setCellValue(reportOweFeeDto.getOwnerTel());
            row.createCell(3).setCellValue(reportOweFeeDto.getEndTime());
            row.createCell(4).setCellValue(reportOweFeeDto.getDeadlineTime());
            if (!StringUtil.isEmpty(configIds)) {
                for (int feeConfigIndex = 0; feeConfigIndex < feeConfigDtos.size(); feeConfigIndex++) {
                    row.createCell(5 + feeConfigIndex).setCellValue(getFeeConfigAmount(feeConfigDtos.get(feeConfigIndex), reportOweFeeDto));
                }
                row.createCell(5 + feeConfigDtos.size()).setCellValue(getAllFeeOweAmount(feeConfigDtos, reportOweFeeDto));
            } else {
                row.createCell(5).setCellValue(getAllFeeOweAmount(feeConfigDtos, reportOweFeeDto));
            }

        }
        return workbook;
    }

    private List<ReportOweFeeDto> getReportListOweFees(JSONObject reqJson) {

        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPage(1);
        reportOweFeeDto.setRow(10000);
        reportOweFeeDto.setHasOweFee("Y");

        List<ReportOweFeeDto> allReportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        if (ListUtil.isNull(allReportOweFeeDtos)) {
            return allReportOweFeeDtos;
        }

        //get old report owe fee
        List<ReportOweFeeDto> oldReportOweFeeDtos = new ArrayList<>();
        ReportOweFeeDto oldReportOweFeeDto = null;
        for (ReportOweFeeDto tmpReportOweFeeDto : allReportOweFeeDtos) {
            if (existsOweFee(oldReportOweFeeDtos, tmpReportOweFeeDto.getPayerObjId())) {
                continue;
            }
            oldReportOweFeeDto = new ReportOweFeeDto();
            oldReportOweFeeDto.setPayerObjId(tmpReportOweFeeDto.getPayerObjId());

            oldReportOweFeeDtos.add(oldReportOweFeeDto);
        }

        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            dealItem(tmpReportOweFeeDto, allReportOweFeeDtos);
        }

        if (reportOweFeeDto.getConfigIds() == null || reportOweFeeDto.getConfigIds().length < 1) {
            return oldReportOweFeeDtos;
        }

        //如果费用对象上没有这个费用项时默认写零
        FeeConfigDto feeConfigDto = null;
        feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(reportOweFeeDto.getConfigIds());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            for (String configId : reportOweFeeDto.getConfigIds()) {
                if (hasItem(tmpReportOweFeeDto.getItems(), configId) != null) {
                    continue;
                }
                ReportOweFeeItemDto reportOweFeeItemDto = new ReportOweFeeItemDto();
                reportOweFeeItemDto.setConfigId(configId);
                reportOweFeeItemDto.setConfigName(getFeeConfigName(feeConfigDtos, configId));
                reportOweFeeItemDto.setAmountOwed("0");
                reportOweFeeItemDto.setPayerObjId("");
                reportOweFeeItemDto.setPayerObjName("");
                tmpReportOweFeeDto.getItems().add(reportOweFeeItemDto);
            }
        }
        return oldReportOweFeeDtos;
    }

    private ReportOweFeeItemDto hasItem(List<ReportOweFeeItemDto> reportOweFeeItemDtos, String configId) {
        if (ListUtil.isNull(reportOweFeeItemDtos)) {
            return null;
        }
        for (ReportOweFeeItemDto reportOweFeeItemDto : reportOweFeeItemDtos) {
            if (reportOweFeeItemDto.getConfigId().equals(configId)) {
                return reportOweFeeItemDto;
            }
        }

        return null;
    }

    private void dealItem(ReportOweFeeDto oldReportOweFeeDto, List<ReportOweFeeDto> allReportOweFeeDtos) {
        List<ReportOweFeeItemDto> items = new ArrayList<>();
        if (ListUtil.isNull(allReportOweFeeDtos)) {
            return;
        }

        ReportOweFeeItemDto reportOweFeeItemDto = null;
        for (ReportOweFeeDto reportOweFeeDto : allReportOweFeeDtos) {
            if (!oldReportOweFeeDto.getPayerObjId().equals(reportOweFeeDto.getPayerObjId())) {
                continue;
            }
            reportOweFeeItemDto = hasItem(items, reportOweFeeDto.getConfigId());
            if (reportOweFeeItemDto == null) {
                reportOweFeeItemDto = new ReportOweFeeItemDto();
                reportOweFeeItemDto.setConfigId(reportOweFeeDto.getConfigId());
                reportOweFeeItemDto.setFeeName(reportOweFeeDto.getFeeName());
                reportOweFeeItemDto.setAmountOwed(reportOweFeeDto.getAmountOwed());
                reportOweFeeItemDto.setPayerObjId(reportOweFeeDto.getPayerObjId());
                reportOweFeeItemDto.setPayerObjName(reportOweFeeDto.getPayerObjName());
                reportOweFeeItemDto.setConfigName(reportOweFeeDto.getConfigName());
                try {
                    reportOweFeeItemDto.setStartTime(DateUtil.getDateFromString(reportOweFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                    reportOweFeeItemDto.setEndTime(DateUtil.getDateFromString(reportOweFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                items.add(reportOweFeeItemDto);
            } else {
                BigDecimal oldAmount = new BigDecimal(Double.parseDouble(reportOweFeeItemDto.getAmountOwed()));
                oldAmount = oldAmount.add(new BigDecimal(Double.parseDouble(reportOweFeeDto.getAmountOwed()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                reportOweFeeItemDto.setAmountOwed(oldAmount.doubleValue() + "");
            }
            if (!StringUtil.isEmpty(reportOweFeeDto.getOwnerName()) && StringUtil.isEmpty(oldReportOweFeeDto.getOwnerName())) {
                oldReportOweFeeDto.setOwnerName(reportOweFeeDto.getOwnerName());
            }
            if (!StringUtil.isEmpty(reportOweFeeDto.getOwnerTel()) && StringUtil.isEmpty(oldReportOweFeeDto.getOwnerTel())) {
                oldReportOweFeeDto.setOwnerTel(reportOweFeeDto.getOwnerTel());
            }
            oldReportOweFeeDto.setUpdateTime(reportOweFeeDto.getUpdateTime());
            oldReportOweFeeDto.setConfigName(reportOweFeeDto.getConfigName());
        }

        //计算总金额
        BigDecimal totalAmount = new BigDecimal(0);
        Date startTime = null;
        Date endTime = null;
        for (ReportOweFeeItemDto tempReportOweFeeItemDto : items) {
            if (startTime == null) {
                startTime = tempReportOweFeeItemDto.getStartTime();
            }
            if (startTime.getTime() > tempReportOweFeeItemDto.getStartTime().getTime()) {
                startTime = tempReportOweFeeItemDto.getStartTime();
            }
            if (endTime == null) {
                endTime = tempReportOweFeeItemDto.getEndTime();
            }
            if (endTime.getTime() < tempReportOweFeeItemDto.getEndTime().getTime()) {
                endTime = tempReportOweFeeItemDto.getEndTime();
            }
            totalAmount = totalAmount.add(new BigDecimal(Double.parseDouble(tempReportOweFeeItemDto.getAmountOwed()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        oldReportOweFeeDto.setEndTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setDeadlineTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setItems(items);
        oldReportOweFeeDto.setPayerObjName(items.get(0).getPayerObjName());
        oldReportOweFeeDto.setAmountOwed(totalAmount.doubleValue() + "");

    }

    private String getFeeConfigName(List<FeeConfigDto> feeConfigDtos, String configId) {

        if (ListUtil.isNull(feeConfigDtos)) {
            return "无";
        }

        for (FeeConfigDto feeConfigDto : feeConfigDtos) {
            if (feeConfigDto.getConfigId().equals(configId)) {
                return feeConfigDto.getFeeName();
            }
        }

        return "无";
    }

    /**
     * exists owe fee in oldReportOweFeeDtos
     * true is exists false is not
     *
     * @param oldReportOweFeeDtos
     * @param payerObjId
     * @return
     */
    private boolean existsOweFee(List<ReportOweFeeDto> oldReportOweFeeDtos, String payerObjId) {
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            // if equal return true
            if (tmpReportOweFeeDto.getPayerObjId().equals(payerObjId)) {
                return true;
            }

        }
        //default return false
        return false;
    }

    private List<FeeConfigDto> getFeeConfigs(List<ReportOweFeeDto> oweFees) {
        List<FeeConfigDto> feeConfigDtos = new ArrayList<>();
        FeeConfigDto feeConfigDto = null;
        for (int oweFeeIndex = 0; oweFeeIndex < oweFees.size(); oweFeeIndex++) {
            List<ReportOweFeeItemDto> items = oweFees.get(oweFeeIndex).getItems();
            for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
                if (existsFeeConfig(feeConfigDtos, items.get(itemIndex))) {
                    continue;
                }
                feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(items.get(itemIndex).getConfigId());
                feeConfigDto.setFeeName(items.get(itemIndex).getConfigName());
                feeConfigDtos.add(feeConfigDto);
            }
        }

        return feeConfigDtos;
    }

    private boolean existsFeeConfig(List<FeeConfigDto> feeConfigDtos, ReportOweFeeItemDto reportOweFeeItemDto) {
        if (ListUtil.isNull(feeConfigDtos)) {
            return false;
        }
        for (FeeConfigDto feeConfigDto : feeConfigDtos) {
            if (feeConfigDto.getConfigId().equals(reportOweFeeItemDto.getConfigId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * _getAllFeeOweAmount: function (_fee) {
     * let _feeConfigNames = $that.listOweFeeInfo.feeConfigNames;
     * if (_feeConfigNames.length < 1) {
     * return _fee.amountOwed;
     * }
     * <p>
     * let _amountOwed = 0.0;
     * let _items = _fee.items;
     * _feeConfigNames.forEach(_feeItem =>{
     * _items.forEach(_item=>{
     * if(_feeItem.configId == _item.configId){
     * _amountOwed += parseFloat(_item.amountOwed);
     * }
     * })
     * })
     * return _amountOwed;
     * }
     *
     * @param reportOweFeeDto
     * @return
     */
    private double getAllFeeOweAmount(List<FeeConfigDto> feeConfigDtos, ReportOweFeeDto reportOweFeeDto) {
        if (ListUtil.isNull(feeConfigDtos)) {
            return Double.parseDouble(reportOweFeeDto.getAmountOwed());
        }
        List<ReportOweFeeItemDto> items = reportOweFeeDto.getItems();
        if (ListUtil.isNull(items)) {
            return Double.parseDouble(reportOweFeeDto.getAmountOwed());
        }

        BigDecimal oweAmount = new BigDecimal(0);
        for (FeeConfigDto feeConfigDto : feeConfigDtos) {
            for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
                if (feeConfigDto.getConfigId().equals(items.get(itemIndex).getConfigId())) {
                    oweAmount = oweAmount.add(new BigDecimal(items.get(itemIndex).getAmountOwed()));
                }
            }
        }

        return oweAmount.doubleValue();
    }

    private double getFeeConfigAmount(FeeConfigDto feeConfigDto, ReportOweFeeDto reportOweFeeDto) {
        List<ReportOweFeeItemDto> items = reportOweFeeDto.getItems();
        double oweAmount = 0;

        if (ListUtil.isNull(items)) {
            return oweAmount;
        }

        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            if (feeConfigDto.getConfigId().equals(items.get(itemIndex).getConfigId())) {
                oweAmount = Double.parseDouble(items.get(itemIndex).getAmountOwed());
                break;
            }
        }
        return oweAmount;
    }

}
