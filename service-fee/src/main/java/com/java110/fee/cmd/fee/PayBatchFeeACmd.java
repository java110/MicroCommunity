package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Java110Cmd(serviceCode = "fee.payBatchFee")
public class PayBatchFeeACmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayBatchFeeACmd.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;
    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");

        if (!reqJson.containsKey("fees")) {
            throw new CmdException("未包含费用");
        }

        JSONArray fees = reqJson.getJSONArray("fees");

        if (ListUtil.isNull(fees)) {
            throw new CmdException("未包含费用");
        }
        JSONObject paramInObj = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            paramInObj = fees.getJSONObject(feeIndex);
            //判断是否 费用状态为缴费结束
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(paramInObj.getString("feeId"));
            feeDto.setCommunityId(reqJson.getString("communityId"));
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "传入费用ID错误");
            feeDto = feeDtos.get(0);
            if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
                throw new IllegalArgumentException("收费已经结束，不能再缴费");
            }
            paramInObj.put("feeDto", feeDto);
            Date endTime = feeDto.getEndTime();
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(feeDto.getConfigId());
            feeConfigDto.setCommunityId(paramInObj.getString("communityId"));
            List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
            if (ListUtil.isNull(feeConfigDtos)) {
                throw new IllegalArgumentException("费用项不存在");
            }
            Date maxEndTime = feeDtos.get(0).getDeadlineTime();
            //周期性费用
            if (maxEndTime == null || FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
                maxEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
            }

            if (maxEndTime != null && endTime != null && !FeeDto.FEE_FLAG_ONCE.equals(feeConfigDtos.get(0).getFeeFlag())) {
                Date newDate = DateUtil.stepMonth(endTime, paramInObj.getDouble("cycles").intValue());
                if (newDate.getTime() > maxEndTime.getTime()) {
                    throw new IllegalArgumentException("缴费周期超过 缴费结束时间,请用按结束时间方式缴费");
                }
            }
        }
    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");

        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(reqJson.getString("communityId"));

        String payOrderId = reqJson.getString("payOrderId");

        JSONArray fees = reqJson.getJSONArray("fees");
        JSONObject paramInObj = null;
        JSONArray details = new JSONArray();
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            try {
                paramInObj = fees.getJSONObject(feeIndex);
                doDeal(paramInObj, reqJson.getString("communityId"), receiptCode, cmdDataFlowContext, userDtos.get(0), payOrderId);
            } catch (Exception e) {
                logger.error("处理异常", e);
                throw new CmdException(e.getMessage());
            }
            details.add(paramInObj.getString("detailId"));
        }

        JSONObject data = new JSONObject();
        data.put("details", details);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    private void doDeal(JSONObject paramObj, String communityId, String receiptCode,
                        ICmdDataFlowContext cmdDataFlowContext,
                        UserDto userDto,
                        String payOrderId) throws Exception {
        paramObj.put("communityId", communityId);
        //获取订单ID
        String oId = Java110TransactionalFactory.getOId();

        //开始锁代码
        PayFeePo payFeePo = null;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + paramObj.get("feeId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            JSONObject feeDetail = addFeeDetail(paramObj);
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(feeDetail, PayFeeDetailPo.class);
            if (StringUtil.isEmpty(oId)) {
                oId = payFeeDetailPo.getDetailId();
            }
            payFeeDetailPo.setPayOrderId(oId);
            // todo 如果 扫码枪支付 输入支付订单ID
            if (!StringUtil.isEmpty(payOrderId)) {
                payFeeDetailPo.setPayOrderId(payOrderId);
            }
            payFeeDetailPo.setCashierId(userDto.getUserId());
            payFeeDetailPo.setCashierName(userDto.getName());
            payFeeDetailPo.setOpenInvoice("N");
            //todo 缓存收据编号
            CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
            int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
            JSONObject fee = modifyFee(paramObj);
            payFeePo = BeanConvertUtil.covertBean(fee, PayFeePo.class);

            flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }
        //车辆延期
        updateOwnerCarEndTime(payFeePo, paramObj);

        //处理报修单
        doDealRepairOrder(paramObj);

    }

    /**
     * 处理报修单
     *
     * @param paramObj
     */
    private void doDealRepairOrder(JSONObject paramObj) {
        int flag;//判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(paramObj.getString("communityId"));
        feeAttrDto.setFeeId(paramObj.getString("feeId"));
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
        //修改 派单状态
        if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(feeAttrDtos.get(0).getValue());
            //查询报修记录
            List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
            Assert.listOnlyOne(repairDtos, "报修信息错误！");
            //获取报修渠道
            String repairChannel = repairDtos.get(0).getRepairChannel();
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
            repairPoolPo.setCommunityId(paramObj.getString("communityId"));
            if (repairChannel.equals("Z")) { //如果是业主自主报修，状态就变成待评价
                repairPoolPo.setState(RepairDto.STATE_APPRAISE);
            } else { //如果是员工代客报修或电话报修，状态就变成待回访
                repairPoolPo.setState(RepairDto.STATE_RETURN_VISIT);
            }
            flag = repairPoolNewV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }

            repairDto = new RepairDto();
            repairDto.setRepairId(feeAttrDtos.get(0).getValue());
            //查询报修记录
            repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);

            Assert.listOnlyOne(repairDtos, "报修信息错误！");
            //获取报修渠道
            repairChannel = repairDtos.get(0).getRepairChannel();
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
                repairUserPo.setContext("已支付" + paramObj.getString("feePrice") + "元");
                //新增待评价状态
                RepairUserPo repairUser = new RepairUserPo();
                repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
                repairUser.setStartTime(repairUserPo.getEndTime());
                repairUser.setState(RepairUserDto.STATE_EVALUATE);
                repairUser.setContext("待评价");
                repairUser.setCommunityId(paramObj.getString("communityId"));
                repairUser.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUser.setRepairId(repairUserDtoList.get(0).getRepairId());
                repairUser.setStaffId(repairUserDtoList.get(0).getStaffId());
                repairUser.setStaffName(repairUserDtoList.get(0).getStaffName());
                repairUser.setPreStaffId(repairUserDtoList.get(0).getStaffId());
                repairUser.setPreStaffName(repairUserDtoList.get(0).getStaffName());
                repairUser.setPreRuId(repairUserDtoList.get(0).getRuId());
                repairUser.setRepairEvent("auditUser");
                flag = repairUserNewV1InnerServiceSMOImpl.saveRepairUserNew(repairUser);
                if (flag < 1) {
                    throw new CmdException("更新微信派单池信息失败");
                }
            } else {  //如果是员工代客报修或电话报修，状态就变成已支付
                repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
                //如果是已支付状态，就更新结束时间
                repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setContext("已支付" + paramObj.getString("feePrice") + "元");
            }
            flag = repairUserNewV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
        }
    }

    /**
     * 修改车辆 结束时间
     *
     * @param payFeePo
     * @param paramObj
     * @throws ParseException
     */
    private void updateOwnerCarEndTime(PayFeePo payFeePo, JSONObject paramObj) throws ParseException {
        FeeDto feeInfo = (FeeDto) paramObj.get("feeDto");
        int flag;//为停车费单独处理
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeInfo.getPayerObjType())) {
            return;
        }
        Date feeEndTime = DateUtil.getDateFromString(payFeePo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(feeInfo.getCommunityId());
        ownerCarDto.setCarId(feeInfo.getPayerObjId());
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Assert.listOnlyOne(ownerCarDtos, "查询业主错误！");
        //获取车位id
        String psId = ownerCarDtos.get(0).getPsId();
        //获取车辆状态(1001 正常状态，2002 欠费状态  3003 车位释放)
        String carState = ownerCarDtos.get(0).getState();
        if (!StringUtil.isEmpty(psId) && !StringUtil.isEmpty(carState) && carState.equals("3003")) {
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
        //车位费用续租
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            //后付费 或者信用期车辆 加一个月
            if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeInfo.getPaymentCd())
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

    public JSONObject addFeeDetail(JSONObject paramInJson) {
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        //todo 支付方式
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        businessFeeDetail.put("state", FeeDetailDto.STATE_NORMAL);

        //todo 查询fee对象,这个在校验时已经写过了
        FeeDto feeDto = (FeeDto) paramInJson.get("feeDto");

        businessFeeDetail.put("startTime", DateUtil.getFormatTimeStringA(feeDto.getEndTime()));
        Date targetEndTime = null;
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());

        targetEndTime = computeFeeSMOImpl.getFeeEndTimeByCycles(feeDto, paramInJson.getString("cycles"));
        cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
        double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        businessFeeDetail.put("payableAmount", tmpReceivableAmount);

        businessFeeDetail.put("endTime", DateUtil.getFormatTimeStringA(targetEndTime));
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));

        return businessFeeDetail;
    }


    /**
     * 修改费用信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject modifyFee(JSONObject paramInJson) {
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeDto");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        if (FeeDto.FEE_FLAG_ONCE.equals(feeInfo.getFeeFlag())) {
            if (feeInfo.getDeadlineTime() != null) {
                endCalender.setTime(feeInfo.getDeadlineTime());
            } else if (!StringUtil.isEmpty(feeInfo.getCurDegrees())) {
                endCalender.setTime(feeInfo.getCurReadingTime());
            } else if (feeInfo.getImportFeeEndTime() == null) {
                endCalender.setTime(feeInfo.getConfigEndTime());
            } else {
                endCalender.setTime(feeInfo.getImportFeeEndTime());
            }
            feeInfo.setState(FeeDto.STATE_FINISH);
        }
        feeInfo.setEndTime(endCalender.getTime());
        Date maxEndTime = feeInfo.getDeadlineTime();
        if (FeeDto.FEE_FLAG_CYCLE.equals(feeInfo.getFeeFlag())) {
            maxEndTime = feeInfo.getConfigEndTime();
        }
        //判断 结束时间 是否大于 费用项 结束时间，这里 容错一下，如果 费用结束时间大于 费用项结束时间 30天 走报错 属于多缴费
        if (maxEndTime != null) {
            if (feeInfo.getEndTime().getTime() - maxEndTime.getTime() > 30 * 24 * 60 * 60 * 1000L) {
                throw new IllegalArgumentException("缴费超过了 费用项结束时间");
            }
        }
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeStringA(feeInfo.getStartTime()));
        feeMap.put("endTime", DateUtil.getFormatTimeStringA(feeInfo.getEndTime()));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        JSONObject businessFee = new JSONObject();
        businessFee.putAll(feeMap);
        //为停车费单独处理
        paramInJson.put("carFeeEndTime", feeInfo.getEndTime());
        paramInJson.put("carPayerObjType", feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId", feeInfo.getPayerObjId());


        // 周期性收费、缴费后，到期日期在费用项终止日期后，则设置缴费状态结束，设置结束日期为费用项终止日期
        if (!FeeFlagTypeConstant.ONETIME.equals(feeInfo.getFeeFlag())) {
            //这里 容错五天时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(maxEndTime);
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            maxEndTime = calendar.getTime();
            if (feeInfo.getEndTime().after(maxEndTime)) {
                businessFee.put("state", FeeConfigConstant.END);
                businessFee.put("endTime", maxEndTime);
            }
        }

        return businessFee;
    }

    private static Calendar getTargetEndTime(Calendar endCalender, Double cycles) {
        if (StringUtil.isInteger(cycles.toString())) {
            endCalender.add(Calendar.MONTH, new Double(cycles).intValue());
            return endCalender;
        }
        if (cycles >= 1) {
            endCalender.add(Calendar.MONTH, new Double(Math.floor(cycles)).intValue());
            cycles = cycles - Math.floor(cycles);
        }
        int futureDay = endCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int hours = new Double(cycles * futureDay * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        return endCalender;
    }



    private void computeCycleEndTimeAmount(FeeDto feeDto, JSONObject paramInObj) {

    }

}

