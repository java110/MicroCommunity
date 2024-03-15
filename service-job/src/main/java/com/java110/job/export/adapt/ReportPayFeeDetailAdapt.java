package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 缴费明细导出
 */
@Service("reportPayFeeDetail")
public class ReportPayFeeDetailAdapt implements IExportDataAdapt {

    @Autowired
    private IQueryPayFeeDetailInnerServiceSMO queryPayFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
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
        row.createCell(3).setCellValue("费用类型");
        row.createCell(4).setCellValue("费用项");
        row.createCell(5).setCellValue("费用状态");
        row.createCell(6).setCellValue("支付方式");
        row.createCell(7).setCellValue("费用开始时间");
        row.createCell(8).setCellValue("费用结束时间");
        row.createCell(9).setCellValue("缴费时间");
        row.createCell(10).setCellValue("应缴/应收金额");
        row.createCell(11).setCellValue("实收金额");
        row.createCell(12).setCellValue("优惠金额");
        row.createCell(13).setCellValue("减免金额");
        row.createCell(14).setCellValue("赠送金额");
        row.createCell(15).setCellValue("滞纳金");
        row.createCell(16).setCellValue("面积");
        row.createCell(17).setCellValue("车位");
        row.createCell(18).setCellValue("账户抵扣");
        row.createCell(19).setCellValue("收银员");
        row.createCell(20).setCellValue("备注");
        JSONObject reqJson = exportDataDto.getReqJson();

        String endTime = reqJson.getString("endTime");

        if (!StringUtil.isEmpty(endTime) && !endTime.contains(":")) {
            endTime += " 23:59:59";
            reqJson.put("endTime", endTime);
        }
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsDto.class);
        //查询数据
        getRepairPayFeeDetail(sheet, reportFeeMonthStatisticsDto);
        return workbook;
    }

    private void getRepairPayFeeDetail(Sheet sheet, ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) throws ParseException {
        reportFeeMonthStatisticsDto.setPage(1);
        reportFeeMonthStatisticsDto.setRow(MAX_ROW);
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.query(reportFeeMonthStatisticsDto);
        appendData(resultVo, sheet, 0);
        if (resultVo.getRecords() < 2) {
            return;
        }
        for (int page = 2; page <= resultVo.getRecords(); page++) {
            reportFeeMonthStatisticsDto.setPage(page);
            reportFeeMonthStatisticsDto.setRow(MAX_ROW);
            resultVo = queryPayFeeDetailInnerServiceSMOImpl.query(reportFeeMonthStatisticsDto);
            appendData(resultVo, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(ResultVo resultVo, Sheet sheet, int step) throws ParseException {
        Object data = resultVo.getData();
        JSONArray reportFeeMonthStatisticsDtos = JSONArray.parseArray(JSONObject.toJSONString(data));
//        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = (List<ReportFeeMonthStatisticsDto>) resultVo.getData();
        Row row = null;
        JSONObject dataObj = null;
//        JSONObject reportFeeMonthStatisticsDto = null;
        Date endDate = null;
        for (int roomIndex = 0; roomIndex < reportFeeMonthStatisticsDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = reportFeeMonthStatisticsDtos.getJSONObject(roomIndex);
//            dataObj = JSONObject.parseObject(JSONObject.toJSONString(reportFeeMonthStatisticsDtos.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("oId"));
            if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(dataObj.getString("payerObjType"))) {
                String payerObjName = dataObj.getString("payerObjName");
                payerObjName += doFreshRoomInfo(dataObj.getString("payerObjId"));
                row.createCell(1).setCellValue(payerObjName);

            } else {
                row.createCell(1).setCellValue(dataObj.getString("payerObjName"));
            }
            endDate = DateUtil.getDateFromStringB(dataObj.getString("endTime"));
            //todo 如果不是一次性费用结束时间建1
            if (!StringUtil.isEmpty(dataObj.getString("feeFlag"))
                    && !FeeDto.FEE_FLAG_ONCE.equals(dataObj.getString("feeFlag"))) {
                endDate = DateUtil.stepDay(endDate, -1);
            }

            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(4).setCellValue(dataObj.getString("feeName"));
            row.createCell(5).setCellValue(dataObj.getString("stateName"));
            row.createCell(6).setCellValue(dataObj.getString("primeRate"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(7).setCellValue(DateUtil.getFormatTimeStringB(format.parse(dataObj.getString("startTime"))));
            row.createCell(8).setCellValue(DateUtil.getFormatTimeStringB(endDate));
            row.createCell(9).setCellValue(DateUtil.getFormatTimeStringA(dataObj.getDate("createTime")));
            row.createCell(10).setCellValue(dataObj.getDouble("receivableAmount"));
            row.createCell(11).setCellValue(dataObj.getDouble("receivedAmount"));
            row.createCell(12).setCellValue(dataObj.getDouble("preferentialAmount"));
            row.createCell(13).setCellValue(dataObj.getDouble("deductionAmount"));
            row.createCell(14).setCellValue(dataObj.getDouble("giftAmount"));
            row.createCell(15).setCellValue(dataObj.getDouble("lateFee"));
            row.createCell(16).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(17).setCellValue(dataObj.getString("psName"));
            row.createCell(18).setCellValue(dataObj.getString("withholdAmount"));
            row.createCell(19).setCellValue(dataObj.getString("cashierName"));
            row.createCell(20).setCellValue(dataObj.getString("remark"));
        }
    }

    private String doFreshRoomInfo(String ownerId) {

        if (StringUtil.isNullOrNone(ownerId)) {
            return "";
        }
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerId);
        ownerRoomRelDto.setPage(1);
        ownerRoomRelDto.setRow(3); //只展示3个房屋以内 不然页面太乱
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ListUtil.isNull(ownerRoomRelDtos)) {
            return "";
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String roomName = "";
        for (RoomDto tRoomDto : roomDtos) {
            roomName += (tRoomDto.getFloorNum() + "-" + tRoomDto.getUnitNum() + "-" + tRoomDto.getRoomNum() + "-" + "/");
        }
        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        return "(" + roomName + ")";

    }
}
