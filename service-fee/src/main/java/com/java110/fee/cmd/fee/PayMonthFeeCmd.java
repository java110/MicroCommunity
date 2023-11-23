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
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.payFee.PayFeeDetailMonthDto;
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
import java.text.ParseException;
import java.util.*;

/**
 * 按月交费
 */
@Java110Cmd(serviceCode = "fee.payMonthFee")
public class PayMonthFeeCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(PayMonthFeeCmd.class);
    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;


    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "primeRate", "未包含支付方式");
        if (!reqJson.containsKey("selectMonthIds")) {
            throw new CmdException("未包含缴费月份");
        }

        JSONArray selectMonthIds = reqJson.getJSONArray("selectMonthIds");
        if (selectMonthIds == null || selectMonthIds.size() < 1) {
            throw new CmdException("未包含缴费月份");
        }
        List<String> monthIds = new ArrayList<>();
        for (int monthIndex = 0; monthIndex < selectMonthIds.size(); monthIndex++) {
            monthIds.add(selectMonthIds.getString(monthIndex));
        }

        // todo 查询需要缴费的明细月
        PayFeeDetailMonthDto payFeeDetailMonthDto = new PayFeeDetailMonthDto();
        payFeeDetailMonthDto.setMonthIds(monthIds.toArray(new String[monthIds.size()]));
        payFeeDetailMonthDto.setCommunityId(reqJson.getString("communityId"));
        payFeeDetailMonthDto.setDetailId("-1");
        List<PayFeeDetailMonthDto> payFeeDetailMonthDtos = payFeeDetailMonthInnerServiceSMOImpl.queryPagePayFeeDetailMonths(payFeeDetailMonthDto);

        if (payFeeDetailMonthDtos == null || payFeeDetailMonthDtos.size() < 1) {
            throw new CmdException("未包含缴费月份");
        }

        Map<String, List<PayFeeDetailMonthDto>> feeMonths = new HashMap<>();

        //todo 检查是否跳月了
        PayFeeDetailMonthDto feeDetailMonthDto = null;
        for (PayFeeDetailMonthDto tmpPayFeeDetailMonthDto : payFeeDetailMonthDtos) {
            if (!feeMonths.containsKey(tmpPayFeeDetailMonthDto.getFeeId())) {
                feeDetailMonthDto = new PayFeeDetailMonthDto();
                feeDetailMonthDto.setFeeId(tmpPayFeeDetailMonthDto.getFeeId());
                feeDetailMonthDto.setDetailId("-1");
                feeDetailMonthDto.setCommunityId(reqJson.getString("communityId"));
                List<PayFeeDetailMonthDto> feeDetailMonthDtos = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonths(feeDetailMonthDto);
                feeMonths.put(tmpPayFeeDetailMonthDto.getFeeId(), feeDetailMonthDtos);
            }
            //todo 单条费用校验是否夸月缴费
            validateOneMonthSkipPayFee(tmpPayFeeDetailMonthDto, feeMonths);

        }

        reqJson.put("payFeeDetailMonthDtos", payFeeDetailMonthDtos);


    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");


        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(reqJson.getString("communityId"));

        List<PayFeeDetailMonthDto> payFeeDetailMonthDtos = (List<PayFeeDetailMonthDto>) reqJson.get("payFeeDetailMonthDtos");

        Calendar createTimeCal = Calendar.getInstance();

        Map<String, FeeDto> feeDtoMap = new HashMap<>();

        JSONArray details = new JSONArray();
        for (PayFeeDetailMonthDto payFeeDetailMonthDto : payFeeDetailMonthDtos) {
            // todo 费用只查一次提高 效率
            if (!feeDtoMap.containsKey(payFeeDetailMonthDto.getFeeId())) {
                //todo 查询费用是否存在
                FeeDto feeDto = new FeeDto();
                feeDto.setFeeId(payFeeDetailMonthDto.getFeeId());
                feeDto.setCommunityId(payFeeDetailMonthDto.getCommunityId());
                List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
                if (feeDtos == null || feeDtos.size() != 1) {
                    throw new CmdException("费用不存在");
                }
                feeDtoMap.put(payFeeDetailMonthDto.getFeeId(), feeDtos.get(0));
            }
            createTimeCal.add(Calendar.SECOND, 1);
            try {
                doMonthFee(payFeeDetailMonthDto, context, userDtos.get(0), reqJson, createTimeCal.getTime(), feeDtoMap,receiptCode);
            } catch (Exception e) {
                logger.error("处理异常", e);
                throw new CmdException(e.getMessage());
            }
            details.add(payFeeDetailMonthDto.getDetailId());
        }

        JSONObject data = new JSONObject();
        data.put("details", details);

        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    /**
     * 处理 月费用
     *
     * @param payFeeDetailMonthDto
     * @param context
     * @param userDto
     * @param reqJson
     */
    private void doMonthFee(PayFeeDetailMonthDto payFeeDetailMonthDto, ICmdDataFlowContext context, UserDto userDto, JSONObject reqJson, Date createTime, Map<String, FeeDto> feeDtoMap,String receiptCode) {
        //todo 计算结束时间
        Date startTime = DateUtil.getDateFromStringB(payFeeDetailMonthDto.getCurMonthTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, 1);
        String endTime = DateUtil.getFormatTimeStringB(calendar.getTime());


        //获取订单ID
        String oId = Java110TransactionalFactory.getOId();

        //开始锁代码
        PayFeePo payFeePo = null;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + payFeeDetailMonthDto.getFeeId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            //生成交费明细
            PayFeeDetailPo payFeeDetailPo = addFeeDetail(payFeeDetailMonthDto, userDto, reqJson);
            if (StringUtil.isEmpty(oId)) {
                oId = payFeeDetailPo.getDetailId();
            }
            payFeeDetailPo.setPayOrderId(oId);
            payFeeDetailPo.setEndTime(endTime);
            // todo 按月交费时 主要按时间顺序排序时 能够整齐
            payFeeDetailPo.setCreateTime(DateUtil.getFormatTimeStringA(createTime));
            payFeeDetailPo.setOpenInvoice("N");

            //todo 缓存收据编号
            CommonCache.setValue(payFeeDetailPo.getDetailId()+CommonCache.RECEIPT_CODE,receiptCode,CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
            int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
            payFeePo = modifyFee(payFeeDetailMonthDto, reqJson, feeDtoMap.get(payFeeDetailMonthDto.getFeeId()), endTime);

            flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }


            //todo 将detailId 返回出去
            payFeeDetailMonthDto.setDetailId(payFeeDetailPo.getDetailId());

        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }
        //车辆延期
        updateOwnerCarEndTime(payFeePo, reqJson, feeDtoMap.get(payFeeDetailMonthDto.getFeeId()));

        //处理报修单
        doDealRepairOrder(payFeeDetailMonthDto, feeDtoMap.get(payFeeDetailMonthDto.getFeeId()));


    }


    public PayFeePo modifyFee(PayFeeDetailMonthDto payFeeDetailMonthDto, JSONObject reqJson, FeeDto feeInfo, String endTime) {

        feeInfo.setEndTime(DateUtil.getDateFromStringB(endTime));
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

        PayFeePo payFeePo = BeanConvertUtil.covertBean(feeInfo, PayFeePo.class);
        //为停车费单独处理
        reqJson.put("carFeeEndTime", feeInfo.getEndTime());
        reqJson.put("carPayerObjType", feeInfo.getPayerObjType());
        reqJson.put("carPayerObjId", feeInfo.getPayerObjId());


        // 周期性收费、缴费后，到期日期在费用项终止日期后，则设置缴费状态结束，设置结束日期为费用项终止日期
        if (!FeeFlagTypeConstant.ONETIME.equals(feeInfo.getFeeFlag())) {
            //这里 容错五天时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(maxEndTime);
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            maxEndTime = calendar.getTime();
            if (feeInfo.getEndTime().after(maxEndTime)) {
                payFeePo.setState(FeeConfigConstant.END);
                payFeePo.setEndTime(DateUtil.getFormatTimeStringB(maxEndTime));
            }
        }

        return payFeePo;
    }


    public PayFeeDetailPo addFeeDetail(PayFeeDetailMonthDto payFeeDetailMonthDto, UserDto userDto, JSONObject reqJson) {
        PayFeeDetailPo feeDetailPo = new PayFeeDetailPo();
        feeDetailPo.setCashierName(userDto.getName());
        feeDetailPo.setReceivedAmount(payFeeDetailMonthDto.getReceivedAmount());
        feeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        feeDetailPo.setCashierId(userDto.getUserId());
        feeDetailPo.setFeeId(payFeeDetailMonthDto.getFeeId());
        feeDetailPo.setCommunityId(payFeeDetailMonthDto.getCommunityId());
        feeDetailPo.setCycles("1");
        feeDetailPo.setStartTime(payFeeDetailMonthDto.getCurMonthTime());
        feeDetailPo.setPayableAmount(payFeeDetailMonthDto.getReceivableAmount());
        feeDetailPo.setPrimeRate(reqJson.getString("primeRate"));
        feeDetailPo.setRemark(reqJson.getString("remark"));
        feeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
        feeDetailPo.setReceivableAmount(payFeeDetailMonthDto.getReceivableAmount());
        return feeDetailPo;
    }

    private void updateOwnerCarEndTime(PayFeePo payFeePo, JSONObject paramObj, FeeDto feeInfo) {
        int flag;//为停车费单独处理
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeInfo.getPayerObjType())) {
            return;
        }
        Date feeEndTime = DateUtil.getDateFromStringA(payFeePo.getEndTime());
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
        if (!StringUtil.isEmpty(psId) && "3003".equals(carState)) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(psId);
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");
            //获取车位状态(出售 S，出租 H ，空闲 F)
            String state = parkingSpaceDtos.get(0).getState();
            if (!"F".equals(state)) {
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

    private void validateOneMonthSkipPayFee(PayFeeDetailMonthDto tmpPayFeeDetailMonthDto, Map<String, List<PayFeeDetailMonthDto>> feeMonths) {
        List<PayFeeDetailMonthDto> feeDetailMonthDtos = feeMonths.get(tmpPayFeeDetailMonthDto.getFeeId());
        if (feeDetailMonthDtos == null || feeDetailMonthDtos.size() < 1) {
            throw new CmdException("未连续交费，请连续交费");
        }
        List<PayFeeDetailMonthDto> newFeeDetailMonthDtos = new ArrayList<>();
        // todo 存在 调月的情况
        for (PayFeeDetailMonthDto tmpFeeDetailMonthDto : feeDetailMonthDtos) {
            if (Integer.parseInt(tmpFeeDetailMonthDto.getDetailYear()) <= Integer.parseInt(tmpPayFeeDetailMonthDto.getDetailYear())
                    && Integer.parseInt(tmpFeeDetailMonthDto.getDetailMonth()) < Integer.parseInt(tmpPayFeeDetailMonthDto.getDetailMonth())) {
                throw new CmdException("未连续缴费，请选择" + tmpFeeDetailMonthDto.getDetailYear() + "-" + tmpFeeDetailMonthDto.getDetailMonth()
                        + " ," + tmpPayFeeDetailMonthDto.getFeeName());
            }
            if (Integer.parseInt(tmpFeeDetailMonthDto.getDetailYear()) == Integer.parseInt(tmpPayFeeDetailMonthDto.getDetailYear())
                    && Integer.parseInt(tmpFeeDetailMonthDto.getDetailMonth()) == Integer.parseInt(tmpPayFeeDetailMonthDto.getDetailMonth())) {
                continue;
            }
            newFeeDetailMonthDtos.add(tmpFeeDetailMonthDto);
        }

        feeMonths.put(tmpPayFeeDetailMonthDto.getFeeId(), newFeeDetailMonthDtos);

    }

    /**
     * 处理报修单
     *
     * @param feeDto
     */
    private void doDealRepairOrder(PayFeeDetailMonthDto payFeeDetailMonthDto, FeeDto feeDto) {
        int flag;//判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(feeDto.getCommunityId());
        feeAttrDto.setFeeId(feeDto.getFeeId());
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
            repairPoolPo.setCommunityId(feeDto.getCommunityId());
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
                repairUserPo.setContext("已支付" + payFeeDetailMonthDto.getReceivedAmount() + "元");
                //新增待评价状态
                RepairUserPo repairUser = new RepairUserPo();
                repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
                repairUser.setStartTime(repairUserPo.getEndTime());
                repairUser.setState(RepairUserDto.STATE_EVALUATE);
                repairUser.setContext("待评价");
                repairUser.setCommunityId(feeDto.getCommunityId());
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
                repairUserPo.setContext("已支付" + payFeeDetailMonthDto.getReceivedAmount() + "元");
            }
            flag = repairUserNewV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
        }
    }
}
