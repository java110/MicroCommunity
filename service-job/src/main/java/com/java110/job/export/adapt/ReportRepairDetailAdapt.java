package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component("reportRepairDetail")
public class ReportRepairDetailAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        JSONObject paramIn = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("报修汇总表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("员工ID");
        row.createCell(1).setCellValue("员工姓名");
        row.createCell(2).setCellValue("处理中条数");
        row.createCell(3).setCellValue("派单条数");
        row.createCell(4).setCellValue("转单条数");
        row.createCell(5).setCellValue("退单条数");
        row.createCell(6).setCellValue("已回访条数");
        row.createCell(7).setCellValue("已完结条数");
        row.createCell(8).setCellValue("员工评分");
        RepairUserDto repairUserDto = BeanConvertUtil.covertBean(paramIn,RepairUserDto.class);
        if (!StringUtil.isEmpty(repairUserDto.getBeginStartTime())) {
            repairUserDto.setBeginStartTime(repairUserDto.getBeginStartTime() + " 00:00:00");
        }
        if (!StringUtil.isEmpty(repairUserDto.getBeginEndTime())) {
            repairUserDto.setBeginEndTime(repairUserDto.getBeginEndTime() + " 23:59:59");
        }
        if (!StringUtil.isEmpty(repairUserDto.getFinishStartTime())) {
            repairUserDto.setFinishStartTime(repairUserDto.getFinishStartTime() + " 00:00:00");
        }
        if (!StringUtil.isEmpty(repairUserDto.getFinishEndTime())) {
            repairUserDto.setFinishEndTime(repairUserDto.getFinishEndTime() + " 23:59:59");
        }
        List<RepairUserDto> repairs = this.queryRepair(repairUserDto);
        if (ListUtil.isNull(repairs)) {
            return workbook;
        }
        RepairUserDto dataObj = null;
        for (int roomIndex = 0; roomIndex < repairs.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = repairs.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.getStaffId());
            row.createCell(1).setCellValue(dataObj.getStaffName());
            row.createCell(2).setCellValue(dataObj.getDealAmount());
            row.createCell(3).setCellValue(dataObj.getDispatchAmount());
            row.createCell(4).setCellValue(dataObj.getTransferOrderAmount());
            row.createCell(5).setCellValue(dataObj.getChargebackAmount());
            row.createCell(6).setCellValue(dataObj.getReturnAmount());
            row.createCell(7).setCellValue(dataObj.getStatementAmount());
            row.createCell(8).setCellValue(dataObj.getScore());
        }
        return workbook;
    }

    public List<RepairUserDto> queryRepair(RepairUserDto repairUserDto) {
        //查询员工报修表员工信息
        List<RepairUserDto> repairUsers = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        int count = repairUsers.size();
        //获取员工id和姓名集合
        List<RepairUserDto> staffs;
        if (StringUtil.isEmpty(repairUserDto.getStaffId())) {
            repairUserDto.setPage(-1);
            staffs = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        } else {
            repairUserDto.setPage(-1);
            repairUserDto.setStaffId(null);
            staffs = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        }
        List<RepairUserDto> repairUserList = new ArrayList<>();
        //处理中总数量
        int dealNumber = 0;
        //结单总数量
        int statementNumber = 0;
        //退单总数量
        int chargebackNumber = 0;
        //转单总数量
        int transferOrderNumber = 0;
        //派单总数量
        int dispatchNumber = 0;
        //已回访总数量
        int returnNumber = 0;
        if (count > 0) {
            for (RepairUserDto repairUser : repairUsers) {
                RepairUserDto repairUserInfo = new RepairUserDto();
                //员工id
                repairUserDto.setStaffId(repairUser.getStaffId());
                List<RepairUserDto> repairUserDtoList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairWithOutPage(repairUserDto);
                if (ListUtil.isNull(repairUserDtoList)) {
                    continue;
                }
                //处理中数量
                int dealAmount = 0;
                //结单数量
                int statementAmount = 0;
                //退单数量
                int chargebackAmount = 0;
                //转单数量
                int transferOrderAmount = 0;
                //派单数量
                int dispatchAmount = 0;
                //回访数量
                int returnAmount = 0;
                //评分
                String score = "";
                for (RepairUserDto repair : repairUserDtoList) {
                    //处理中状态
                    if (repair.getState().equals("10001")) {
                        //获取数量
                        int amount = Integer.parseInt(repair.getAmount());
                        dealAmount = dealAmount + amount;
                    } else if (repair.getState().equals("10002")) {  //结单状态
                        //获取数量
                        int amount = Integer.parseInt(repair.getAmount());
                        statementAmount = statementAmount + amount;
                    } else if (repair.getState().equals("10003")) {  //退单状态
                        //获取数量
                        int amount = Integer.parseInt(repair.getAmount());
                        chargebackAmount = chargebackAmount + amount;
                    } else if (repair.getState().equals("10004")) {  //转单状态
                        //获取数量
                        int amount = Integer.parseInt(repair.getAmount());
                        transferOrderAmount = transferOrderAmount + amount;
                    } else if (repair.getState().equals("10006")) {  //派单状态
                        int amount = Integer.parseInt(repair.getAmount());
                        dispatchAmount = dispatchAmount + amount;
                    } else if (repair.getState().equals("10008")) {  //已回访状态
                        int amount = Integer.parseInt(repair.getAmount());
                        returnAmount = returnAmount + amount;
                    }
                    if (!StringUtil.isEmpty(repair.getScore())) {
                        score = repair.getScore();
                    }
                }
                //员工id
                repairUserInfo.setStaffId(repairUser.getStaffId());
                //员工姓名
                repairUserInfo.setStaffName(repairUser.getStaffName());
                //处理中报修数量
                repairUserInfo.setDealAmount(Integer.toString(dealAmount));
                //处理中报修总数量
                repairUserInfo.setDealNumber(Integer.toString(dealNumber));
                //结单报修数量
                repairUserInfo.setStatementAmount(Integer.toString(statementAmount));
                //结单报修总数量
                repairUserInfo.setStatementNumber(Integer.toString(statementNumber));
                //退单报修数量
                repairUserInfo.setChargebackAmount(Integer.toString(chargebackAmount));
                //退单报修总数量
                repairUserInfo.setChargebackNumber(Integer.toString(chargebackNumber));
                //转单报修数量
                repairUserInfo.setTransferOrderAmount(Integer.toString(transferOrderAmount));
                //转单报修总数量
                repairUserInfo.setTransferOrderNumber(Integer.toString(transferOrderNumber));
                //派单报修数量
                repairUserInfo.setDispatchAmount(Integer.toString(dispatchAmount));
                //派单报修总数量
                repairUserInfo.setDispatchNumber(Integer.toString(dispatchNumber));
                //回访数量
                repairUserInfo.setReturnAmount(Integer.toString(returnAmount));
                //回访总数量
                repairUserInfo.setReturnNumber(Integer.toString(returnNumber));
                //员工id和姓名信息集合
                repairUserInfo.setRepairList(staffs);
                //员工评分
                repairUserInfo.setScore(score);
                repairUserList.add(repairUserInfo);

                dealNumber = Integer.parseInt(repairUserInfo.getDealAmount()) + dealNumber;
                statementNumber = Integer.parseInt(repairUserInfo.getStatementAmount()) + statementNumber;
                chargebackNumber = Integer.parseInt(repairUserInfo.getChargebackAmount()) + chargebackNumber;
                transferOrderNumber = Integer.parseInt(repairUserInfo.getTransferOrderAmount()) + transferOrderNumber;
                dispatchNumber = Integer.parseInt(repairUserInfo.getDispatchAmount()) + dispatchNumber;
                returnNumber = Integer.parseInt(repairUserInfo.getReturnAmount()) + returnNumber;
            }
        } else {
            repairUserList = new ArrayList<>();
        }

        RepairUserDto repairUser = new RepairUserDto();
        repairUser.setDealNumber(Integer.toString(dealNumber));
        repairUser.setStatementNumber(Integer.toString(statementNumber));
        repairUser.setChargebackNumber(Integer.toString(chargebackNumber));
        repairUser.setTransferOrderNumber(Integer.toString(transferOrderNumber));
        repairUser.setDispatchNumber(Integer.toString(dispatchNumber));
        repairUser.setReturnNumber(Integer.toString(returnNumber));

        return repairUserList;
    }
}
