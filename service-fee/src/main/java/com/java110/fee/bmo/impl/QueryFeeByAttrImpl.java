package com.java110.fee.bmo.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.fee.bmo.IQueryFeeByAttr;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.fee.ApiFeeDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryFeeByAttrImpl implements IQueryFeeByAttr {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> query(FeeAttrDto feeAttrDto) {

        int count = feeInnerServiceSMOImpl.queryFeeByAttrCount(feeAttrDto);

        List<ApiFeeDataVo> fees = null;

        if (count > 0) {
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFeeByAttr(feeAttrDto);
            computeFeePrice(feeDtos);
            fees = BeanConvertUtil.covertBeanList(feeDtos, ApiFeeDataVo.class);
        } else {
            fees = new ArrayList<>();
        }

        return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) feeAttrDto.getRow()), count, fees);
    }


    private void computeFeePrice(List<FeeDto> feeDtos) {

        for (FeeDto feeDto : feeDtos) {
            if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
                computeFeePriceByRoom(feeDto);
            } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
                computeFeePriceByParkingSpace(feeDto);
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
        }else if ("3003".equals(computingFormula)) { // 固定费用
            feePrice = 0;
        } else if ("1101".equals(computingFormula)) { // 租金
            feePrice = 0;
        } else if ("1102".equals(computingFormula)) { // 租金
            feePrice = 0;
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
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
        } else if ("3003".equals(computingFormula)) { // 固定费用
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("1101".equals(computingFormula)) { // 租金
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
            feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("1102".equals(computingFormula)) { // 租金
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
            feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);
    }

}
