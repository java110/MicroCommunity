package com.java110.fee.bmo.fee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IOwnerCarNewV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FinishFeeNotifyImpl implements IFinishFeeNotify {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;


    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void updateCarEndTime(String feeId, String communityId) {
        int flag;
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (ListUtil.isNull(feeDtos)) {
            return;
        }
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDtos.get(0).getPayerObjType())) {
            return;
        }
        Date feeEndTime = feeDtos.get(0).getEndTime();
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setCarId(feeDtos.get(0).getPayerObjId());
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            return;
        }
        //获取车位id
        String psId = ownerCarDtos.get(0).getPsId();
        //todo 获取车辆状态(1001 正常状态，2002 欠费状态  3003 车位释放)
        String carState = ownerCarDtos.get(0).getState();
        if (!StringUtil.isEmpty(psId) && "3003".equals(carState)) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(psId);
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");
            //获取车位状态(出售 S，出租 H ，空闲 F)
            String state = parkingSpaceDtos.get(0).getState();
            if (!StringUtil.isEmpty(state) && !state.equals("F")) {
                throw new IllegalArgumentException("车位已被使用，不能再缴费！");
            }
        }


        Calendar endTimeCalendar = null;
        //todo 车位费用续租
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            //后付费 或者信用期车辆 加一个月
            if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeDtos.get(0).getPaymentCd())
                    || OwnerCarDto.CAR_TYPE_CREDIT.equals(tmpOwnerCarDto.getCarType())) {
                endTimeCalendar = Calendar.getInstance();
                endTimeCalendar.setTime(feeEndTime);
                endTimeCalendar.add(Calendar.MONTH, 1);
                feeEndTime = endTimeCalendar.getTime();
            }
            if (tmpOwnerCarDto.getEndTime().getTime() >= feeEndTime.getTime()) {
                continue;
            }
            OwnerCarPo ownerCarPo = new OwnerCarPo();
            ownerCarPo.setMemberId(tmpOwnerCarDto.getMemberId());
            ownerCarPo.setEndTime(DateUtil.getFormatTimeString(feeEndTime, DateUtil.DATE_FORMATE_STRING_A));
            flag = ownerCarNewV1InnerServiceSMOImpl.updateOwnerCarNew(ownerCarPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
        }
    }

    @Override
    public void updateRepair(String feeId, String communityId, String receivedAmount) {
        //判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(communityId);
        feeAttrDto.setFeeId(feeId);
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        int flag = 0;
        //修改 派单状态
        if (ListUtil.isNull(feeAttrDtos)) {
            return;
        }

        RepairPoolPo repairPoolPo = new RepairPoolPo();
        repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
        repairPoolPo.setCommunityId(communityId);
        repairPoolPo.setState(RepairDto.STATE_APPRAISE);
        flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(feeAttrDtos.get(0).getValue());
        //查询报修记录
        List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "报修信息错误！");
        //获取报修渠道
        String repairChannel = repairDtos.get(0).getRepairChannel();
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(feeAttrDtos.get(0).getValue());
        repairUserDto.setState(RepairUserDto.STATE_PAY_FEE);
        //查询待支付状态的记录
        List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtoList, "信息错误！");
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
        if ("Z".equals(repairChannel)) {  //如果业主是自主报修，状态就变成已支付，并新增一条待评价状态
            repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
            //如果是待评价状态，就更新结束时间
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setContext("已支付" + receivedAmount + "元");
            //新增待评价状态
            RepairUserPo repairUser = new RepairUserPo();
            repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
            repairUser.setStartTime(repairUserPo.getEndTime());
            repairUser.setState(RepairUserDto.STATE_EVALUATE);
            repairUser.setContext("待评价");
            repairUser.setCommunityId(communityId);
            repairUser.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUser.setRepairId(repairUserDtoList.get(0).getRepairId());
            repairUser.setStaffId(repairUserDtoList.get(0).getStaffId());
            repairUser.setStaffName(repairUserDtoList.get(0).getStaffName());
            repairUser.setPreStaffId(repairUserDtoList.get(0).getStaffId());
            repairUser.setPreStaffName(repairUserDtoList.get(0).getStaffName());
            repairUser.setPreRuId(repairUserDtoList.get(0).getRuId());
            repairUser.setRepairEvent("auditUser");
            flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        } else {  //如果是员工代客报修或电话报修，状态就变成已支付
            repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
            //如果是已支付状态，就更新结束时间
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setContext("已支付" + receivedAmount + "元");
            flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        }
    }

    @Override
    public void withholdAccount(JSONObject paramObj, String feeId, String communityId) {
        if (!paramObj.containsKey("accountAmount")) {
            return;
        }

        double accountAmount = paramObj.getDouble("accountAmount");

        if (accountAmount <= 0) {
            return;
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(paramObj.getString("acctId"));
        accountDetailPo.setAmount(accountAmount + "");
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);


    }

    @Override
    public void withholdAccount(PayFeeDataDto payFeeDataDto, String feeId, String communityId) {
        if (payFeeDataDto.getAccountAmount() <= 0) {
            return;
        }

        double accountAmount = payFeeDataDto.getAccountAmount();

        if (accountAmount <= 0) {
            return;
        }



        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(payFeeDataDto.getAcctId());
        accountDetailPo.setAmount(accountAmount + "");
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

    }
}
