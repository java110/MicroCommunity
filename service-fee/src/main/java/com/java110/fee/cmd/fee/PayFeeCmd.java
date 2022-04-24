package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeReceipt.FeeReceiptDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.constant.FeeStateConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.payFee")
public class PayFeeCmd extends AbstractServiceCmdListener {
    private static Logger logger = LoggerFactory.getLogger(PayFeeCmd.class);


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeDetailNewV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


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
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairPoolNewV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairUserNewV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private ICouponUserDetailV1InnerServiceSMO couponUserDetailV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(reqJson, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求报文中未包含feeId节点");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("cycles"), "周期不能为空");
        Assert.hasLength(reqJson.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(reqJson.getString("feeId"), "费用ID不能为空");

        //判断是否 费用状态为缴费结束
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "传入费用ID错误");

        feeDto = feeDtos.get(0);

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            throw new IllegalArgumentException("收费已经结束，不能再缴费");
        }

        Date endTime = feeDto.getEndTime();

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (feeConfigDtos != null && feeConfigDtos.size() == 1) {
            try {
                Date configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);

                Date newDate = DateUtil.stepMonth(endTime, reqJson.getInteger("cycles") - 1);

                if (newDate.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("缴费周期超过 缴费结束时间");
                }

            } catch (Exception e) {
                logger.error("比较费用日期失败", e);
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject paramObj) throws CmdException {

        logger.debug("paramObj : {}", paramObj);
        PayFeePo payFeePo = null;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + paramObj.get("feeId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            JSONObject feeDetail = addFeeDetail(paramObj);
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(feeDetail, PayFeeDetailPo.class);
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
            DistributedLock.releaseDistributedLock(requestId, key);
        }
        //账户处理
        dealAccount(paramObj);
        //折扣管理
        if (paramObj.containsKey("selectDiscount")) {
            JSONObject discountBusiness = null;
            JSONArray selectDiscounts = paramObj.getJSONArray("selectDiscount");
            for (int discountIndex = 0; discountIndex < selectDiscounts.size(); discountIndex++) {
                addPayFeeDetailDiscount(paramObj, selectDiscounts.getJSONObject(discountIndex));
            }
        }

        //为停车费单独处理
        updateCarEndTime(paramObj);

        //处理报修单
        doDealRepairOrder(paramObj);


        //修改折扣申请状态，空置房折扣只能用一次
        String selectDiscount = paramObj.getString("selectDiscount");
        JSONArray params = JSONArray.parseArray(selectDiscount);
        for (int index = 0; index < params.size(); index++) {
            JSONObject param = params.getJSONObject(index);
            if (!StringUtil.isEmpty(param.getString("ardId"))) {
                ApplyRoomDiscountPo applyRoomDiscountPo = new ApplyRoomDiscountPo();
                //空置房优惠不可用
                applyRoomDiscountPo.setInUse("1");
                applyRoomDiscountPo.setArdId(param.getString("ardId"));
                applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);
            }
        }

