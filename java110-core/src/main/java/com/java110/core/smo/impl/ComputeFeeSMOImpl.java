package com.java110.core.smo.impl;

import com.java110.config.properties.code.Java110Properties;
import com.java110.core.context.Environment;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractRoom.ContractRoomDto;
import com.java110.dto.fee.*;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 费用计算 服务类
 * <p>
 * add by wuxw 2020-09-23
 *
 * @openSource https://gitee.com/wuxw7/MicroCommunity.git
 */

@Service
public class ComputeFeeSMOImpl implements IComputeFeeSMO {

    protected static final Logger logger = LoggerFactory.getLogger(ComputeFeeSMOImpl.class);

    @Autowired(required = false)
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired(required = false)
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired(required = false)
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired(required = false)
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired(required = false)
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired(required = false)
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired(required = false)
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    @Autowired(required = false)
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired(required = false)
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired(required = false)
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;

    @Autowired
    private Java110Properties java110Properties;

    @Override
    public Date getFeeEndTime() {
        return null;
    }

    /**
     * 计算实时欠费金额
     *
     * @param tmpFeeDto
     */
    public void computeEveryOweFee(FeeDto tmpFeeDto) {
        computeEveryOweFee(tmpFeeDto, null);
    }

    @Override
    public void computeEveryOweFee(FeeDto tmpFeeDto, RoomDto roomDto) {
        computeFeePrice(tmpFeeDto, roomDto);
    }


