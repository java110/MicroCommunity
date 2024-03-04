package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 房屋收费导出
 *
 * @author fqz
 * @date 2023-12-14 14:18
 */
@Service(value = "roomCreateFee")
public class RoomCreateFeeAdapt implements IExportDataAdapt {

    private static Logger logger = LoggerFactory.getLogger(RoomCreateFeeAdapt.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("房屋收费");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用项目");
        row.createCell(1).setCellValue("费用标识");
        row.createCell(2).setCellValue("费用类型");
        row.createCell(3).setCellValue("应收金额");
        row.createCell(4).setCellValue("建账时间");
        row.createCell(5).setCellValue("应收时间段");
        row.createCell(6).setCellValue("说明");
        row.createCell(7).setCellValue("状态");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getRoomCreateFee(sheet, reqJson);
        return workbook;
    }

    private void getRoomCreateFee(Sheet sheet, JSONObject reqJson) throws ParseException {
        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        if (reqJson.containsKey("roomId") && !StringUtil.isEmpty(reqJson.getString("roomId"))) {
            feeDto.setPayerObjId(reqJson.getString("roomId"));
        }
        if (reqJson.containsKey("payerObjIds") && !StringUtil.isEmpty(reqJson.getString("payerObjIds"))) {
            String payerObjIds = reqJson.getString("payerObjIds");
            feeDto.setPayerObjIds(payerObjIds.split(","));
        }
        freshPayerObjIdByRoomNum(reqJson);
        feeDto.setPage(1);
        feeDto.setRow(MAX_ROW);
        List<FeeDto> feeList = feeInnerServiceSMOImpl.queryFees(feeDto);
        computeFeePrice(feeList);
        appendData(feeList, sheet, reqJson);
    }

