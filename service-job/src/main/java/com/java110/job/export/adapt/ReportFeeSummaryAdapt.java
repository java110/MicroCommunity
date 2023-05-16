package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.FloorDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("reportFeeSummary")
public class ReportFeeSummaryAdapt implements IExportDataAdapt {

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("费用汇总表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("楼栋");
        row.createCell(1).setCellValue("收费户");
        row.createCell(2).setCellValue("欠费户");
        row.createCell(3).setCellValue("历史欠费+当期欠费=欠费");
        row.createCell(6).setCellValue("欠费追回+当期实收+预交=实收");
        row.createCell(10).setCellValue("已交户/收费户=户收费率");
        row.createCell(11).setCellValue("(实收-预交)/(历史欠费+当期应收)=收费率");

        JSONObject reqJson = exportDataDto.getReqJson();

        //查询数据
        doReportFeeSummary(sheet, reqJson);

        //todo 合并 3 4 5
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
        return workbook;

    }

    private void doReportFeeSummary(Sheet sheet, JSONObject reqJson) {

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorV1InnerServiceSMOImpl.queryFloors(floorDto);

        JSONObject floorSummaryData = null;
        for (int floorIndex = 0; floorIndex < floorDtos.size(); floorIndex++) {
            floorSummaryData = queryFloorFeeSummary(floorDtos.get(floorIndex), reqJson);
            appendData(floorSummaryData, sheet, floorIndex + 1, floorDtos.get(floorIndex));
        }
    }


    private void appendData(JSONObject dataObj, Sheet sheet, int step, FloorDto floorDto) {

        Row row = null;
        row = sheet.createRow(step);
        row.createCell(0).setCellValue(floorDto.getFloorName());
        row.createCell(1).setCellValue(dataObj.getIntValue("feeRoomCount"));
        row.createCell(2).setCellValue(dataObj.getIntValue("oweRoomCount"));
        row.createCell(3).setCellValue(dataObj.getDouble("hisOweFee"));
        row.createCell(4).setCellValue(dataObj.getDouble("curOweFee"));

        BigDecimal curOweFee = new BigDecimal(dataObj.getDouble("curOweFee"));
        BigDecimal hisOweFee = new BigDecimal(dataObj.getDouble("hisOweFee"));
        row.createCell(5).setCellValue(curOweFee.add(hisOweFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        row.createCell(6).setCellValue(dataObj.getDouble("hisReceivedFee"));
        //(fee.receivedFee-fee.hisReceivedFee-fee.preReceivedFee)
        BigDecimal receivedFee = new BigDecimal(dataObj.getDouble("receivedFee"));
        BigDecimal hisReceivedFee = new BigDecimal(dataObj.getDouble("hisReceivedFee"));
        BigDecimal preReceivedFee = new BigDecimal(dataObj.getDouble("preReceivedFee"));
        row.createCell(7).setCellValue(receivedFee.subtract(hisReceivedFee).subtract(preReceivedFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        row.createCell(8).setCellValue(dataObj.getDouble("preReceivedFee"));
        row.createCell(9).setCellValue(dataObj.getDouble("receivedFee"));
        //((fee.feeRoomCount-fee.oweRoomCount)/fee.feeRoomCount*100).toFixed(2)

        BigDecimal feeRoomCount = new BigDecimal(dataObj.getDouble("feeRoomCount"));
        BigDecimal oweRoomCount = new BigDecimal(dataObj.getDouble("oweRoomCount"));
        BigDecimal roomFeeRate = new BigDecimal(0);
        if(feeRoomCount.doubleValue()>0){
            feeRoomCount.subtract(oweRoomCount).divide(feeRoomCount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        row.createCell(10).setCellValue(roomFeeRate.doubleValue() + "%");
        //((fee.receivedFee-fee.preReceivedFee)/(fee.hisOweFee+fee.curReceivableFee)*100).toFixed(2)
        BigDecimal curReceivableFee = new BigDecimal(dataObj.getDouble("curReceivableFee"));
        curReceivableFee = hisOweFee.add(curReceivableFee);
        roomFeeRate = new BigDecimal(0);
        if(curReceivableFee.doubleValue()> 0) {
            roomFeeRate = receivedFee.subtract(preReceivedFee).divide(curReceivableFee, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        row.createCell(11).setCellValue(roomFeeRate.doubleValue() + "%");

    }

    /**
     * 查询楼栋费用汇总信息
     *
     * @param floorDto
     * @param reqJson
     * @return
     */
    private JSONObject queryFloorFeeSummary(FloorDto floorDto, JSONObject reqJson) {

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(floorDto.getFloorId());
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        if (reqJson.containsKey("configIds")) {
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }

        //todo 查询历史欠费
        double hisOweFee = reportFeeStatisticsInnerServiceSMOImpl.getHisMonthOweFee(queryStatisticsDto);

        //todo 查询 单月欠费
        double curOweFee = reportFeeStatisticsInnerServiceSMOImpl.getCurMonthOweFee(queryStatisticsDto);

        //todo 查询当月应收
        double curReceivableFee = reportFeeStatisticsInnerServiceSMOImpl.getCurReceivableFee(queryStatisticsDto);

        //todo 查询 欠费追回
        double hisReceivedFee = reportFeeStatisticsInnerServiceSMOImpl.getHisReceivedFee(queryStatisticsDto);

        //todo  查询 预交费用
        double preReceivedFee = reportFeeStatisticsInnerServiceSMOImpl.getPreReceivedFee(queryStatisticsDto);

        //todo 查询实收
        double receivedFee = reportFeeStatisticsInnerServiceSMOImpl.getReceivedFee(queryStatisticsDto);

        //todo 空闲房屋数
        long feeRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getFeeRoomCount(queryStatisticsDto);

        //todo 欠费户数
        int oweRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getOweRoomCount(queryStatisticsDto);

        JSONObject data = new JSONObject();
        data.put("hisOweFee", hisOweFee);
        data.put("curOweFee", curOweFee);
        data.put("hisReceivedFee", hisReceivedFee);
        data.put("preReceivedFee", preReceivedFee);
        data.put("receivedFee", receivedFee);
        data.put("feeRoomCount", feeRoomCount);
        data.put("oweRoomCount", oweRoomCount);
        data.put("curReceivableFee", curReceivableFee);

        return data;
    }
}
