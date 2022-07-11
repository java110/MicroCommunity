package com.java110.report.bmo.reportOwnerPayFee.impl;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.report.bmo.reportOwnerPayFee.IGetReportOwnerPayFeeBMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("getReportOwnerPayFeeBMOImpl")
public class GetReportOwnerPayFeeBMOImpl implements IGetReportOwnerPayFeeBMO {

    @Autowired
    private IReportOwnerPayFeeInnerServiceSMO reportOwnerPayFeeInnerServiceSMOImpl;

    /**
     * @param reportOwnerPayFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportOwnerPayFeeDto reportOwnerPayFeeDto) {
        if (!StringUtil.isEmpty(reportOwnerPayFeeDto.getPfYear())) {
            reportOwnerPayFeeDto.setFeeEndTime(reportOwnerPayFeeDto.getPfYear() + "-01-01");
        } else {
            Calendar calendar = Calendar.getInstance();
            reportOwnerPayFeeDto.setFeeEndTime(calendar.get(Calendar.YEAR) + "-01-01");
        }
        int count = reportOwnerPayFeeInnerServiceSMOImpl.queryReportOwnerPayFeesCount(reportOwnerPayFeeDto);

        List<ReportOwnerPayFeeDto> reportOwnerPayFeeDtos = null;
        if (count > 0) {
            reportOwnerPayFeeDtos = reportOwnerPayFeeInnerServiceSMOImpl.queryReportOwnerPayFees(reportOwnerPayFeeDto);
            refreshOwnerPayFeeDto(reportOwnerPayFeeDtos, reportOwnerPayFeeDto);
        } else {
            reportOwnerPayFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportOwnerPayFeeDto.getRow()), count, reportOwnerPayFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * @param reportOwnerPayFeeDtos
     */
    private void refreshOwnerPayFeeDto(List<ReportOwnerPayFeeDto> reportOwnerPayFeeDtos, ReportOwnerPayFeeDto tmpReportOwnerPayFeeDto) {
        if (reportOwnerPayFeeDtos == null || reportOwnerPayFeeDtos.size() < 1) {
            return;
        }
        List<String> feeIds = new ArrayList<>();
        for (ReportOwnerPayFeeDto reportOwnerPayFeeDto : reportOwnerPayFeeDtos) {
            if (!StringUtil.isEmpty(reportOwnerPayFeeDto.getImportFeeName())) {
                reportOwnerPayFeeDto.setFeeName(reportOwnerPayFeeDto.getImportFeeName());
            }
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reportOwnerPayFeeDto.getPayerObjType())) {
                reportOwnerPayFeeDto.setObjName(reportOwnerPayFeeDto.getRoomName());
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportOwnerPayFeeDto.getPayerObjType())) {
                reportOwnerPayFeeDto.setObjName(reportOwnerPayFeeDto.getCarNum());
            } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(reportOwnerPayFeeDto.getPayerObjType())) {
                reportOwnerPayFeeDto.setObjName(reportOwnerPayFeeDto.getContractCode());
            }
            feeIds.add(reportOwnerPayFeeDto.getFeeId());
        }

        ReportOwnerPayFeeDto reportOwnerPayFeeDto = new ReportOwnerPayFeeDto();
        reportOwnerPayFeeDto.setFeeIds(feeIds.toArray(new String[feeIds.size()]));
        reportOwnerPayFeeDto.setPfYear(tmpReportOwnerPayFeeDto.getPfYear());
        reportOwnerPayFeeDto.setConfigId(tmpReportOwnerPayFeeDto.getConfigId());

        if (StringUtil.isEmpty(reportOwnerPayFeeDto.getPfYear())) {
            reportOwnerPayFeeDto.setPfYear(DateUtil.getYear() + "");
        }
        List<ReportOwnerPayFeeDto> tmpReportOwnerPayFeeDtos
                = reportOwnerPayFeeInnerServiceSMOImpl.queryReportOwnerMonthPayFees(reportOwnerPayFeeDto);


        List<ReportOwnerPayFeeDto> tmoNewReportOwnerPayFeeDtos = null;

        for (ReportOwnerPayFeeDto oldReportOwnerPayFeeDto : reportOwnerPayFeeDtos) {
            tmoNewReportOwnerPayFeeDtos = new ArrayList<>();
            for (ReportOwnerPayFeeDto newReportOwnerPayFeeDto : tmpReportOwnerPayFeeDtos) {
                if (oldReportOwnerPayFeeDto.getFeeId().equals(newReportOwnerPayFeeDto.getFeeId())) {
                    tmoNewReportOwnerPayFeeDtos.add(newReportOwnerPayFeeDto);
                }
            }
            oldReportOwnerPayFeeDto.setReportOwnerPayFeeDtos(tmoNewReportOwnerPayFeeDtos);
        }


    }

}
