package com.java110.report.bmo.reportOweFee.impl;

import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.reportOweFee.ReportOweFeeItemDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.report.bmo.reportOweFee.IGetReportOweFeeBMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service("getReportOweFeeBMOImpl")
public class GetReportOweFeeBMOImpl implements IGetReportOweFeeBMO {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;


    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;


    /**
     * @param reportOweFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportOweFeeDto reportOweFeeDto) {

        reportOweFeeDto.setHasOweFee("Y");
        int count = reportOweFeeInnerServiceSMOImpl.queryReportOweFeesCount(reportOweFeeDto);

        List<ReportOweFeeDto> reportOweFeeDtos = null;
        if (count > 0) {
            reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportOweFees(reportOweFeeDto);
            refreshReportOwe(reportOweFeeDtos, reportOweFeeDto.getConfigIds());
            //查询大计 合计
            dealTheOweFeeSumAmount(reportOweFeeDtos, reportOweFeeDto);
        } else {
            reportOweFeeDtos = new ArrayList<>();
        }
        //按照 amountOwed 降序排列
        // reportOweFeeDtos.sort(Comparator.comparing(ReportOweFeeDto :: getAmountOwed).reversed());
        Collections.sort(reportOweFeeDtos, new Comparator<ReportOweFeeDto>() {
            @Override
            public int compare(ReportOweFeeDto o1, ReportOweFeeDto o2) {
                //降序
                return o2.getAmountOwed().compareTo(o1.getAmountOwed());
            }
        });
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportOweFeeDto.getRow()), count, reportOweFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 查询大计 总欠费 和 各个费用项总欠费
     *
     * @param reportOweFeeDtos
     * @param reportOweFeeDto
     */
    private void dealTheOweFeeSumAmount(List<ReportOweFeeDto> reportOweFeeDtos, ReportOweFeeDto reportOweFeeDto) {
        if (reportOweFeeDtos == null || reportOweFeeDtos.size() < 1) {
            return;
        }
        //查询总计
        double totalAmount = 0.0;
        if (reportOweFeeDto.getConfigIds() == null || reportOweFeeDto.getConfigIds().length < 1) {
            totalAmount = reportOweFeeInnerServiceSMOImpl.computeReportOweFeeTotalAmount(reportOweFeeDto);
            for (ReportOweFeeDto reportOweFeeDto1 : reportOweFeeDtos) {
                reportOweFeeDto1.setTotalOweAmount(totalAmount);
            }
            return;
        }

        //计算分项的累计值
        List<ReportOweFeeItemDto> reportOweFeeItemDtos = reportOweFeeInnerServiceSMOImpl.computeReportOweFeeItemAmount(reportOweFeeDto);

        if (reportOweFeeItemDtos == null || reportOweFeeItemDtos.size() < 1) {
            return;
        }
        BigDecimal totalAmountDes = new BigDecimal(totalAmount);
        for (ReportOweFeeItemDto tmpReportOweFeeItemDto : reportOweFeeItemDtos) {
            totalAmountDes = totalAmountDes.add(new BigDecimal(tmpReportOweFeeItemDto.getTotalOweAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        for (ReportOweFeeDto reportOweFeeDto1 : reportOweFeeDtos) {
            reportOweFeeDto1.setItemTotalOweAmounts(reportOweFeeItemDtos);
            reportOweFeeDto1.setTotalOweAmount(totalAmountDes.doubleValue());
        }
    }

    /**
     * query all owe fee
     *
     * @param reportOweFeeDto
     * @return
     */
    @Override
    public ResponseEntity<String> getAllFees(ReportOweFeeDto reportOweFeeDto) {
        List<ReportOweFeeDto> allReportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        if (allReportOweFeeDtos == null || allReportOweFeeDtos.size() < 1) {
            return ResultVo.createResponseEntity(allReportOweFeeDtos);
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
            return ResultVo.createResponseEntity(oldReportOweFeeDtos);
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
                reportOweFeeItemDto.setConfigName(getFeeConfigName(feeConfigDtos,configId));
                reportOweFeeItemDto.setAmountOwed("0");
                reportOweFeeItemDto.setPayerObjId("");
                reportOweFeeItemDto.setPayerObjName("");
                tmpReportOweFeeDto.getItems().add(reportOweFeeItemDto);
            }
        }
        return ResultVo.createResponseEntity(oldReportOweFeeDtos);
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
        List<ReportOweFeeDto> reportOweFeeDtos = new ArrayList<>();
        for (ReportOweFeeDto reportOweFee : allReportOweFeeDtos) {
            ReportOweFeeDto reportOwe = new ReportOweFeeDto();
            reportOwe.setOweId(reportOweFee.getOweId());
            if ("3333".equals(reportOweFee.getPayerObjType())) {
                List<ReportOweFeeDto> reportOweFees = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFeesByRoom(reportOwe);
                reportOweFee.setOweId(reportOweFees.get(0).getOweId());
                reportOweFee.setOwnerName(reportOweFees.get(0).getOwnerName());
                reportOweFee.setOwnerTel(reportOweFees.get(0).getOwnerTel());
            } else if ("6666".equals(reportOweFee.getPayerObjType())) {
                List<ReportOweFeeDto> reportOweFees = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFeesByCar(reportOwe);
                reportOweFee.setOweId(reportOweFees.get(0).getOweId());
                reportOweFee.setOwnerName(reportOweFees.get(0).getOwnerName());
                reportOweFee.setOwnerTel(reportOweFees.get(0).getOwnerTel());
            }else if ("7777".equals(reportOweFee.getPayerObjType())) {
                List<ReportOweFeeDto> reportOweFees = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFeesByCar(reportOwe);
                reportOweFee.setOweId(reportOweFees.get(0).getOweId());
                reportOweFee.setOwnerName(reportOweFees.get(0).getOwnerName());
                reportOweFee.setOwnerTel(reportOweFees.get(0).getOwnerTel());
            }
            reportOweFeeDtos.add(reportOweFee);
        }
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            dealItem(tmpReportOweFeeDto, reportOweFeeDtos);
        }


        if (configIds == null || configIds.length < 1) {
            return;
        }

        //如果费用对象上没有这个费用项时默认写零
        FeeConfigDto feeConfigDto = null;
        feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(configIds);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        for (ReportOweFeeDto tmpReportOweFeeDto : oldReportOweFeeDtos) {
            for (String configId : configIds) {
                if (hasItem(tmpReportOweFeeDto.getItems(), configId) != null) {
                    continue;
                }
                ReportOweFeeItemDto reportOweFeeItemDto = new ReportOweFeeItemDto();
                reportOweFeeItemDto.setConfigId(configId);
                reportOweFeeItemDto.setConfigName(getFeeConfigName(feeConfigDtos,configId));
                reportOweFeeItemDto.setAmountOwed("0");
                reportOweFeeItemDto.setPayerObjId("");
                reportOweFeeItemDto.setPayerObjName("");
                tmpReportOweFeeDto.getItems().add(reportOweFeeItemDto);
            }
        }

    }

    private String getFeeConfigName(List<FeeConfigDto> feeConfigDtos, String configId) {

        if(feeConfigDtos == null || feeConfigDtos.size()<1){
            return "无";
        }

        for(FeeConfigDto feeConfigDto:feeConfigDtos){
            if(feeConfigDto.getConfigId().equals(configId)){
                return feeConfigDto.getFeeName();
            }
        }

        return "无";
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
