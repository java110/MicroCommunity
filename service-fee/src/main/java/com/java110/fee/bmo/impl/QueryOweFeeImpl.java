package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.BillDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.fee.bmo.IQueryOweFee;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class QueryOweFeeImpl implements IQueryOweFee {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> query(FeeDto feeDto) {

        //查询费用信息arrearsEndTime
        feeDto.setArrearsEndTime(DateUtil.getCurrentDate());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            feeDtos = new ArrayList<>();
            return ResultVo.createResponseEntity(feeDtos);
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        for (FeeDto tmpFeeDto : feeDtos) {
            computeOweFee(tmpFeeDto);//计算欠费金额

            //如果金额为0 就排除
            if (tmpFeeDto.getFeePrice() > 0) {
                tmpFeeDtos.add(tmpFeeDto);
            }
        }


        return ResultVo.createResponseEntity(tmpFeeDtos);
    }

    @Override
    public ResponseEntity<String> queryAllOwneFee(FeeDto feeDto) {
        ResponseEntity<String> responseEntity = null;

        if (!freshFeeDtoParam(feeDto)) {
            return ResultVo.createResponseEntity(1, 0, new JSONArray());
        }

        if (FeeConfigDto.BILL_TYPE_EVERY.equals(feeDto.getBillType())) {
            responseEntity = computeEveryOweFee(feeDto);
        } else {
            responseEntity = computeBillOweFee(feeDto);
        }
        return responseEntity;
    }

    private boolean freshFeeDtoParam(FeeDto feeDto) {

        if (StringUtil.isEmpty(feeDto.getPayerObjId())) {
            return true;
        }

        if (!feeDto.getPayerObjId().contains("#")) {
            return false;
        }
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
            String[] nums = feeDto.getPayerObjId().split("#");
            if (nums.length != 3) {
                return false;
            }
            RoomDto roomDto = new RoomDto();
            roomDto.setFloorId(nums[0]);
            roomDto.setUnitNum(nums[1]);
            roomDto.setRoomNum(nums[2]);
            roomDto.setCommunityId(feeDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

            if (roomDtos == null || roomDtos.size() < 1) {
                return false;
            }
            feeDto.setPayerObjId(roomDtos.get(0).getRoomId());

        } else {
            String[] nums = feeDto.getPayerObjId().split("#");
            if (nums.length != 2) {
                return false;
            }
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(nums[0]);
            parkingSpaceDto.setNum(nums[1]);
            parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
                return false;
            }
            feeDto.setPayerObjId(parkingSpaceDtos.get(0).getPsId());
        }

        return true;
    }

    /**
     * 账单费用
     *
     * @param feeDto
     * @return
     */
    private ResponseEntity<String> computeBillOweFee(FeeDto feeDto) {
        int count = feeInnerServiceSMOImpl.computeBillOweFeeCount(feeDto);
        List<FeeDto> feeDtos = null;
        if (count > 0) {
            feeDtos = feeInnerServiceSMOImpl.computeBillOweFee(feeDto);
        } else {
            feeDtos = new ArrayList<>();
        }
        return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) feeDto.getRow()), count, feeDtos);
    }

    /**
     * 实时费用
     *
     * @param feeDto
     * @return
     */
    private ResponseEntity<String> computeEveryOweFee(FeeDto feeDto) {

        int count = feeInnerServiceSMOImpl.computeEveryOweFeeCount(feeDto);
        List<FeeDto> feeDtos = null;
        if (count > 0) {
            feeDtos = feeInnerServiceSMOImpl.computeEveryOweFee(feeDto);
            computeFeePrices(feeDtos);

        } else {
            feeDtos = new ArrayList<>();
        }
        return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) feeDto.getRow()), count, feeDtos);
    }

    private void computeFeePrices(List<FeeDto> feeDtos) {

        List<FeeDto> roomFees = new ArrayList<>();
        List<FeeDto> psFees = new ArrayList<>();
        List<String> roomIds = new ArrayList<>();
        List<String> psIds = new ArrayList<>();

        for (FeeDto fee : feeDtos) {
            if ("3333".equals(fee.getPayerObjType())) { //房屋相关
                roomFees.add(fee);
                roomIds.add(fee.getPayerObjId());
            } else if ("6666".equals(fee.getPayerObjType())) {//车位相关
                psFees.add(fee);
                psIds.add(fee.getPayerObjId());
            }
        }

        if (roomFees.size() > 0) {
            computeRoomFee(roomFees, roomIds);
        }

        if (psFees.size() > 0) {
            computePsFee(psFees, psIds);
        }
    }

    /**
     * 计算停车费
     *
     * @param psFees
     */
    private void computePsFee(List<FeeDto> psFees, List<String> psIds) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(psFees.get(0).getCommunityId());
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
            return;
        }
        List<String> ownerIds = new ArrayList<>();
        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            for (FeeDto feeDto : psFees) {
                dealFeePs(tmpParkingSpaceDto, feeDto);
            }
            ownerIds.add(tmpParkingSpaceDto.getOwnerId());
        }

        if (ownerIds.size() < 1) {
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        ownerDto.setCommunityId(psFees.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        for (OwnerDto tmpOwnerDto : ownerDtos) {
            for (FeeDto feeDto : psFees) {
                dealFeeOwner(tmpOwnerDto, feeDto);
            }
        }
    }

    private void dealFeePs(ParkingSpaceDto tmpParkingSpaceDto, FeeDto feeDto) {
        if (!tmpParkingSpaceDto.getPsId().equals(feeDto.getPayerObjId())) {
            return;
        }
        feeDto.setRoomName(tmpParkingSpaceDto.getAreaNum() + "停车场" + tmpParkingSpaceDto.getNum() + "车位");

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(tmpParkingSpaceDto.getArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用

            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = 0.00;
        }

        feeDto.setFeePrice(feePrice);
        double month = dayCompare(feeDto.getEndTime(), DateUtil.getCurrentDate());
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(month));
        feeDto.setAmountOwed(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() + "");
    }

    /**
     * 计算房屋费
     *
     * @param roomFees
     */
    private void computeRoomFee(List<FeeDto> roomFees, List<String> roomIds) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(roomFees.get(0).getCommunityId());
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) { //数据有问题
            return;
        }

        for (RoomDto tmpRoomDto : roomDtos) {
            for (FeeDto feeDto : roomFees) {
                dealFeeRoom(tmpRoomDto, feeDto);
            }
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        ownerDto.setCommunityId(roomFees.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        for (OwnerDto tmpOwnerDto : ownerDtos) {
            for (FeeDto feeDto : roomFees) {
                dealFeeOwner(tmpOwnerDto, feeDto);
            }
        }

    }

    private void dealFeeOwner(OwnerDto tmpOwnerDto, FeeDto feeDto) {

        if (!tmpOwnerDto.getRoomId().equals(feeDto.getPayerObjId())) {
            return;
        }

        feeDto.setOwnerName(tmpOwnerDto.getName());
        feeDto.setOwnerTel(tmpOwnerDto.getLink());
    }

    private void dealFeeRoom(RoomDto tmpRoomDto, FeeDto feeDto) {

        if (!tmpRoomDto.getRoomId().equals(feeDto.getPayerObjId())) {
            return;
        }
        feeDto.setRoomName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(tmpRoomDto.getBuiltUpArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = 0.00;
        }
        feeDto.setFeePrice(feePrice);

        double month = dayCompare(feeDto.getEndTime(), DateUtil.getCurrentDate());
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(month));
        feeDto.setAmountOwed(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue() + "");

    }

    /**
     * 计算欠费金额
     *
     * @param tmpFeeDto
     */
    private void computeOweFee(FeeDto tmpFeeDto) {
        String billType = tmpFeeDto.getBillType();

        if (FeeConfigDto.BILL_TYPE_EVERY.equals(billType)) {
            computeFeePrice(tmpFeeDto);
            return;
        }
        BillDto billDto = new BillDto();
        billDto.setCommunityId(tmpFeeDto.getCommunityId());
        billDto.setConfigId(tmpFeeDto.getConfigId());
        billDto.setCurBill("T");
        List<BillDto> billDtos = feeInnerServiceSMOImpl.queryBills(billDto);
        if (billDtos == null || billDtos.size() < 1) {
            tmpFeeDto.setFeePrice(0.00);
            return;
        }
        BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
        billOweFeeDto.setCommunityId(tmpFeeDto.getCommunityId());
        billOweFeeDto.setFeeId(tmpFeeDto.getFeeId());
        billOweFeeDto.setState(BillOweFeeDto.STATE_WILL_FEE);
        billOweFeeDto.setBillId(billDtos.get(0).getBillId());
        List<BillOweFeeDto> billOweFeeDtos = feeInnerServiceSMOImpl.queryBillOweFees(billOweFeeDto);
        if (billOweFeeDtos == null || billOweFeeDtos.size() < 1) {
            tmpFeeDto.setFeePrice(0.00);
            return;
        }
        tmpFeeDto.setFeePrice(Double.parseDouble(billOweFeeDtos.get(0).getAmountOwed()));
    }


    private void computeFeePrice(FeeDto feeDto) {

        if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
            computeFeePriceByRoom(feeDto);
        } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
            computeFeePriceByParkingSpace(feeDto);
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
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = 0.00;
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
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = 0.00;
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
        long days = (t2 - t1) / (24 * 60 * 60 * 1000);

        BigDecimal tmpDays = new BigDecimal(days);
        BigDecimal monthDay = new BigDecimal(30);

        return tmpDays.divide(monthDay, 2, RoundingMode.HALF_UP).doubleValue();
    }

}