        //根据明细ID 查询收据信息
        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailId(paramObj.getString("detailId"));
        feeReceiptDetailDto.setCommunityId(paramObj.getString("communityId"));
        List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);

        if (feeReceiptDetailDtos != null && feeReceiptDetailDtos.size() > 0) {
            cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(feeReceiptDetailDtos.get(0)));
            return;
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(feeReceiptDetailDto));
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
     * 处理停车费
     *
     * @param paramObj
     */
    private void updateCarEndTime(JSONObject paramObj) {
        int flag;
        if (paramObj.containsKey("carPayerObjType") && FeeDto.PAYER_OBJ_TYPE_CAR.equals(paramObj.getString("carPayerObjType"))) {
            Date feeEndTime = (Date) paramObj.get("carFeeEndTime");
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCommunityId(paramObj.getString("communityId"));
            ownerCarDto.setCarId(paramObj.getString("carPayerObjId"));
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
            //车位费用续租
            if (ownerCarDtos != null) {
                for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
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
        }
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscount(JSONObject paramInJson, JSONObject discountJson) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailDiscountId));
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscount = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
        //businessFee.putAll(feeMap);
        int fage = payFeeDetailDiscountNewV1InnerServiceSMOImpl.savePayFeeDetailDiscountNew(payFeeDetailDiscount);

        if (fage < 1) {
            throw new CmdException("更新费用信息失败");
        }
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson) {
        String remark = paramInJson.getString("remark");
        if (!StringUtil.isEmpty(remark)) {
            remark = "-" + remark;
        }
        paramInJson.put("remark", "现场收银台支付" + remark);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));
        //支付方式
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        if (!businessFeeDetail.containsKey("state") || StringUtil.isEmpty(businessFeeDetail.getString("state"))) {
            businessFeeDetail.put("state", "1400");
        }
        feeDto = feeDtos.get(0);
        businessFeeDetail.put("startTime", DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        int hours = 0;
        Date targetEndTime = null;
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        if ("-101".equals(paramInJson.getString("cycles"))) {
            Date endTime = feeDto.getEndTime();
            Calendar endCalender = Calendar.getInstance();
            endCalender.setTime(endTime);
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            endCalender = getTargetEndTime(endCalender, cycles.doubleValue());
            targetEndTime = endCalender.getTime();
            paramInJson.put("tmpCycles", cycles.doubleValue());
            businessFeeDetail.put("cycles", cycles.doubleValue());
            businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
        } else {
            targetEndTime = computeFeeSMOImpl.getFeeEndTimeByCycles(feeDto, paramInJson.getString("cycles"));
            cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
            double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
        }
        businessFeeDetail.put("endTime", DateUtil.getFormatTimeString(targetEndTime, DateUtil.DATE_FORMATE_STRING_A));
        paramInJson.put("feeInfo", feeDto);

        return businessFeeDetail;
    }

    public JSONObject modifyFee(JSONObject paramInJson) {

        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        if ("-101".equals(paramInJson.getString("cycles"))) {
            endCalender = getTargetEndTime(endCalender, Double.parseDouble(paramInJson.getString("tmpCycles")));
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
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
            //businessFee.put("state",FeeDto.STATE_FINISH);
            feeInfo.setState(FeeDto.STATE_FINISH);
        }
        feeInfo.setEndTime(endCalender.getTime());
        //判断 结束时间 是否大于 费用项 结束时间，这里 容错一下，如果 费用结束时间大于 费用项结束时间 30天 走报错 属于多缴费
        if (feeInfo.getEndTime().getTime() - feeInfo.getConfigEndTime().getTime() > 30 * 24 * 60 * 60 * 1000L) {
            throw new IllegalArgumentException("缴费超过了 费用项结束时间");
        }
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        businessFee.putAll(feeMap);
        //为停车费单独处理
        paramInJson.put("carFeeEndTime", feeInfo.getEndTime());
        paramInJson.put("carPayerObjType", feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId", feeInfo.getPayerObjId());

        // 周期性收费、缴费后，到期日期在费用项终止日期后，则设置缴费状态结束，设置结束日期为费用项终止日期
        if (FeeFlagTypeConstant.CYCLE.equals(feeInfo.getFeeFlag())) {
            //这里 容错五天时间
            Date configEndTime = feeInfo.getConfigEndTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(configEndTime);
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            configEndTime = calendar.getTime();
            if (feeInfo.getEndTime().after(configEndTime)) {
                businessFee.put("state", FeeStateConstant.END);
                businessFee.put("endTime", feeInfo.getConfigEndTime());
            }
        }
        return businessFee;
    }


    public void dealAccount(JSONObject paramObj) {
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectUserAccount");
        if (jsonArray == null || jsonArray.size() < 1) {
            return;
        }
        //应收款 totalFeePrice
        BigDecimal totalFeePrice = new BigDecimal(paramObj.getString("totalFeePrice")); //应收款
        //实收款 receivedAmount
        BigDecimal receivedAmount = new BigDecimal(paramObj.getString("receivedAmount")); //实收款（扣款金额）

        BigDecimal redepositAmount = new BigDecimal("0.00");//抵扣金额
        for (int columnIndex = 0; columnIndex < jsonArray.size(); columnIndex++) {
            JSONObject param = jsonArray.getJSONObject(columnIndex);
            //账户金额
            BigDecimal amount = new BigDecimal(param.getString("amount")); //账户金额
            int flag = amount.compareTo(receivedAmount);
            if (flag == -1) {//账户金额小于实收款
                receivedAmount = receivedAmount.subtract(amount);
                redepositAmount = amount;//抵扣金额
            } else {
                redepositAmount = receivedAmount;//抵扣金额
            }
            //剩余金额
            amount.compareTo(receivedAmount);
            String acctId = param.getString("acctId");
            if (StringUtil.isEmpty(acctId)) {
                throw new IllegalArgumentException("账户id为空！");
            }
            AccountDto accountDto = new AccountDto();
            accountDto.setAcctId(acctId);
            //查询账户金额
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            Assert.listOnlyOne(accountDtos, "查询账户金额错误！");
            if (accountDtos != null && accountDtos.size() > 0) {
                AccountDto accountDto1 = accountDtos.get(0);
                BigDecimal accountDto1Amount = new BigDecimal(accountDto1.getAmount());
                if (accountDto1Amount.compareTo(redepositAmount) == -1) {
                    throw new UnsupportedOperationException("账户金额抵扣不足，请您确认账户金额！");
                }
            }


            AccountDetailPo accountDetailPo = new AccountDetailPo();
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            int flag1 = redepositAmount.compareTo(BigDecimal.ZERO);
            if (flag1 == 1) {
                accountDetailPo.setAmount(redepositAmount + "");
                accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
                accountDetailPo.setRemark("前台缴费扣款");
            }
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetailPo.setObjType(accountDtos.get(0).getObjType());
            accountDetailPo.setObjId(accountDtos.get(0).getObjId());
            accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
            accountDetailPo.setRelAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

            flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

            if (flag < 1) {
                throw new CmdException("扣款失败");
            }
        }


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

}