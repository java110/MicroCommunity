package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.app.AppDto;
import com.java110.dto.couponUser.CouponUserDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeDiscount.ComputeDiscountDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parkingSpaceApply.ParkingSpaceApplyDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.ICouponUserDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.couponUser.CouponUserPo;
import com.java110.po.couponUserDetail.CouponUserDetailPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.parkingSpaceApply.ParkingSpaceApplyPo;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.payFeeConfirm")
public class PayFeeConfirmCmd extends Cmd {

    @Autowired
    private IFeeBMO feeBMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO iFeeDetailInnerServiceSMO;
    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;
    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;
    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private ICouponUserDetailV1InnerServiceSMO couponUserDetailV1InnerServiceSMOImpl;
    @Autowired
    private IParkingSpaceApplyV1InnerServiceSMO parkingSpaceApplyV1InnerServiceSMOImpl;
    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    //默认序列
    protected static final int DEFAULT_SEQ = 1;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "oId", "是否包含订单信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String paramIn = CommonCache.getAndRemoveValue("payFeePre" + reqJson.getString("oId"));
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        String appId = paramObj.getString("appId");

        if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {  //微信小程序支付
            paramObj.put("primeRate", "6");
            paramObj.put("remark", "线上小程序支付");
        } else if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) {  //微信公众号支付
            paramObj.put("primeRate", "5");
            paramObj.put("remark", "线上公众号支付");
        } else {
            paramObj.put("primeRate", "6");
            paramObj.put("remark", "线上小程序支付");
        }

        //处理现金账户
        dealAccount(paramObj);

        paramObj.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

        //处理 优惠折扣
        addDiscount(paramObj);

        //修改已使用优惠卷信息
        modifyCouponUser(paramObj);

        //添加单元信息
        feeBMOImpl.addFeePreDetail(paramObj);
        feeBMOImpl.modifyPreFee(paramObj);

        dealOwnerCartEndTime(paramObj);

        //判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(paramObj.getString("communityId"));
        feeAttrDto.setFeeId(paramObj.getString("feeId"));
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
        //修改 派单状态
        if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
            repairPoolPo.setCommunityId(paramObj.getString("communityId"));
            repairPoolPo.setState(RepairDto.STATE_APPRAISE);
            int flag = repairPoolNewV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(feeAttrDtos.get(0).getValue());
            repairUserDto.setState(RepairUserDto.STATE_PAY_FEE);
            //查询待支付状态的记录
            List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
            Assert.listOnlyOne(repairUserDtoList, "信息错误！");
            RepairUserPo repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
            repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
            //如果是待评价状态，就更新结束时间
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            DecimalFormat df = new DecimalFormat("0.00");
            BigDecimal payment_amount = new BigDecimal(paramObj.getString("receivableAmount"));
            repairUserPo.setContext("已支付" + df.format(payment_amount) + "元");
            flag = repairUserNewV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
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
            repairUser.setbId("-1");
            flag = repairUserNewV1InnerServiceSMOImpl.saveRepairUserNew(repairUser);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
        }

        //查询 pay_fee_detail 是否缴费
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(paramObj.getString("detailId"));
        List<FeeDetailDto> feeDetailDtoList = iFeeDetailInnerServiceSMO.queryBusinessFeeDetails(feeDetailDto);
        if (feeDetailDtoList != null && feeDetailDtoList.size() == 1) {
            //获取bId
            String bId = feeDetailDtoList.get(0).getbId();
            //获取优惠
            //List<ComputeDiscountDto> computeDiscountDtos = (List<ComputeDiscountDto>) paramObj.get("computeDiscountDtos");
            JSONArray computeDiscountDtos = paramObj.getJSONArray("computeDiscountDtos");
            ComputeDiscountDto computeDiscountDto = null;
            if (computeDiscountDtos != null && computeDiscountDtos.size() > 0) {
                for (int accountIndex = 0; accountIndex < computeDiscountDtos.size(); accountIndex++) {
                    computeDiscountDto = BeanConvertUtil.covertBean(computeDiscountDtos.getJSONObject(accountIndex), ComputeDiscountDto.class);
                    if (!StringUtil.isEmpty(computeDiscountDto.getArdId())) {
                        ApplyRoomDiscountPo applyRoomDiscountPo = new ApplyRoomDiscountPo();
                        //将业务id更新到空置房优惠里面
                        applyRoomDiscountPo.setbId(bId);
                        applyRoomDiscountPo.setArdId(computeDiscountDto.getArdId());
                        int flag = applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);
                        if (flag < 1) {
                            throw new CmdException("更新微信派单池信息失败");
                        }
                    }
                }
            }

        }
        //回调判断 车位申请表是否有数据，有数据则刷新申请表状态为 3003 完成状态即可
        //判断车辆是否已经有申请单
        ParkingSpaceApplyDto parkingSpaceApplyDto = new ParkingSpaceApplyDto();
        parkingSpaceApplyDto.setFeeId(paramObj.getString("feeId"));
        parkingSpaceApplyDto.setState("2002");//审核中
        List<ParkingSpaceApplyDto> parkingSpaceApplyDtos = parkingSpaceApplyV1InnerServiceSMOImpl.queryParkingSpaceApplys(parkingSpaceApplyDto);
        if (parkingSpaceApplyDtos != null && parkingSpaceApplyDtos.size() > 0) {
            ParkingSpaceApplyPo parkingSpaceApplyPo = new ParkingSpaceApplyPo();
            parkingSpaceApplyPo.setApplyId(parkingSpaceApplyDtos.get(0).getApplyId());
            parkingSpaceApplyPo.setState("3003");
            int flag = parkingSpaceApplyV1InnerServiceSMOImpl.updateParkingSpaceApply(parkingSpaceApplyPo);
            if (flag < 1) {
                throw new CmdException("更新车位申请表状态失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void dealAccount(JSONObject paramObj) {

        if (!paramObj.containsKey("deductionAmount") || paramObj.getDouble("deductionAmount") <= 0) {
            return;
        }

        BigDecimal deductionAmount = new BigDecimal(paramObj.getDouble("deductionAmount"));

        JSONArray accountDtos = paramObj.getJSONArray("selectUserAccount");
        BigDecimal amount = null;
        AccountDto accountDto = null;
        for (int accountIndex = 0; accountIndex < accountDtos.size(); accountIndex++) {
            accountDto = BeanConvertUtil.covertBean(accountDtos.getJSONObject(accountIndex), AccountDto.class);
            amount = new BigDecimal(Double.parseDouble(accountDto.getAmount()));
            AccountDetailPo accountDetailPo = new AccountDetailPo();
            accountDetailPo.setAcctId(accountDto.getAcctId());
            accountDetailPo.setObjId(accountDto.getObjId());
            accountDetailPo.setObjType(accountDto.getObjType());
            if (amount.doubleValue() < deductionAmount.doubleValue()) {
                accountDetailPo.setAmount(amount.doubleValue() + "");
                deductionAmount = deductionAmount.subtract(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                accountDetailPo.setAmount(deductionAmount.doubleValue() + "");
                deductionAmount = deductionAmount.subtract(deductionAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            int flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

            if (flag < 1) {
                throw new CmdException("扣款失败");
            }
        }


        if (deductionAmount.doubleValue() > 0) {
            throw new CmdException("账户金额不足");
        }

        paramObj.put("remark", paramObj.getString("remark") + "-现金账户抵扣" + paramObj.getDouble("deductionAmount") + "元");

    }

    private void modifyCouponUser(JSONObject paramObj) {
        if (!paramObj.containsKey("couponPrice") || paramObj.getDouble("couponPrice") <= 0) {
            return;
        }
        //FeeDto feeInfo = (FeeDto) paramObj.get("feeInfo");
        CouponUserDto couponUserDto = null;
        JSONArray couponUserDtos = paramObj.getJSONArray("couponUserDtos");
        CouponUserDto couponUser = null;
        for (int accountIndex = 0; accountIndex < couponUserDtos.size(); accountIndex++) {
            couponUser = BeanConvertUtil.covertBean(couponUserDtos.getJSONObject(accountIndex), CouponUserDto.class);
            couponUserDto = new CouponUserDto();
            couponUserDto.setCouponId(couponUser.getCouponId());
            couponUserDto.setState(CouponUserDto.COUPON_STATE_RUN);
            List<CouponUserDto> couponUserDtos1 = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
            if (couponUserDtos1 == null || couponUserDtos1.size() < 1) {
                throw new CmdException("优惠券被使用");
            }
            CouponUserPo couponUserPo = new CouponUserPo();
            couponUserPo.setState(CouponUserDto.COUPON_STATE_STOP);
            couponUserPo.setCouponId(couponUser.getCouponId());
            int fage = couponUserV1InnerServiceSMOImpl.updateCouponUser(couponUserPo);
            if (fage < 1) {
                throw new CmdException("更新优惠卷信息失败");
            }
            CouponUserDetailPo couponUserDetailPo = new CouponUserDetailPo();
            couponUserDetailPo.setUoId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            couponUserDetailPo.setCouponId(couponUser.getCouponId());
            couponUserDetailPo.setUserId(couponUser.getUserId());
            couponUserDetailPo.setCouponName(couponUser.getCouponName());
            couponUserDetailPo.setUserName(couponUser.getUserName());
            couponUserDetailPo.setObjId(paramObj.getString("feeId"));
            couponUserDetailPo.setObjType(paramObj.getString("feeTypeCd"));
            couponUserDetailPo.setOrderId(paramObj.getString("oId"));
            fage = couponUserDetailV1InnerServiceSMOImpl.saveCouponUserDetail(couponUserDetailPo);
            if (fage < 1) {
                throw new CmdException("新增优惠卷使用记录信息失败");
            }
        }

        paramObj.put("remark", paramObj.getString("remark") + "-优惠劵抵扣" + paramObj.getDouble("couponPrice") + "元");

    }

    private void addDiscount(JSONObject paramObj) {

        if (!paramObj.containsKey("discountPrice") || paramObj.getDouble("discountPrice") <= 0) {
            return;
        }
        JSONArray computeDiscountDtos = paramObj.getJSONArray("computeDiscountDtos");
        ComputeDiscountDto computeDiscountDto = null;
        for (int accountIndex = 0; accountIndex < computeDiscountDtos.size(); accountIndex++) {
            computeDiscountDto = BeanConvertUtil.covertBean(computeDiscountDtos.getJSONObject(accountIndex), ComputeDiscountDto.class);
            if (computeDiscountDto.getDiscountPrice() <= 0) {
                continue;
            }
            JSONObject businessFee = new JSONObject();
            businessFee.put("discountPrice", computeDiscountDto.getDiscountPrice());
            businessFee.put("discountId", computeDiscountDto.getDiscountId());
            businessFee.put("detailId", paramObj.getString("detailId"));
            businessFee.put("communityId", paramObj.getString("communityId"));
            businessFee.put("feeId", paramObj.getString("feeId"));

            PayFeeDetailDiscountPo payFeeDetailDiscount = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
            payFeeDetailDiscount.setbId("-1");
            payFeeDetailDiscount.setDetailDiscountId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
            int fage = payFeeDetailDiscountNewV1InnerServiceSMOImpl.savePayFeeDetailDiscountNew(payFeeDetailDiscount);

            if (fage < 1) {
                throw new CmdException("更新费用信息失败");
            }
        }
    }

    private void dealOwnerCartEndTime(JSONObject paramObj) {

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramObj.getString("feeId"));
        feeDto.setCommunityId(paramObj.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }
        //为停车费单独处理
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDtos.get(0).getPayerObjType())) {
            return;
        }
        Date feeEndTime = feeDtos.get(0).getEndTime();
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(paramObj.getString("communityId"));
        ownerCarDto.setCarId(feeDtos.get(0).getPayerObjId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);


        Calendar endTimeCalendar = null;
        //车位费用续租
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            //后付费 或者信用期车辆 加一个月
            if(FeeConfigDto.PAYMENT_CD_AFTER.equals(feeDtos.get(0).getPaymentCd())
                    || OwnerCarDto.CAR_TYPE_CREDIT.equals(tmpOwnerCarDto.getCarType())){
                endTimeCalendar = Calendar.getInstance();
                endTimeCalendar.setTime(feeEndTime);
                endTimeCalendar.add(Calendar.MONTH, 1);
                feeEndTime = endTimeCalendar.getTime();
            }
            if (tmpOwnerCarDto.getEndTime().getTime() < feeEndTime.getTime()) {
                OwnerCarPo ownerCarPo = new OwnerCarPo();
                ownerCarPo.setMemberId(tmpOwnerCarDto.getMemberId());
                ownerCarPo.setEndTime(DateUtil.getFormatTimeString(feeEndTime, DateUtil.DATE_FORMATE_STRING_A));
                int fage = ownerCarNewV1InnerServiceSMOImpl.updateOwnerCarNew(ownerCarPo);
                if (fage < 1) {
                    throw new CmdException("更新费用信息失败");
                }
            }
        }
    }
}
