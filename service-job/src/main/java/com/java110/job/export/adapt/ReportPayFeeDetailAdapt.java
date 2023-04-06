package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 缴费明细导出
 */
@Service("reportPayFeeDetail")
public class ReportPayFeeDetailAdapt implements IExportDataAdapt{

    @Autowired
    private IQueryPayFeeDetailInnerServiceSMO queryPayFeeDetailInnerServiceSMOImpl;

    private static final int MAX_ROW = 200;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("缴费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("订单号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("费用项");
        row.createCell(4).setCellValue("费用类型");
        row.createCell(5).setCellValue("费用状态");
        row.createCell(6).setCellValue("支付方式");
        row.createCell(7).setCellValue("费用开始时间");
        row.createCell(8).setCellValue("费用结束时间");
        row.createCell(9).setCellValue("缴费时间");
        row.createCell(10).setCellValue("应收金额");
        row.createCell(11).setCellValue("实收金额");
        row.createCell(12).setCellValue("优惠金额");
        row.createCell(13).setCellValue("减免金额");
        row.createCell(14).setCellValue("赠送金额");
        row.createCell(15).setCellValue("滞纳金");
        row.createCell(16).setCellValue("空置房打折金额");
        row.createCell(17).setCellValue("空置房减免金额");
        row.createCell(18).setCellValue("面积");
        row.createCell(19).setCellValue("车位");
        row.createCell(20).setCellValue("账户抵扣");
        row.createCell(21).setCellValue("收银员");

        JSONObject reqJson = exportDataDto.getReqJson();
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson,ReportFeeMonthStatisticsDto.class);
        if(reqJson.containsKey("roomName") && !StringUtil.isEmpty(reqJson.getString("roomName"))){
            String[] roomNameArray = reqJson.getString("roomName").split("-",3);
            reportFeeMonthStatisticsDto.setFloorNum(roomNameArray[0]);
            reportFeeMonthStatisticsDto.setUnitNum(roomNameArray[1]);
            reportFeeMonthStatisticsDto.setRoomNum(roomNameArray[2]);
        }

        //查询数据
        getRepairPayFeeDetail(sheet, reportFeeMonthStatisticsDto);

        return workbook;

    }

    private void getRepairPayFeeDetail(Sheet sheet, ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        reportFeeMonthStatisticsDto.setPage(1);
        reportFeeMonthStatisticsDto.setRow(MAX_ROW);
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.query(reportFeeMonthStatisticsDto);
        appendData(resultVo,sheet,0);

        if(resultVo.getRecords() < 2){
            return ;
        }

        for(int page = 2;page <= resultVo.getRecords(); page++){
            reportFeeMonthStatisticsDto.setPage(page);
            reportFeeMonthStatisticsDto.setRow(MAX_ROW);
            resultVo = queryPayFeeDetailInnerServiceSMOImpl.query(reportFeeMonthStatisticsDto);
            appendData(resultVo,sheet,(page-1)*MAX_ROW);
        }
    }

    private void appendData(ResultVo resultVo,Sheet sheet,int step) {

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = (List<ReportFeeMonthStatisticsDto>)resultVo.getData();
        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < reportFeeMonthStatisticsDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex +step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(reportFeeMonthStatisticsDtos.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("oId"));
            if (!StringUtil.isEmpty(dataObj.getString("payerObjType")) && dataObj.getString("payerObjType").equals("3333")) { //房屋
                row.createCell(1).setCellValue(dataObj.getString("floorNum") + "-" + dataObj.getString("unitNum") + "-" + dataObj.getString("roomNum"));
            } else {
                row.createCell(1).setCellValue(dataObj.getString("objName"));
            }
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("feeName"));
            row.createCell(4).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(5).setCellValue(dataObj.getString("stateName"));
            row.createCell(6).setCellValue(dataObj.getString("primeRate"));
            row.createCell(7).setCellValue(dataObj.getString("startTime"));
            row.createCell(8).setCellValue(dataObj.getString("endTime"));
            row.createCell(9).setCellValue(DateUtil.getFormatTimeString(dataObj.getDate("createTime"),DateUtil.DATE_FORMATE_STRING_A));
            row.createCell(10).setCellValue(dataObj.getDouble("receivableAmount"));
            row.createCell(11).setCellValue(dataObj.getDouble("receivedAmount"));
            row.createCell(12).setCellValue(dataObj.getDouble("preferentialAmount"));
            row.createCell(13).setCellValue(dataObj.getDouble("deductionAmount"));
            row.createCell(14).setCellValue(dataObj.getDouble("giftAmount"));
            row.createCell(15).setCellValue(dataObj.getDouble("lateFee"));
            row.createCell(16).setCellValue(dataObj.getDouble("vacantHousingDiscount"));
            row.createCell(17).setCellValue(dataObj.getDouble("vacantHousingReduction"));
            row.createCell(18).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(19).setCellValue(dataObj.getString("psName"));
            row.createCell(20).setCellValue(dataObj.getString("withholdAmount"));
            row.createCell(21).setCellValue(dataObj.getString("cashierName"));

        }
    }
}