    private void appendData(List<FeeDto> feeList, Sheet sheet, JSONObject reqJson) throws ParseException {
        Row row = null;
        //查询房屋信息，获取房屋面积
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(rooms, "查询房屋信息错误！");
        for (int index = 0; index < feeList.size(); index++) {
            row = sheet.createRow(index + 1);
            FeeDto feeDto = feeList.get(index);
            row.createCell(0).setCellValue(feeDto.getFeeName());
            row.createCell(1).setCellValue(feeDto.getFeeFlagName());
            row.createCell(2).setCellValue(feeDto.getFeeTypeCdName());
            row.createCell(3).setCellValue(feeDto.getAmountOwed());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(4).setCellValue(format.format(feeDto.getStartTime()));
            if (StringUtil.isEmpty(feeDto.getState()) || feeDto.getState().equals("2009001")) {
                row.createCell(5).setCellValue("--");
            } else {
                BigDecimal amountOwed = new BigDecimal(feeDto.getAmountOwed());
                String endTime = DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_B);
                String deadlineTime = DateUtil.getFormatTimeString(feeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_B);
                if (BigDecimal.ZERO.compareTo(amountOwed) == 0 && endTime.equals(deadlineTime)) {
                    row.createCell(5).setCellValue(endTime + "~--");
                } else {
                    String newTime = dateSubOneDay(endTime, deadlineTime, feeDto.getFeeFlag());
                    row.createCell(5).setCellValue(endTime + "~" + newTime);
                }
            }
            if (!StringUtil.isEmpty(feeDto.getComputingFormula()) && feeDto.getComputingFormula().equals("5005") && feeDto.getComputingFormula().equals("9009")) {
                BigDecimal mwPrice = new BigDecimal(feeDto.getMwPrice());
                if (mwPrice.compareTo(BigDecimal.ZERO) > 0) {
                    row.createCell(6).setCellValue("上期度数：" + feeDto.getPreDegrees() + " 本期度数：" + feeDto.getCurDegrees()
                            + " 单价：" + feeDto.getMwPrice() + " 附加费：" + feeDto.getAdditionalAmount());
                } else {
                    row.createCell(6).setCellValue("上期度数：" + feeDto.getPreDegrees() + " 本期度数：" + feeDto.getCurDegrees()
                            + " 单价：" + feeDto.getSquarePrice() + " 附加费：" + feeDto.getAdditionalAmount());
                }
            } else if (!StringUtil.isEmpty(feeDto.getComputingFormula()) && feeDto.getComputingFormula().equals("6006")) {
                String attrValue = getAttrValue(feeDto, "390006");
                row.createCell(6).setCellValue("用量：" + attrValue + " 单价：" + feeDto.getSquarePrice()
                        + " 附加费：" + feeDto.getAdditionalAmount());
            } else if (!StringUtil.isEmpty(feeDto.getFeeTypeCd()) && feeDto.getFeeTypeCd().equals("888800010017")) {
                String attrValue = getAttrValue(feeDto, "390005");
                String attrValue1 = getAttrValue(feeDto, "390003");
                row.createCell(6).setCellValue("算法：" + attrValue + " 用量：" + attrValue1);
            } else if (!StringUtil.isEmpty(feeDto.getComputingFormula()) && feeDto.getComputingFormula().equals("4004")) {
                row.createCell(6).setCellValue("费用根据实际情况而定");
            } else if (!StringUtil.isEmpty(feeDto.getFeeFlag()) && feeDto.getFeeFlag().equals("1003006")) {
                row.createCell(6).setCellValue("面积：" + rooms.get(0).getBuiltUpArea() + " 单价：" + feeDto.getSquarePrice()
                        + " 附加费：" + feeDto.getAdditionalAmount());
            } else {
                row.createCell(6).setCellValue("面积：" + rooms.get(0).getBuiltUpArea() + " 单价：" + feeDto.getSquarePrice()
                        + " 固定费：" + feeDto.getAdditionalAmount());
            }
            row.createCell(7).setCellValue(feeDto.getStateName());
        }
    }

    /**
     * 处理时间
     *
     * @return
     */
    private String dateSubOneDay(String startTime, String endTime, String feeFlag) throws ParseException {
        if (StringUtil.isEmpty(endTime)) {
            return endTime;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cl1 = Calendar.getInstance();
        cl1.setTime(format.parse(startTime));
        //如果开始时间是31日 结束时间是30日 不做处理
        int _startTimeLastDay = cl1.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar cl2 = Calendar.getInstance();
        cl2.setTime(format.parse(endTime));
        int _endTimeLastDay = cl2.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (_startTimeLastDay == 31 && _endTimeLastDay == 30) {
            return endTime;
        }
        //2月份特殊处理
        int _endTimeMonth = cl2.get(Calendar.MONTH) + 1;
        if (_endTimeMonth == 1 && _endTimeLastDay > 26 && _startTimeLastDay > 26) {
            return endTime;
        }
        if (feeFlag != "2006012") {
            int date = cl2.get(Calendar.DATE) - 1;
            int year = cl2.get(Calendar.YEAR);
            int month = cl2.get(Calendar.MONTH) + 1;
            String newMonth = "";
            String newDate = "";
            if (date < 10) {
                newDate = "0" + date;
            } else {
                newDate = "" + date;
            }
            if (month < 10) {
                newMonth = "0" + month;
            } else {
                newMonth = "" + month;
            }
            endTime = year + "-" + newMonth + "-" + newDate;
        }
        return endTime;
    }

    /**
     * 计算费用
     *
     * @param feeDtos
     */
    private void computeFeePrice(List<FeeDto> feeDtos) {
        if (feeDtos == null || feeDtos.isEmpty()) {
            return;
        }
        String val = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(), TOTAL_FEE_PRICE);
        if (StringUtil.isEmpty(val)) {
            val = MappingCache.getValue(DOMAIN_COMMON, TOTAL_FEE_PRICE);
        }
        //先取单小区的如果没有配置 取 全局的
        String received_amount_switch = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(), RECEIVED_AMOUNT_SWITCH);
        if (StringUtil.isEmpty(received_amount_switch)) {
            received_amount_switch = MappingCache.getValue(DOMAIN_COMMON, RECEIVED_AMOUNT_SWITCH);
        }
        //先取单小区的如果没有配置 取 全局的
        String offlinePayFeeSwitch = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(), OFFLINE_PAY_FEE_SWITCH);
        if (StringUtil.isEmpty(offlinePayFeeSwitch)) {
            offlinePayFeeSwitch = MappingCache.getValue(DOMAIN_COMMON, OFFLINE_PAY_FEE_SWITCH);
        }
        for (FeeDto feeDto : feeDtos) {
            try {
                // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
                Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto);
                Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
                double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
                feeDto.setCycle(feeDto.getPaymentCycle());
                //todo 这里考虑 账单模式的场景
                if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
                    feeDto.setCycle(oweMonth + "");
                }
                feeDto.setDeadlineTime(targetEndDate);
                //todo 算费
                doComputeFeePrice(feeDto, oweMonth);
                feeDto.setVal(val);
                //关闭 线下收银功能
                if (StringUtil.isEmpty(received_amount_switch)) {
                    feeDto.setReceivedAmountSwitch("1");//默认启用实收款输入框
                } else {
                    feeDto.setReceivedAmountSwitch(received_amount_switch);
                }
                feeDto.setOfflinePayFeeSwitch(offlinePayFeeSwitch);
            } catch (Exception e) {
                logger.error("查询费用信息 ，费用信息错误", e);
            }
            //去掉多余0
            feeDto.setSquarePrice(Double.parseDouble(feeDto.getSquarePrice()) + "");
            feeDto.setAdditionalAmount(Double.parseDouble(feeDto.getAdditionalAmount()) + "");
        }
    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void doComputeFeePrice(FeeDto feeDto, double oweMonth) {
        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        //BigDecimal feeTotalPrice = new BigDecimal(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()));
        feeDto.setFeeTotalPrice(MoneyUtil.computePriceScale(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()),
                feeDto.getScale(),
                Integer.parseInt(feeDto.getDecimalPlace())));
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(MoneyUtil.computePriceScale(curFeePrice.doubleValue(), feeDto.getScale(), Integer.parseInt(feeDto.getDecimalPlace())) + "");
        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())
                && feeDto.getDeadlineTime() == null) {
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
        //考虑租金递增
        computeFeeSMOImpl.dealRentRate(feeDto);
    }

    /**
     * 根据房屋名称 刷入 payerObjId
     *
     * @param reqJson
     */
    private void freshPayerObjIdByRoomNum(JSONObject reqJson) {
        if (!reqJson.containsKey("roomNum") || StringUtil.isEmpty(reqJson.getString("roomNum"))) {
            return;
        }
        String[] roomNums = reqJson.getString("roomNum").split("-", 3);
        if (roomNums == null || roomNums.length != 3) {
            throw new IllegalArgumentException("房屋编号格式错误！");
        }
        String floorNum = roomNums[0];
        String unitNum = roomNums[1];
        String roomNum = roomNums[2];
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorNum(floorNum);
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);
        if (floorDtos == null || floorDtos.size() < 1) {
            return;
        }
        for (FloorDto floor : floorDtos) {
            UnitDto unitDto = new UnitDto();
            unitDto.setFloorId(floor.getFloorId());
            unitDto.setUnitNum(unitNum);
            List<UnitDto> unitDtos = unitInnerServiceSMOImpl.queryUnits(unitDto);
            if (unitDtos == null || unitDtos.size() < 1) {
                continue;
            }
            for (UnitDto unit : unitDtos) {
                RoomDto roomDto = new RoomDto();
                roomDto.setUnitId(unit.getUnitId());
                roomDto.setRoomNum(roomNum);
                roomDto.setCommunityId(reqJson.getString("communityId"));
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                Assert.listOnlyOne(roomDtos, "查询不到房屋！");
                reqJson.put("payerObjId", roomDtos.get(0).getRoomId());
            }
        }
    }

    /**
     * 获取费用属性的值
     *
     * @param feeDto
     * @param value
     * @return
     */
    private String getAttrValue(FeeDto feeDto, String value) {
        List<FeeAttrDto> feeAttrs = feeDto.getFeeAttrDtos();
        String attrValue = "";
        for (FeeAttrDto feeAttrDto : feeAttrs) {
            if (feeAttrDto.getSpecCd().equals(value)) {
                attrValue = feeAttrDto.getValue();
            }
        }
        return attrValue;
    }
}
