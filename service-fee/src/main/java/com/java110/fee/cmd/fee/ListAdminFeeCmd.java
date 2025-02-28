package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.api.fee.ApiFeeDataVo;
import com.java110.vo.api.fee.ApiFeeVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.listAdminFee")
public class ListAdminFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListAdminFeeCmd.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        List<ApiFeeDataVo> fees = new ArrayList<>();
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);
        if (count > 0) {
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);//查询费用项目
            //todo 计算费用
            computeFeePrice(feeDtos);
            List<ApiFeeDataVo> apiFeeDataVos = BeanConvertUtil.covertBeanList(feeDtos, ApiFeeDataVo.class);
            for (ApiFeeDataVo apiFeeDataVo : apiFeeDataVos) {
                //获取付费对象类型
                String payerObjType = apiFeeDataVo.getPayerObjType();
                if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(payerObjType)) {
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
        context.setResponseEntity(responseEntity);
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
        if (ListUtil.isNull(feeDtos)) {
            return;
        }

        for (FeeDto feeDto : feeDtos) {
            try {
                // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
                Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto);
                Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
                double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
                feeDto.setCycle(feeDto.getPaymentCycle());
                //todo 这里考虑 账单模式的场景
                if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
                    feeDto.setCycle(oweMonth + "");
                }
                feeDto.setDeadlineTime(targetEndDate);
                //todo 算费
                doComputeFeePrice(feeDto, oweMonth);

            } catch (Exception e) {
                logger.error("查询费用信息 ，费用信息错误", e);
            }
            //去掉多余0
            feeDto.setSquarePrice(Double.parseDouble(feeDto.getSquarePrice()) + "");
            feeDto.setAdditionalAmount(Double.parseDouble(feeDto.getAdditionalAmount()) + "");
        }
    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void doComputeFeePrice(FeeDto feeDto, double oweMonth) {
        String computingFormula = feeDto.getComputingFormula();
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        //BigDecimal feeTotalPrice = new BigDecimal(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()));
        feeDto.setFeeTotalPrice(MoneyUtil.computePriceScale(Double.parseDouble(feePriceAll.get("feeTotalPrice").toString()),
                feeDto.getScale(),
                Integer.parseInt(feeDto.getDecimalPlace())));
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(MoneyUtil.computePriceScale(curFeePrice.doubleValue(), feeDto.getScale(), Integer.parseInt(feeDto.getDecimalPlace())) + "");
        //动态费用
        if ("4004".equals(computingFormula)
                && FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())
                && !FeeDto.STATE_FINISH.equals(feeDto.getState())
                && feeDto.getDeadlineTime() == null) {
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
        //考虑租金递增
        computeFeeSMOImpl.dealRentRate(feeDto);
    }

}
