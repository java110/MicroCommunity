package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.fee.ApiFeeDataVo;
import com.java110.vo.api.fee.ApiFeeVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.listFee")
public class ListFeeCmd extends AbstractServiceCmdListener {
    private static Logger logger = LoggerFactory.getLogger(ListFeeCmd.class);

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

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

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

        List<ApiFeeDataVo> fees = new ArrayList<>();

        if (count > 0) {
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);//查询费用项目
            computeFeePrice(feeDtos);//计算费用
            List<ApiFeeDataVo> apiFeeDataVos = BeanConvertUtil.covertBeanList(feeDtos, ApiFeeDataVo.class);
            for (ApiFeeDataVo apiFeeDataVo : apiFeeDataVos) {
                //获取付费对象类型
                String payerObjType = apiFeeDataVo.getPayerObjType();
                if (!StringUtil.isEmpty(payerObjType) && payerObjType.equals("6666")) {
                    apiFeeDataVo.setCarTypeCd("1001");
                }
                fees.add(apiFeeDataVo);
            }
            freshFeeAttrs(fees, feeDtos);
        } else {
            fees = new ArrayList<>();
        }
        ApiFeeVo apiFeeVo = new ApiFeeVo();
        apiFeeVo.setTotal(count);
        apiFeeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiFeeVo.setFees(fees);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshFeeAttrs(List<ApiFeeDataVo> fees, List<FeeDto> feeDtos) {
        for (ApiFeeDataVo apiFeeDataVo : fees) {
            for (FeeDto feeDto : feeDtos) {
                if (apiFeeDataVo.getFeeId().equals(feeDto.getFeeId())) {
                    apiFeeDataVo.setFeeAttrs(feeDto.getFeeAttrDtos());
                }
            }
        }
    }

    private void computeFeePrice(List<FeeDto> feeDtos) {

        if(feeDtos == null || feeDtos.size() < 1){
            return;
        }
        String val = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(),TOTAL_FEE_PRICE);
        if(StringUtil.isEmpty(val)){
            val = MappingCache.getValue(DOMAIN_COMMON, TOTAL_FEE_PRICE);
        }

        //先取单小区的如果没有配置 取 全局的
        String received_amount_switch = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(),RECEIVED_AMOUNT_SWITCH);
        if(StringUtil.isEmpty(received_amount_switch)){
            received_amount_switch = MappingCache.getValue(DOMAIN_COMMON, RECEIVED_AMOUNT_SWITCH);
        }

        //先取单小区的如果没有配置 取 全局的
        String offlinePayFeeSwitch = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(),OFFLINE_PAY_FEE_SWITCH);
        if(StringUtil.isEmpty(offlinePayFeeSwitch)){
            offlinePayFeeSwitch = MappingCache.getValue(DOMAIN_COMMON, OFFLINE_PAY_FEE_SWITCH);
        }

        for (FeeDto feeDto : feeDtos) {
            try {
                // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
                Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto);
                Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
                double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
                feeDto.setCycle(feeDto.getPaymentCycle());
                if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
                    computeFeePriceByRoom(feeDto, oweMonth);
                } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
                    computeFeePriceByCar(feeDto, oweMonth);
                } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) {//车位相关
                    computeFeePriceByContract(feeDto, oweMonth);
                }
                feeDto.setDeadlineTime(targetEndDate);

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
        }
    }

    private void computeFeePriceByCar(FeeDto feeDto, double oweMonth) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        ownerCarDto.setCommunityId(feeDto.getCommunityId());
        ownerCarDto.setCarId(feeDto.getPayerObjId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) { //数据有问题
            return;
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
            return;
        }
        String computingFormula = feeDto.getComputingFormula();
        DecimalFormat df = new DecimalFormat("0.00");
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        feeDto.setFeeTotalPrice(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()));

        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(df.format(curFeePrice));
        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(df.format(curFeePrice));
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto, double oweMonth) {
        String computingFormula = feeDto.getComputingFormula();
        DecimalFormat df = new DecimalFormat("0.00");
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        feeDto.setFeeTotalPrice(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()));
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed((df.format(curFeePrice)));
        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(df.format(curFeePrice) + "");
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
    }

    /**
     * 根据合同来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByContract(FeeDto feeDto, double oweMonth) {
        String computingFormula = feeDto.getComputingFormula();
        DecimalFormat df = new DecimalFormat("0.00");
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        feeDto.setFeeTotalPrice(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()));
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(df.format(curFeePrice));
        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            feeDto.setAmountOwed(df.format(curFeePrice) + "");
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
    }
}
