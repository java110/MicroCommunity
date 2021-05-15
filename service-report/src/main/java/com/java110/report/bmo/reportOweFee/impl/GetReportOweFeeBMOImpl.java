package com.java110.report.bmo.reportOweFee.impl;

import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.reportOweFee.ReportOweFeeItemDto;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.report.bmo.reportOweFee.IGetReportOweFeeBMO;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("getReportOweFeeBMOImpl")
public class GetReportOweFeeBMOImpl implements IGetReportOweFeeBMO {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;


    /**
     * @param reportOweFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportOweFeeDto reportOweFeeDto) {


        int count = reportOweFeeInnerServiceSMOImpl.queryReportOweFeesCount(reportOweFeeDto);

        List<ReportOweFeeDto> reportOweFeeDtos = null;
        if (count > 0) {
            reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportOweFees(reportOweFeeDto);
            refreshReportOwe(reportOweFeeDtos, reportOweFeeDto.getConfigIds());
        } else {
            reportOweFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportOweFeeDto.getRow()), count, reportOweFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshReportOwe(List<ReportOweFeeDto> oldReportOweFeeDtos, String[] configIds) {
        List<String> payObjIds = new ArrayList<>();

        if (oldReportOweFeeDtos == null || oldReportOweFeeDtos.size() < 1) {
            return;
        }

        for (ReportOweFeeDto reportOweFeeDto : oldReportOweFeeDtos) {
            payObjIds.add(reportOweFeeDto.getPayerObjId());
        }
        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setPayerObjIds(payObjIds.toArray(new String[payObjIds.size()]));
        List<ReportOweFeeDto> allReportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);

        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            dealItem(tmpReportOweFeeDto, allReportOweFeeDtos);
        }

        if (configIds == null || configIds.length < 1) {
            return;
        }

        //如果费用对象上没有这个费用项时默认写零
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            for (String configId : configIds) {
                if (hasItem(tmpReportOweFeeDto.getItems(), configId) != null) {
                    continue;
                }
                ReportOweFeeItemDto reportOweFeeItemDto = new ReportOweFeeItemDto();
                reportOweFeeItemDto.setConfigId(configId);
                reportOweFeeItemDto.setFeeName("");
                reportOweFeeItemDto.setAmountOwed("0");
                reportOweFeeItemDto.setPayerObjId("");
                reportOweFeeItemDto.setPayerObjName("");
                tmpReportOweFeeDto.getItems().add(reportOweFeeItemDto);
            }
        }

    }

    private void dealItem(ReportOweFeeDto oldReportOweFeeDto, List<ReportOweFeeDto> allReportOweFeeDtos) {
        List<ReportOweFeeItemDto> items = new ArrayList<>();
        if (allReportOweFeeDtos == null || allReportOweFeeDtos.size() < 1) {
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
                try {
                    reportOweFeeItemDto.setStartTime(DateUtil.getDateFromString(reportOweFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                    reportOweFeeItemDto.setEndTime(DateUtil.getDateFromString(reportOweFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                items.add(reportOweFeeItemDto);
            } else {
                BigDecimal oldAmount = new BigDecimal(Double.parseDouble(reportOweFeeItemDto.getAmountOwed()));
                oldAmount = oldAmount.add(new BigDecimal(Double.parseDouble(reportOweFeeDto.getAmountOwed())));
                reportOweFeeItemDto.setAmountOwed(oldAmount.doubleValue() + "");
            }
            oldReportOweFeeDto.setOwnerName(reportOweFeeDto.getOwnerName());
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
                endTime = tempReportOweFeeItemDto.getStartTime();
            }
            totalAmount = totalAmount.add(new BigDecimal(Double.parseDouble(tempReportOweFeeItemDto.getAmountOwed())));
        }
        oldReportOweFeeDto.setEndTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setDeadlineTime(DateUtil.getFormatTimeString(endTime, DateUtil.DATE_FORMATE_STRING_A));
        oldReportOweFeeDto.setItems(items);
        oldReportOweFeeDto.setPayerObjName(items.get(0).getPayerObjName());
        oldReportOweFeeDto.setAmountOwed(totalAmount.doubleValue() + "");
    }

    private ReportOweFeeItemDto hasItem(List<ReportOweFeeItemDto> reportOweFeeItemDtos, String configId) {
        if (reportOweFeeItemDtos == null || reportOweFeeItemDtos.size() < 1) {
            return null;
        }
        for (ReportOweFeeItemDto reportOweFeeItemDto : reportOweFeeItemDtos) {
            if (reportOweFeeItemDto.getConfigId().equals(configId)) {
                return reportOweFeeItemDto;
            }
        }

        return null;
    }

}
