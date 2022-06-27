package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.listOwnerOweFee")
public class ListOwnerOweFeeCmd extends Cmd {
    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "ownerId", "未包含小区业主ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //1.0 查询业主房屋 查询业主是否有房屋欠费
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(reqJson.getString("ownerId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        List<FeeDto> resultFees = new ArrayList<>();

        if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
            getRoomOweFee(ownerRoomRelDtos, reqJson, resultFees);
        }

        //2.0 查询业主是否有 车位欠费

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setOwnerId(reqJson.getString("ownerId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos != null && ownerCarDtos.size() > 0) {
            getParkingSpaceOweFee(ownerCarDtos, reqJson, resultFees);
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(1, resultFees.size(), resultFees);
        context.setResponseEntity(responseEntity);
    }

    private void getParkingSpaceOweFee(List<OwnerCarDto> ownerCarDtos, JSONObject reqJson, List<FeeDto> resultFees) {
        String payObjName = "";
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
            parkingSpaceDto.setPsId(ownerCarDto.getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                continue;
            }
            ParkingSpaceDto tmpParkingSpaceDto = parkingSpaceDtos.get(0);
            payObjName = tmpParkingSpaceDto.getAreaNum() + "停车场" + tmpParkingSpaceDto.getNum() + "车位";
            FeeDto feeDto = new FeeDto();
            feeDto.setCommunityId(reqJson.getString("communityId"));
            feeDto.setPayerObjId(ownerCarDto.getPsId());
            feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            getOweFee(feeDtos, reqJson, resultFees, payObjName);
        }

    }

    private void getRoomOweFee(List<OwnerRoomRelDto> ownerRoomRelDtos, JSONObject reqJson, List<FeeDto> resultFees) {
        String payObjName = "";
        for (OwnerRoomRelDto ownerRoomRelDto : ownerRoomRelDtos) {
            RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setRoomId(ownerRoomRelDto.getRoomId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() < 1) {
                continue;
            }
            RoomDto tmpRoomDto = roomDtos.get(0);
            payObjName = tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室";
            FeeDto feeDto = new FeeDto();
            feeDto.setCommunityId(reqJson.getString("communityId"));
            feeDto.setPayerObjId(ownerRoomRelDto.getRoomId());
            feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            getOweFee(feeDtos, reqJson, resultFees, payObjName);
        }

    }

    private void getOweFee(List<FeeDto> feeDtos, JSONObject reqJson, List<FeeDto> resultFees, String payObjName) {
        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (FeeDto feeDto : feeDtos) {
            Date endTime = feeDto.getEndTime();
            if (endTime.getTime() > DateUtil.getCurrentDate().getTime()) {//没有欠费
                continue;
            }

            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setCommunityId(reqJson.getString("communityId"));
            feeConfigDto.setConfigId(feeDto.getConfigId());
            List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
            if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
                continue;
            }

            FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);

            if (FeeConfigDto.BILL_TYPE_EVERY.equals(tmpFeeConfigDto.getBillType())) {//直接计算 欠费金额
                computeFeePriceByRoom(feeDto);
                double month = dayCompare(endTime, DateUtil.getCurrentDate());
                BigDecimal feePrice = new BigDecimal(feeDto.getFeePrice());
                feePrice = feePrice.multiply(new BigDecimal(month));
                FeeDto tmpFeeDto = new FeeDto();
                tmpFeeDto.setFeeId(feeDto.getFeeId());
                tmpFeeDto.setFeeName(feeDto.getFeeName());
                tmpFeeDto.setOweFee(feePrice.doubleValue());
                tmpFeeDto.setPayerObjType(feeDto.getPayerObjType());
                tmpFeeDto.setPayerObjName(payObjName);
                tmpFeeDto.setPayerObjId(feeDto.getPayerObjId());
                resultFees.add(tmpFeeDto);
                continue;
            }
            //查询档期欠费账单表 是否有欠费
            BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
            billOweFeeDto.setCommunityId(reqJson.getString("communityId"));
            billOweFeeDto.setFeeId(feeDto.getFeeId());
            List<BillOweFeeDto> billOweFeeDtos = feeInnerServiceSMOImpl.queryBillOweFees(billOweFeeDto);

            if (billOweFeeDtos == null || billOweFeeDtos.size() < 1) { //没有欠费

                continue;
            }

            for (BillOweFeeDto tmpBillOweFeeDto : billOweFeeDtos) {
                FeeDto tmpFeeDto = new FeeDto();
                tmpFeeDto.setFeeId(feeDto.getFeeId());
                tmpFeeDto.setFeeName(feeDto.getFeeName());
                tmpFeeDto.setOweFee(Double.parseDouble(tmpBillOweFeeDto.getBillAmountOwed()));
                tmpFeeDto.setPayerObjName(payObjName);
                tmpFeeDto.setPayerObjId(feeDto.getPayerObjId());
                tmpFeeDto.setPayerObjType(feeDto.getPayerObjType());
                resultFees.add(tmpFeeDto);
            }


        }

    }


    private void computeFeePriceByParkingSpace(FeeDto feeDto) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
        parkingSpaceDto.setPsId(feeDto.getPayerObjId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
            return;
        }

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用

            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);


    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(feeDto.getCommunityId());
        roomDto.setRoomId(feeDto.getPayerObjId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) { //数据有问题
            return;
        }

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDtos.get(0).getBuiltUpArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);
    }


    /**
     * 计算2个日期之间相差的  以年、月、日为单位，各自计算结果是多少
     * 比如：2011-02-02 到  2017-03-02
     * 以年为单位相差为：6年
     * 以月为单位相差为：73个月
     * 以日为单位相差为：2220天
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static double dayCompare(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);

        long t1 = from.getTimeInMillis();
        long t2 = to.getTimeInMillis();
        double days = (t2 - t1)*1.00/ (24 * 60 * 60 * 1000);

        BigDecimal tmpDays = new BigDecimal(days);
        BigDecimal monthDay = new BigDecimal(30);

        return tmpDays.divide(monthDay, 2, RoundingMode.HALF_UP).doubleValue();
    }
}
