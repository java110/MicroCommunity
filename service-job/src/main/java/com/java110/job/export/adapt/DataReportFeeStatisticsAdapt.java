package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service("dataReportFeeStatistics")
public class DataReportFeeStatisticsAdapt implements IExportDataAdapt {

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        JSONObject reqJson = exportDataDto.getReqJson();
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }

        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("欠费统计");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("楼栋");
        row.createCell(1).setCellValue("户数");
        row.createCell(2).setCellValue("收费户");
        row.createCell(3).setCellValue("历史欠费");
        row.createCell(4).setCellValue("总欠费");
        row.createCell(5).setCellValue("本日已交户数");
        row.createCell(6).setCellValue("本日已交金额");
        row.createCell(7).setCellValue("历史欠费清缴户");
        row.createCell(8).setCellValue("历史欠费清缴金额");
        row.createCell(9).setCellValue("本月已收户数");
        row.createCell(10).setCellValue("剩余户数");
        row.createCell(11).setCellValue("已收户占比");
        row.createCell(12).setCellValue("当月已收金额");
        row.createCell(13).setCellValue("当月剩余未收");
        row.createCell(14).setCellValue("收费率");

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorV1InnerServiceSMOImpl.queryFloors(floorDto);
        if (floorDtos == null || floorDtos.size() < 1) {
            return workbook;
        }

        JSONArray datas = new JSONArray();
        //todo 根据楼栋ID循环查询
        for (FloorDto tmpFloorDto : floorDtos) {
            //todo 获取到数据
            doGetData(tmpFloorDto.getFloorId(), datas, reqJson);
        }

        if (datas == null || datas.size() < 1) {
            return workbook;
        }
        JSONObject data = null;
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            data = datas.getJSONObject(dataIndex);
            appendData(data, sheet, dataIndex);
        }


        return workbook;
    }

    /**
     * 封装数据到Excel中
     *
     * @param sheet
     */
    private void appendData(JSONObject data, Sheet sheet, int seq) {

        Row row = sheet.createRow(seq + 1);

        row.createCell(0).setCellValue(data.getString("floorNum"));
        row.createCell(1).setCellValue(data.getString("roomCount"));
        row.createCell(2).setCellValue(data.getString("feeCount"));
        row.createCell(3).setCellValue(data.getString("hisMonthOweFee"));
        row.createCell(4).setCellValue(data.getString("oweFee"));
        row.createCell(5).setCellValue(data.getString("todayReceivedRoomCount"));
        row.createCell(6).setCellValue(data.getString("todayReceivedRoomAmount"));
        row.createCell(7).setCellValue(data.getString("hisOweReceivedRoomCount"));
        row.createCell(8).setCellValue(data.getString("hisOweReceivedRoomAmount"));
        row.createCell(9).setCellValue(data.getString("monthReceivedRoomCount"));
        row.createCell(10).setCellValue(data.getIntValue("oweRoomCount"));
        BigDecimal feeRoomCount = new BigDecimal(data.getIntValue("feeRoomCount"));
        if (feeRoomCount.doubleValue() == 0) {
            row.createCell(11).setCellValue("0%");
        } else {
            BigDecimal roomCount = new BigDecimal(data.getIntValue("feeRoomCount") - data.getIntValue("oweRoomCount"));
            feeRoomCount = roomCount.divide(feeRoomCount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            row.createCell(11).setCellValue(feeRoomCount.doubleValue() + "%");
        }
        row.createCell(12).setCellValue(data.getString("monthReceivedRoomAmount"));
        row.createCell(13).setCellValue(data.getString("curMonthOweFee"));

        BigDecimal monthReceivedRoomAmount = new BigDecimal(data.getIntValue("monthReceivedRoomAmount"));
        BigDecimal hisMonthOweFee = new BigDecimal(data.getIntValue("hisMonthOweFee"));
        BigDecimal hisReceivedFee = new BigDecimal(data.getIntValue("hisReceivedFee"));
        BigDecimal curReceivableFee = new BigDecimal(data.getIntValue("curReceivableFee"));

       BigDecimal divideFee = hisMonthOweFee.add(hisReceivedFee).add(curReceivableFee);
       if(divideFee.doubleValue() == 0){
           row.createCell(14).setCellValue("0%");
       }else{
           BigDecimal preReceivedFee = new BigDecimal(data.getIntValue("preReceivedFee"));
           monthReceivedRoomAmount = monthReceivedRoomAmount.subtract(preReceivedFee).divide(divideFee, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
           row.createCell(14).setCellValue(monthReceivedRoomAmount.doubleValue() + "%");
       }
    }

    /**
     * 查询数据
     *
     * @param floorId
     * @param datas
     */
    private void doGetData(String floorId, JSONArray datas, JSONObject reqJson) {
        JSONObject data = new JSONObject();
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setFloorId(floorId);
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));

        String monthFastDate = DateUtil.getFormatTimeStringB(DateUtil.getFirstDate(reqJson.getString("startDate")));
        String monthLastDate = DateUtil.getFormatTimeStringB(DateUtil.getNextMonthFirstDate(reqJson.getString("startDate")));
        String startDate = reqJson.getString("startDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(startDate));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String endDate = DateUtil.getFormatTimeStringB(calendar.getTime());

        // todo 查询楼栋
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorId(floorId);
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorV1InnerServiceSMOImpl.queryFloors(floorDto);
        Assert.listOnlyOne(floorDtos, "楼栋不存在");
        data.put("floorNum", floorDtos.get(0).getFloorNum());

        // todo 查询户数
        long roomCount = getRoomCount(queryStatisticsDto);
        data.put("roomCount", roomCount);


        // todo 查询 历史欠费
        //这里设置查询月1日 不然历史和总欠费一样有点奇怪
        queryStatisticsDto.setStartDate(monthFastDate);
        double hisMonthOweFee = reportFeeStatisticsInnerServiceSMOImpl.getHisMonthOweFee(queryStatisticsDto);
        data.put("hisMonthOweFee", hisMonthOweFee);

        // todo 查询总欠费
        queryStatisticsDto.setEndDate(monthLastDate);
        double oweFee = reportFeeStatisticsInnerServiceSMOImpl.getOweFee(queryStatisticsDto);
        data.put("oweFee", oweFee);

        // todo 本日已交户数
        queryStatisticsDto.setStartDate(startDate);
        queryStatisticsDto.setEndDate(endDate);
        double todayReceivedRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getReceivedRoomCount(queryStatisticsDto);
        data.put("todayReceivedRoomCount", todayReceivedRoomCount);

        // todo 本日已交金额
        double todayReceivedRoomAmount = reportFeeStatisticsInnerServiceSMOImpl.getReceivedRoomAmount(queryStatisticsDto);
        data.put("todayReceivedRoomAmount", todayReceivedRoomAmount);

        // todo 历史欠费清缴户
        queryStatisticsDto.setStartDate(startDate);
        queryStatisticsDto.setEndDate(endDate);
        queryStatisticsDto.setHisDate(monthFastDate);
        double hisOweReceivedRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getHisOweReceivedRoomCount(queryStatisticsDto);
        data.put("hisOweReceivedRoomCount", hisOweReceivedRoomCount);
        // todo 历史欠费清缴金额
        double hisOweReceivedRoomAmount = reportFeeStatisticsInnerServiceSMOImpl.getHisOweReceivedRoomAmount(queryStatisticsDto);
        data.put("hisOweReceivedRoomAmount", hisOweReceivedRoomAmount);

        // todo 本月已收户
        queryStatisticsDto.setStartDate(monthFastDate);
        queryStatisticsDto.setEndDate(monthLastDate);
        double monthReceivedRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getMonthReceivedDetailCount(queryStatisticsDto);
        data.put("monthReceivedRoomCount", monthReceivedRoomCount);

        // todo 查询收费户
        long feeRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getFeeRoomCount(queryStatisticsDto);
        data.put("feeRoomCount", feeRoomCount);

        // todo 计算欠费户
        int oweRoomCount = reportFeeStatisticsInnerServiceSMOImpl.getOweRoomCount(queryStatisticsDto);
        data.put("oweRoomCount", oweRoomCount);

        // todo 当月已收金额
        double monthReceivedRoomAmount = reportFeeStatisticsInnerServiceSMOImpl.getMonthReceivedDetailAmount(queryStatisticsDto);
        data.put("monthReceivedRoomAmount", monthReceivedRoomAmount);
        // todo 剩余未收
        double curMonthOweFee = reportFeeStatisticsInnerServiceSMOImpl.getCurMonthOweFee(queryStatisticsDto);
        data.put("curMonthOweFee", curMonthOweFee);

        //todo 查询当月应收
        queryStatisticsDto.setStartDate(monthFastDate);
        queryStatisticsDto.setEndDate(monthLastDate);
        double curReceivableFee = reportFeeStatisticsInnerServiceSMOImpl.getCurReceivableFee(queryStatisticsDto);
        data.put("curReceivableFee", curReceivableFee);

//        //todo 查询 欠费追回
//        queryStatisticsDto.setStartDate(monthFastDate);
//        queryStatisticsDto.setEndDate(monthLastDate);
//        double hisReceivedFee = reportFeeStatisticsInnerServiceSMOImpl.getHisReceivedFee(queryStatisticsDto);
//        data.put("hisReceivedFee", hisReceivedFee);
//
//        //todo  查询 预交费用
//        queryStatisticsDto.setStartDate(monthFastDate);
//        queryStatisticsDto.setEndDate(monthLastDate);
//        double preReceivedFee = reportFeeStatisticsInnerServiceSMOImpl.getPreReceivedFee(queryStatisticsDto);
//        data.put("preReceivedFee", preReceivedFee);

        datas.add(data);


    }


    /**
     * 查询全部房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setOwnerName(queryStatisticsDto.getOwnerName());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setLink(queryStatisticsDto.getLink());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return baseDataStatisticsInnerServiceSMOImpl.getRoomCount(roomDto);
    }


    /**
     * 查询空闲房屋
     *
     * @param queryStatisticsDto
     * @return
     */
    public long getFreeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setState(RoomDto.STATE_FREE);
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
    }

    /**
     * roomNum 拆分 查询房屋信息
     *
     * @param queryStatisticsDto
     * @param roomDto
     */
    private void addRoomNumCondition(QueryStatisticsDto queryStatisticsDto, RoomDto roomDto) {
        if (StringUtil.isEmpty(queryStatisticsDto.getObjName())) {
            return;
        }
        if (!queryStatisticsDto.getObjName().contains("-")) {
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
            return;
        }
        String[] objNames = queryStatisticsDto.getObjName().split("-");
        if (objNames.length == 2) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum("0");
            roomDto.setRoomNum(objNames[1]);
            return;
        }
        objNames = queryStatisticsDto.getObjName().split("-", 3);
        if (objNames.length == 3) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum(objNames[1]);
            roomDto.setRoomNum(objNames[2]);
        }

    }
}