    /**
     * 计算欠费金额
     *
     * @param tmpFeeDto
     */
    public void computeOweFee(FeeDto tmpFeeDto) {
        String billType = tmpFeeDto.getBillType();

        if (FeeConfigDto.BILL_TYPE_EVERY.equals(billType)) {
            computeFeePrice(tmpFeeDto, null);
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
        try {
            tmpFeeDto.setDeadlineTime(DateUtil.getDateFromString(billOweFeeDtos.get(0).getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
        } catch (ParseException e) {
            logger.error("获取结束时间失败", e);
        }
        tmpFeeDto.setFeePrice(Double.parseDouble(billOweFeeDtos.get(0).getAmountOwed()));
    }

    private void computeFeePrice(FeeDto feeDto, RoomDto roomDto) {

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            computeFeePriceByRoom(feeDto, roomDto);
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
            computeFeePriceByParkingSpace(feeDto);
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) { //房屋相关
            computeFeePriceByContract(feeDto, roomDto);
        }
    }

    private void computeFeePriceByParkingSpace(FeeDto feeDto) {
        Map<String, Object> targetEndDateAndOweMonth = getTargetEndDateAndOweMonth(feeDto);
        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(feeDto.getCommunityId());
        ownerCarDto.setCarId(feeDto.getPayerObjId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) { //数据有问题
            return;
        }

        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = getFeePrice(feeDto);

        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(oweMonth));
        feeDto.setFeePrice(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        feeDto.setDeadlineTime(targetEndDate);

        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            //feeDto.setDeadlineTime(DateUtil.getCurrentDate()); 欠费日期不对先注释
        }

    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto, RoomDto roomDto) {
        Map<String, Object> targetEndDateAndOweMonth = getTargetEndDateAndOweMonth(feeDto);
        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");

        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = getFeePrice(feeDto, roomDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        //double month = dayCompare(feeDto.getEndTime(), DateUtil.getCurrentDate());
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(oweMonth));
        feeDto.setFeePrice(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        feeDto.setDeadlineTime(targetEndDate);

        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            //feeDto.setDeadlineTime(DateUtil.getCurrentDate()); 欠费日期不对先注释
        }
    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByContract(FeeDto feeDto, RoomDto roomDto) {
        Map<String, Object> targetEndDateAndOweMonth = getTargetEndDateAndOweMonth(feeDto);
        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");

        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = getFeePrice(feeDto, roomDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        //double month = dayCompare(feeDto.getEndTime(), DateUtil.getCurrentDate());
        BigDecimal price = new BigDecimal(feeDto.getFeePrice());
        price = price.multiply(new BigDecimal(oweMonth));
        feeDto.setFeePrice(price.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        feeDto.setDeadlineTime(targetEndDate);

        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            //feeDto.setDeadlineTime(DateUtil.getCurrentDate()); 欠费日期不对先注释
        }
    }


    /**
     * 刷新 收据明细
     *
     * @param feeDto
     * @param feeReceiptDetailPo
     */
    @Override
    public void freshFeeReceiptDetail(FeeDto feeDto, FeeReceiptDetailPo feeReceiptDetailPo) {
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            String computingFormula = feeDto.getComputingFormula();
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDto.getPayerObjId());
            roomDto.setCommunityId(feeDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() != 1) {
                return;
            }
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                feeReceiptDetailPo.setArea(roomDtos.get(0).getBuiltUpArea());
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("2002".equals(computingFormula)) { // 固定费用
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice(feeDto.getAdditionalAmount());
            } else if ("3003".equals(computingFormula)) { // 固定费用
                feeReceiptDetailPo.setArea(roomDtos.get(0).getRoomArea());
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("1101".equals(computingFormula)) { // 租金
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice(roomDto.getRoomRent());
            } else if ("4004".equals(computingFormula)) {
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else if ("6006".equals(computingFormula)) {
                String value = "";
                List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();
                for (FeeAttrDto feeAttrDto : feeAttrDtos) {
                    if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION)) {
                        value = feeAttrDto.getValue();
                    }
                }
                feeReceiptDetailPo.setArea(value);
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feeReceiptDetailPo.setArea(roomDtos.get(0).getBuiltUpArea());
                feeReceiptDetailPo.setSquarePrice(feeDto.getComputingFormulaText());
            } else if ("9009".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getMwPrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else if ("1101".equals(computingFormula)) { //租金
                feeReceiptDetailPo.setArea(roomDtos.get(0).getBuiltUpArea());
                feeReceiptDetailPo.setSquarePrice(roomDtos.get(0).getRoomRent());
            } else {
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    return;
                }
                feeReceiptDetailPo.setArea(parkingSpaceDtos.get(0).getArea());
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("2002".equals(computingFormula)) { // 固定费用
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice(feeDto.getAdditionalAmount());
            } else if ("3003".equals(computingFormula)) { // 固定费用
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice("0");
            } else if ("1101".equals(computingFormula)) { // 租金
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice("0");
            } else if ("4004".equals(computingFormula)) {
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else if ("6006".equals(computingFormula)) {
                String value = "";
                List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();
                for (FeeAttrDto feeAttrDto : feeAttrDtos) {
                    if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION)) {
                        value = feeAttrDto.getValue();
                    }
                }
                feeReceiptDetailPo.setArea(value);
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("7007".equals(computingFormula)) { //自定义公式
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    return;
                }
                feeReceiptDetailPo.setArea(parkingSpaceDtos.get(0).getArea());
                feeReceiptDetailPo.setSquarePrice(feeDto.getComputingFormulaText());
            } else if ("9009".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getMwPrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else {

            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();
            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(feeDto.getPayerObjId());
            contractRoomDto.setCommunityId(feeDto.getCommunityId());
            List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);
            if (contractRoomDtos == null || contractRoomDtos.size() == 0) {
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
                return;
            }
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getBuiltUpArea())));
                }
                feeReceiptDetailPo.setArea(builtUpArea.doubleValue() + "");
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("2002".equals(computingFormula)) { // 固定费用
                feeReceiptDetailPo.setArea("");
                feeReceiptDetailPo.setSquarePrice(feeDto.getAdditionalAmount());
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getRoomArea())));
                }
                feeReceiptDetailPo.setArea(builtUpArea.doubleValue() + "");
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getRoomRent())));
                }
                feeReceiptDetailPo.setArea(builtUpArea.doubleValue() + "");
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("4004".equals(computingFormula)) {
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else if ("6006".equals(computingFormula)) {
                String value = "";
                List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();
                for (FeeAttrDto feeAttrDto : feeAttrDtos) {
                    if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION)) {
                        value = feeAttrDto.getValue();
                    }
                }
                feeReceiptDetailPo.setArea(value);
                feeReceiptDetailPo.setSquarePrice(feeDto.getSquarePrice() + "/" + feeDto.getAdditionalAmount());
            } else if ("7007".equals(computingFormula)) { //自定义公式
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getBuiltUpArea())));
                }
                feeReceiptDetailPo.setArea(builtUpArea.doubleValue() + "");
                feeReceiptDetailPo.setSquarePrice(feeDto.getComputingFormulaText());
            } else if ("9009".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal sub = curDegree.subtract(preDegree).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    feeReceiptDetailPo.setArea(sub.doubleValue() + "");
                    feeReceiptDetailPo.setSquarePrice(feeDto.getMwPrice() + "/" + feeDto.getAdditionalAmount());
                }
            } else {
            }
        }
    }

    /**
     * 查询费用对象名称
     *
     * @param feeDto
     * @return
     */
    @Override
    public String getFeeObjName(FeeDto feeDto) {
        String objName = "";
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDto.getPayerObjId());
            roomDto.setCommunityId(feeDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() != 1) {
                return objName;
            }
            roomDto = roomDtos.get(0);
            if (RoomDto.ROOM_TYPE_ROOM.equals(roomDto.getRoomType())) {
                objName = roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum();
            } else {
                objName = roomDto.getFloorNum() + "-" + roomDto.getRoomNum();
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关

            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
                return objName;
            }

            objName = ownerCarDtos.get(0).getCarNum();
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
            parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                return objName;
            }
            objName = objName + "-" + parkingSpaceDtos.get(0).getAreaNum() + "停车场" + "-" + parkingSpaceDtos.get(0).getNum() + "车位";
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) {
            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(feeDto.getPayerObjId());
            contractDto.setCommunityId(feeDto.getCommunityId());
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            if (contractDtos == null || contractDtos.size() < 1) { //数据有问题
                return objName;
            }
            objName = contractDtos.get(0).getContractCode();

        }
        return objName;
    }

    @Override
    public OwnerDto getFeeOwnerDto(FeeDto feeDto) {
        OwnerDto ownerDto = getOwnerDtoByFeeAttr(feeDto);
        if (ownerDto != null) {
            return ownerDto;
        }

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            ownerDto = new OwnerDto();
            ownerDto.setRoomId(feeDto.getPayerObjId());
            ownerDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            return ownerDtos.get(0);
        }

        if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

            Assert.listOnlyOne(ownerCarDtos, "车辆不存在");
            ownerDto = new OwnerDto();
            ownerDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());
            ownerDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            return ownerDtos.get(0);
        }

        if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) {
            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(feeDto.getPayerObjId());
            contractDto.setCommunityId(feeDto.getCommunityId());
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

            Assert.listOnlyOne(contractDtos, "合同不存在");
            ownerDto = new OwnerDto();
            ownerDto.setOwnerId(contractDtos.get(0).getObjId());
            ownerDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            return ownerDtos.get(0);
        }
        return null;
    }

    private OwnerDto getOwnerDtoByFeeAttr(FeeDto feeDto) {
        List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();

        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return null;
        }

        OwnerDto ownerDto = new OwnerDto();
        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_OWNER_ID)) {
                ownerDto.setOwnerId(feeAttrDto.getValue());
            }

            if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_OWNER_NAME)) {
                ownerDto.setName(feeAttrDto.getValue());
            }

            if (feeAttrDto.getSpecCd().equals(FeeAttrDto.SPEC_CD_OWNER_LINK)) {
                ownerDto.setLink(feeAttrDto.getValue());
            }
        }

        if (StringUtil.isEmpty(ownerDto.getOwnerId())) {
            return null;
        }

        return ownerDto;
    }

    @Override
    public void freshFeeObjName(List<FeeDto> feeDtos) {

        List<String> roomIds = new ArrayList<>();
        List<String> carIds = new ArrayList<>();
        for (FeeDto feeDto : feeDtos) {
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
                roomIds.add(feeDto.getPayerObjId());
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
                carIds.add(feeDto.getPayerObjId());
            }
        }

        // 用房屋信息刷 费用付费对象
        freshFeeObjNameByRoomId(feeDtos, roomIds);

        // 用车辆车位 刷 付费对象
        freshFeeObjNameByCarId(feeDtos, carIds);

    }

    /**
     * 刷费用
     *
     * @param feeDtos
     * @param carIds
     */
    private void freshFeeObjNameByCarId(List<FeeDto> feeDtos, List<String> carIds) {

        if (carIds.size() < 1) {
            return;
        }


        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(feeDtos.get(0).getCommunityId());
        ownerCarDto.setCarIds(carIds.toArray(new String[carIds.size()]));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }

        List<String> psIds = new ArrayList<>();

        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            if (StringUtil.isEmpty(tmpOwnerCarDto.getPsId()) || tmpOwnerCarDto.getPsId().startsWith("-")) {
                continue;
            }
            psIds.add(tmpOwnerCarDto.getPsId());
        }

        //没有车位情况下
        if (psIds.size() < 1) {
            for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
                for (FeeDto feeDto : feeDtos) {
                    if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
                        continue;
                    }

                    if (feeDto.getPayerObjId().equals(tmpOwnerCarDto.getCarId())) {
                        feeDto.setPayerObjName(tmpOwnerCarDto.getCarNum());
                    }
                }
            }
            return;
        }


        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeDtos.get(0).getCommunityId());
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
                if (tmpParkingSpaceDto.getPsId().equals(tmpOwnerCarDto.getPsId())) {
                    tmpOwnerCarDto.setAreaNum(tmpParkingSpaceDto.getAreaNum());
                    tmpOwnerCarDto.setNum(tmpParkingSpaceDto.getNum());
                }
            }
        }
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            for (FeeDto feeDto : feeDtos) {
                if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
                    continue;
                }

                if (feeDto.getPayerObjId().equals(tmpOwnerCarDto.getCarId())) {
                    feeDto.setPayerObjName(tmpOwnerCarDto.getCarNum() + "(" + tmpOwnerCarDto.getAreaNum() + "停车场" + tmpOwnerCarDto.getNum() + "车位)");
                }
            }
        }

    }

    /**
     * 用房屋信息刷付费方名称
     *
     * @param feeDtos
     * @param roomIds
     */
    private void freshFeeObjNameByRoomId(List<FeeDto> feeDtos, List<String> roomIds) {

        if (roomIds.size() < 1) {
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(feeDtos.get(0).getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String objName = "";
        for (RoomDto tmpRoomDto : roomDtos) {
            for (FeeDto feeDto : feeDtos) {
                if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
                    continue;
                }
                if (tmpRoomDto.getRoomId().equals(feeDto.getPayerObjId())) {
                    objName = tmpRoomDto.getFloorNum() + "-" + tmpRoomDto.getUnitNum() + "-" + tmpRoomDto.getRoomNum();
                    feeDto.setPayerObjName(objName);
                }
            }
        }
    }

    /**
     * 根据周期 计算费用状态
     *
     * @param feeDto
     * @param cycles
     * @return
     */
    public String getFeeStateByCycles(FeeDto feeDto, String cycles) {
        double cycle = Double.parseDouble(cycles);
        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        endCalender.add(Calendar.MONTH, new Double(Math.floor(cycle)).intValue());
//        Calendar futureDate = Calendar.getInstance();
//        futureDate.setTime(endCalender.getTime());
//        futureDate.add(Calendar.MONTH, 1);
        int futureDay = endCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int hours = new Double((cycle - Math.floor(cycle)) * futureDay * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            return FeeDto.STATE_FINISH;
        } else {
            if ((endCalender.getTime()).after(feeDto.getConfigEndTime())) {
                return FeeDto.STATE_FINISH;
            }
        }
        return FeeDto.STATE_DOING;
    }

    public Date getFeeEndTimeByCycles(FeeDto feeDto, String cycles) {
        double cycle = Double.parseDouble(cycles);

        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        endCalender.add(Calendar.MONTH, new Double(Math.floor(cycle)).intValue());
//        Calendar futureDate = Calendar.getInstance();
//        futureDate.setTime(endCalender.getTime());
//        futureDate.add(Calendar.MONTH, 1);
        int futureDay = endCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int hours = new Double((cycle - Math.floor(cycle)) * futureDay * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            if (feeDto.getDeadlineTime() != null) {
                endCalender.setTime(feeDto.getDeadlineTime());
            } else if (!StringUtil.isEmpty(feeDto.getCurDegrees())) {
                endCalender.setTime(feeDto.getCurReadingTime());
            } else if (feeDto.getImportFeeEndTime() == null) {
                endCalender.setTime(feeDto.getConfigEndTime());
            } else {
                endCalender.setTime(feeDto.getImportFeeEndTime());
            }
        } else {
            if ((endCalender.getTime()).after(feeDto.getConfigEndTime())) {
                endCalender.setTime(feeDto.getConfigEndTime());
            }
        }

        return endCalender.getTime();
    }


    @Override
    public double getCycle() {
        return 0;
    }

    @Override
    public double getReportFeePrice(ReportFeeDto tmpReportFeeDto, ReportRoomDto reportRoomDto, ReportCarDto reportCarDto) {
        BigDecimal feePrice = new BigDecimal(0.0);
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(tmpReportFeeDto.getPayerObjType())) { //房屋相关
            String computingFormula = tmpReportFeeDto.getComputingFormula();

            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(reportRoomDto.getBuiltUpArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAmount()));
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(tmpReportFeeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAmount()));
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeRoomCustomizeFormula(BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class), BeanConvertUtil.covertBean(reportRoomDto, RoomDto.class));
            } else if ("9009".equals(computingFormula)) {
                if (StringUtil.isEmpty(tmpReportFeeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(tmpReportFeeDto.getPayerObjType())) {//车位相关
            String computingFormula = tmpReportFeeDto.getComputingFormula();

            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble("0"));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else if ("3003".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                feePrice = new BigDecimal(0);
            } else if ("1101".equals(computingFormula)) { // 租金
                feePrice = new BigDecimal(0);
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAmount()));
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(tmpReportFeeDto.getCurDegrees())) {
                    throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAmount()));
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeCarCustomizeFormula(BeanConvertUtil.covertBean(tmpReportFeeDto, FeeDto.class), BeanConvertUtil.covertBean(reportCarDto, OwnerCarDto.class));
            } else if ("9009".equals(computingFormula)) {
                if (StringUtil.isEmpty(tmpReportFeeDto.getCurDegrees())) {
                    throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(tmpReportFeeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        }
        return feePrice.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    @Override
    public Map getFeePrice(FeeDto feeDto) {
        return getFeePrice(feeDto, null);
    }

    @Override
    public Map getFeePrice(FeeDto feeDto, RoomDto roomDto) {
        BigDecimal feePrice = new BigDecimal("0.0");
        BigDecimal feeTotalPrice = new BigDecimal(0.0);
        Map<String, Object> feeAmount = new HashMap<>();
        if (Environment.isOwnerPhone(java110Properties)) {
            return getOwnerPhoneFee(feeAmount);
        }
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
            String computingFormula = feeDto.getComputingFormula();
            if (roomDto == null) {
                roomDto = new RoomDto();
                roomDto.setRoomId(feeDto.getPayerObjId());
                roomDto.setCommunityId(feeDto.getCommunityId());
                List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
                if (roomDtos == null || roomDtos.size() != 1) {
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到房屋信息，查询多条数据 roomId=" + feeDto.getPayerObjId());
                }
                roomDto = roomDtos.get(0);
            }
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getBuiltUpArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {  //动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {  //(本期度数-上期度数)*单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);

                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeRoomCustomizeFormula(feeDto, roomDto);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("8008".equals(computingFormula)) {  //手动动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
            String computingFormula = feeDto.getComputingFormula();

            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarTypeCd("1001"); //业主车辆
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "未找到车辆信息");
            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
                parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
                parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
                List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
                if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                }
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeCarCustomizeFormula(feeDto, ownerCarDtos.get(0));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) { //合同相关
            String computingFormula = feeDto.getComputingFormula();

            //查询合同关联房屋
            ContractRoomDto contractRoomDto = new ContractRoomDto();
            contractRoomDto.setContractId(feeDto.getPayerObjId());
            contractRoomDto.setCommunityId(feeDto.getCommunityId());
            List<ContractRoomDto> contractRoomDtos = contractRoomInnerServiceSMOImpl.queryContractRooms(contractRoomDto);

            if ("1001".equals(computingFormula)) { //面积*单价+附加费
                //feePrice = Double.parseDouble(feeDto.getSquarePrice()) * Double.parseDouble(roomDtos.get(0).getBuiltUpArea()) + Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(0);
                for (ContractRoomDto tmpContractRoomDto : contractRoomDtos) {
                    builtUpArea = builtUpArea.add(new BigDecimal(Double.parseDouble(tmpContractRoomDto.getBuiltUpArea())));
                }
                feeDto.setBuiltUpArea(builtUpArea.doubleValue() + "");
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("2002".equals(computingFormula)) { // 固定费用
                //feePrice = Double.parseDouble(feeDto.getAdditionalAmount());
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
//                BigDecimal roomDount = new BigDecimal(contractRoomDtos.size());
//                additionalAmount = additionalAmount.multiply(roomDount);
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("3003".equals(computingFormula)) { // 固定费用
                BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDto.getRoomArea()));
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = (squarePrice.multiply(builtUpArea).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("1101".equals(computingFormula)) { // 租金
                BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(roomDto.getRoomRent()));
                feePrice = additionalAmount.setScale(3, BigDecimal.ROUND_HALF_EVEN);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = additionalAmount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("4004".equals(computingFormula)) {  //动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("5005".equals(computingFormula)) {  //(本期度数-上期度数)*单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = (sub.multiply(squarePrice).add(additionalAmount)).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else if ("6006".equals(computingFormula)) {
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("7007".equals(computingFormula)) { //自定义公式
                feePrice = computeContractCustomizeFormula(feeDto, contractRoomDtos);
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    feeTotalPrice = feePrice.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("8008".equals(computingFormula)) {  //手动动态费用
                feePrice = new BigDecimal(Double.parseDouble(feeDto.getAmount()));
                if (!StringUtil.isEmpty(feeDto.getCycle())) {
                    BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                    BigDecimal amount = new BigDecimal(feeDto.getAmount());
                    feeTotalPrice = amount.multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                }
            } else if ("9009".equals(computingFormula)) {  //(本期度数-上期度数)*动态单价+附加费
                if (StringUtil.isEmpty(feeDto.getCurDegrees())) {
                    //throw new IllegalArgumentException("抄表数据异常");
                } else {
                    BigDecimal curDegree = new BigDecimal(Double.parseDouble(feeDto.getCurDegrees()));
                    BigDecimal preDegree = new BigDecimal(Double.parseDouble(feeDto.getPreDegrees()));
                    BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getMwPrice()));
                    BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
                    BigDecimal sub = curDegree.subtract(preDegree);
                    feePrice = sub.multiply(squarePrice)
                            .add(additionalAmount)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    if (!StringUtil.isEmpty(feeDto.getCycle())) {
                        BigDecimal cycle1 = new BigDecimal(feeDto.getCycle());
                        feeTotalPrice = sub.multiply(squarePrice).add(additionalAmount).multiply(cycle1).setScale(3, BigDecimal.ROUND_DOWN);
                    }
                }
            } else {
                throw new IllegalArgumentException("暂不支持该类公式");
            }
        }

        feePrice.setScale(3, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        feeAmount.put("feePrice", feePrice);
        feeAmount.put("feeTotalPrice", feeTotalPrice);
        return feeAmount;
    }

    /**
     * C 代表房屋对应小区面积
     * <p>
     * R 代表房屋面积
     *
     * @param feeDto
     * @param ownerCarDto
     * @return
     */
    private BigDecimal computeCarCustomizeFormula(FeeDto feeDto, OwnerCarDto ownerCarDto) {
        String value = feeDto.getComputingFormulaText();
        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) { //处理小区面积
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() < 1) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }
        } else if (value.contains("R")) { //处理 房屋面积
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
            parkingSpaceDto.setPsId(ownerCarDto.getPsId());
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
                //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到停车位信息，查询多条数据");
                value = value.replace("R", "0");
            } else {
                value = value.replace("R", parkingSpaceDtos.get(0).getArea());
            }
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            //throw new IllegalArgumentException("公式计算异常，公式为【" + feeDto.getComputingFormulaText() + "】,计算 【" + value + "】异常");
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;
    }

    /**
     * 自定义公式计算
     *
     * @param feeDto
     * @return C 代表房屋对应小区面积
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * L 代表房屋层数
     */
    private BigDecimal computeContractCustomizeFormula(FeeDto feeDto, List<ContractRoomDto> contractRoomDtos) {

        BigDecimal total = new BigDecimal(0.0);
        for (ContractRoomDto contractRoomDto : contractRoomDtos) {
            total = total.add(computeRoomCustomizeFormula(feeDto, contractRoomDto));
        }
        return total;
    }

    /**
     * 自定义公式计算
     *
     * @param feeDto
     * @param roomDto
     * @return C 代表房屋对应小区面积
     * F 代表房屋对应楼栋面积
     * U 代表房屋对应单元面积
     * R 代表房屋面积
     * X 代表房屋收费系数（房屋管理中配置）
     * L 代表房屋层数
     */
    private BigDecimal computeRoomCustomizeFormula(FeeDto feeDto, RoomDto roomDto) {

        String value = feeDto.getComputingFormulaText();
        value = value.replace("\n", "")
                .replace("\r", "")
                .trim();

        if (value.contains("C")) { //处理小区面积
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(feeDto.getCommunityId());
            List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
            if (communityDtos == null || communityDtos.size() < 1) {
                value = value.replace("C", "0");
            } else {
                value = value.replace("C", communityDtos.get(0).getCommunityArea());
            }
        } else if (value.contains("F")) { //处理楼栋
            value = value.replace("F", roomDto.getFloorArea());
        } else if (value.contains("U")) { //处理单元
            value = value.replace("U", roomDto.getUnitArea());
        } else if (value.contains("R")) { //处理 房屋面积
            value = value.replace("R", roomDto.getBuiltUpArea());
        } else if (value.contains("X")) {// 处理 房屋系数
            value = value.replace("X", roomDto.getFeeCoefficient());
        } else if (value.contains("L")) {//处理房屋层数
            value = value.replace("L", roomDto.getLayer());
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal valueObj = null;
        try {
            value = engine.eval(value).toString();
            valueObj = new BigDecimal(Double.parseDouble(value));
        } catch (Exception e) {
            //throw new IllegalArgumentException("公式计算异常，公式为【" + feeDto.getComputingFormulaText() + "】,计算 【" + value + "】异常");
            valueObj = new BigDecimal(0);
        }

        if (valueObj.doubleValue() < 0) {
            return new BigDecimal(0);
        }

        return valueObj;

    }

    /**
     * 计算 计费结束时间和 欠费月份（可能存在小数点）
     *
     * @param feeDto
     * @param ownerCarDto
     * @return
     */
    public Map getTargetEndDateAndOweMonth(FeeDto feeDto, OwnerCarDto ownerCarDto) {
        Date targetEndDate = null;
        double oweMonth = 0.0;

        Map<String, Object> targetEndDateAndOweMonth = new HashMap<>();
        //判断当前费用是否已结束
        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            targetEndDate = feeDto.getEndTime();
            targetEndDateAndOweMonth.put("oweMonth", oweMonth);
            targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
            return targetEndDateAndOweMonth;
        }
        //当前费用为一次性费用
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            //先取 deadlineTime
            if (feeDto.getDeadlineTime() != null) {
                targetEndDate = feeDto.getDeadlineTime();
            } else if (!StringUtil.isEmpty(feeDto.getCurDegrees())) {
                targetEndDate = feeDto.getCurReadingTime();
            } else if (feeDto.getImportFeeEndTime() == null) {
                targetEndDate = feeDto.getConfigEndTime();
            } else {
                targetEndDate = feeDto.getImportFeeEndTime();
            }
            //判断当前费用是不是导入费用
            oweMonth = 1.0;
        } else { //周期性费用
            //当前时间
            Date billEndTime = DateUtil.getCurrentDate();
            //建账时间
            Date startDate = feeDto.getStartTime();
            //计费起始时间
            Date endDate = feeDto.getEndTime();
            //缴费周期
            long paymentCycle = Long.parseLong(feeDto.getPaymentCycle());
            // 当前时间 - 开始时间  = 月份
            double mulMonth = 0.0;
            mulMonth = dayCompare(startDate, billEndTime);

            // 月份/ 周期 = 轮数（向上取整）
            double round = 0.0;
            if ("1200".equals(feeDto.getPaymentCd())) { // 1200预付费
                round = Math.floor(mulMonth / paymentCycle) + 1;
            } else { //2100后付费
                round = Math.floor(mulMonth / paymentCycle);
            }
            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            targetEndDate = getTargetEndTime(round * paymentCycle, startDate);//目标结束时间
            //费用项的结束时间<缴费的结束时间  费用快结束了   取费用项的结束时间
            if (feeDto.getConfigEndTime().getTime() < targetEndDate.getTime()) {
                targetEndDate = feeDto.getConfigEndTime();
            }
            //说明欠费
            if (endDate.getTime() < targetEndDate.getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = dayCompare(endDate, targetEndDate);
            }

            if (feeDto.getEndTime().getTime() > targetEndDate.getTime()) {
                targetEndDate = feeDto.getEndTime();
            }
        }

        targetEndDateAndOweMonth.put("oweMonth", oweMonth);
        targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
        return targetEndDateAndOweMonth;
    }

    public Map getTargetEndDateAndOweMonth(FeeDto feeDto) {

//        if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
//            OwnerCarDto ownerCarDto = new OwnerCarDto();
//            ownerCarDto.setCommunityId(feeDto.getCommunityId());
//            ownerCarDto.setCarId(feeDto.getPayerObjId());
//            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
//            return getTargetEndDateAndOweMonth(feeDto, ownerCarDtos == null || ownerCarDtos.size() < 1 ? null : ownerCarDtos.get(0));
//        }
        return getTargetEndDateAndOweMonth(feeDto, null);
    }

    /**
     * 计算 两个时间点月份
     *
     * @param fromDate 开始时间
     * @param toDate   结束时间
     * @return
     */
    @Override
    public double dayCompare(Date fromDate, Date toDate) {
        double resMonth = 0.0;
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //比较月份差 可能有整数 也会负数
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        //比较年差
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;

        //真实 相差月份
        result = result + month;

        //开始时间  2021-06-01  2021-08-05   result = 2    2021-08-01
        Calendar newFrom = Calendar.getInstance();
        newFrom.setTime(fromDate);
        newFrom.add(Calendar.MONTH, result);
        //如果加月份后 大于了当前时间 默认加 月份 -1 情况 12-19  21-01-10
        //这个是神的逻辑一定好好理解
        if (newFrom.getTime().getTime() > toDate.getTime()) {
            newFrom.setTime(fromDate);
            result = result - 1;
            newFrom.add(Calendar.MONTH, result);
        }

        // t1 2021-08-01   t2 2021-08-05
        long t1 = newFrom.getTime().getTime();
        long t2 = to.getTime().getTime();
        //相差毫秒
        double days = (t2 - t1) * 1.00 / (24 * 60 * 60 * 1000);
        BigDecimal tmpDays = new BigDecimal(days); //相差天数
        BigDecimal monthDay = null;
        Calendar newFromMaxDay = Calendar.getInstance();
        newFromMaxDay.set(newFrom.get(Calendar.YEAR), newFrom.get(Calendar.MONTH), 1, 0, 0, 0);
        newFromMaxDay.add(Calendar.MONTH, 1); //下个月1号
        //在当前月中 这块有问题
        if (toDate.getTime() < newFromMaxDay.getTime().getTime()) {
            monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
            return tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).doubleValue();
        }
        // 上月天数
        days = (newFromMaxDay.getTimeInMillis() - t1) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
        BigDecimal preRresMonth = tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP);

        //下月天数
        days = (t2 - newFromMaxDay.getTimeInMillis()) * 1.00 / (24 * 60 * 60 * 1000);
        tmpDays = new BigDecimal(days);
        monthDay = new BigDecimal(newFromMaxDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        resMonth = tmpDays.divide(monthDay, 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(result)).add(preRresMonth).doubleValue();
        return resMonth;
    }


    //手机端缴费处理
    public Map getOwnerPhoneFee(Map feeAmount) {
        feeAmount.put("feePrice", new BigDecimal(1.00 / 100));
        feeAmount.put("feeTotalPrice", new BigDecimal(1.00 / 100));
        return feeAmount;
    }


    /**
     * 　　 *字符串的日期格式的计算
     */
    public long daysBetween(Date smdate, Date bdate) {
        long between_days = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        between_days = (time2 - time1) / (1000 * 3600 * 24);

        return between_days;
    }

    @Override
    public Date getTargetEndTime(double month, Date startDate) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate);

        Double intMonth = Math.floor(month);
        endDate.add(Calendar.MONTH, intMonth.intValue());
        double doubleMonth = month - intMonth;
        if (doubleMonth <= 0) {
            return endDate.getTime();
        }
        int futureDay = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        Double hour = doubleMonth * futureDay * 24;
        endDate.add(Calendar.HOUR_OF_DAY, hour.intValue());
        return endDate.getTime();
    }


    @Override
    public List<CarInoutDto> computeTempCarStopTimeAndFee(List<CarInoutDto> carInoutDtos) {

        if (carInoutDtos == null || carInoutDtos.size() < 1) {
            return null;
        }


        carInoutDtos = tempCarFeeConfigInnerServiceSMOImpl.computeTempCarFee(carInoutDtos);

        return carInoutDtos;

    }

    @Override
    public List<CarInoutDetailDto> computeTempCarInoutDetailStopTimeAndFee(List<CarInoutDetailDto> carInoutDtos) {
        if (carInoutDtos == null || carInoutDtos.size() < 1) {
            return null;
        }


        carInoutDtos = tempCarFeeConfigInnerServiceSMOImpl.computeTempCarInoutDetailFee(carInoutDtos);

        return carInoutDtos;
    }


    public static void main(String[] args) {
        ComputeFeeSMOImpl computeFeeSMO = new ComputeFeeSMOImpl();
        try {
            double month = computeFeeSMO.dayCompare(
                    DateUtil.getDateFromString("2022-04-14 00:00:00", DateUtil.DATE_FORMATE_STRING_A),
                    DateUtil.getDateFromString("2022-06-01 00:00:00", DateUtil.DATE_FORMATE_STRING_A)
            );
            System.out.println(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        ComputeFeeSMOImpl computeFeeSMO = new ComputeFeeSMOImpl();
//        try {
//            Date startTime = DateUtil.getDateFromString("2020-12-31 00:00:00", DateUtil.DATE_FORMATE_STRING_A);
//            Date endTime = DateUtil.getDateFromString("2021-1-2 00:00:00", DateUtil.DATE_FORMATE_STRING_A);
//            double day = (endTime.getTime() - startTime.getTime()) * 1.00 / (24 * 60 * 60 * 1000);
//
//            System.out.println(day);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
