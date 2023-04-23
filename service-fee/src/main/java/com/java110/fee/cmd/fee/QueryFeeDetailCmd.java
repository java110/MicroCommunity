package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiFeeDetailDataVo;
import com.java110.vo.api.ApiFeeDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询缴费历史
 */
@Java110Cmd(serviceCode = "fee.queryFeeDetail")
public class QueryFeeDetailCmd extends Cmd {

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //获取开始时间
        if (!StringUtil.isEmpty(reqJson.getString("startTime"))) {
            String startTime = reqJson.getString("startTime") + " 00:00:00";
            reqJson.put("startTime", startTime);
        } else {
            reqJson.put("startTime", null);
        }
        //获取结束时间
        if (!StringUtil.isEmpty(reqJson.getString("endTime"))) {
            String endTime = reqJson.getString("endTime") + " 23:59:59";
            reqJson.put("endTime", endTime);
        } else {
            reqJson.put("endTime", null);
        }
        //查询总记录数
        ApiFeeDetailVo apiFeeDetailVo = new ApiFeeDetailVo();
        FeeDetailDto feeDetailDto = BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class);

        int total = feeDetailInnerServiceSMOImpl.queryFeeDetailsCount(feeDetailDto);
        apiFeeDetailVo.setTotal(total);
        if (total > 0) {
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class));
            List<FeeDetailDto> feeDetailList = new ArrayList<>();
            for (FeeDetailDto feeDetail : feeDetailDtos) {
                //获取状态
                String state = feeDetail.getState();
                if (!StringUtil.isEmpty(state) && (state.equals("1300") || state.equals("1100") || state.equals("1200"))) { //退费单、已退费、退费失败状态
                    //获取周期
                    String cycles = feeDetail.getCycles();
                    if (!StringUtil.isEmpty(cycles) && cycles.contains("-")) {
                        feeDetail.setCycles(cycles.substring(1));
                    }
                    //获取应收金额
                    String receivableAmount = feeDetail.getReceivableAmount();
                    if (!StringUtil.isEmpty(receivableAmount) && receivableAmount.contains("-")) {
                        feeDetail.setReceivableAmount(receivableAmount.substring(1));
                    }
                    //获取实收金额
                    String receivedAmount = feeDetail.getReceivedAmount();
                    if (!StringUtil.isEmpty(receivedAmount) && receivedAmount.contains("-")) {
                        feeDetail.setReceivedAmount(receivedAmount.substring(1));
                    }
                }
                FeeAccountDetailDto feeAccountDetailDto = new FeeAccountDetailDto();
                feeAccountDetailDto.setDetailId(feeDetail.getDetailId());
                List<FeeAccountDetailDto> feeAccountDetailDtos = feeAccountDetailServiceSMOImpl.queryFeeAccountDetails(feeAccountDetailDto);
                feeDetail.setFeeAccountDetailDtoList(feeAccountDetailDtos);
                PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
                payFeeDetailDiscountDto.setDetailId(feeDetail.getDetailId());
                List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscounts(payFeeDetailDiscountDto);
                feeDetail.setPayFeeDetailDiscountDtoList(payFeeDetailDiscountDtos);
                feeDetailList.add(feeDetail);
            }
            List<ApiFeeDetailDataVo> feeDetails = BeanConvertUtil.covertBeanList(feeDetailList, ApiFeeDetailDataVo.class);

            //reFreshCreateTime(feeDetails, feeDetailDtos);

            apiFeeDetailVo.setFeeDetails(feeDetails);
        }
        int row = reqJson.getInteger("row");
        apiFeeDetailVo.setRecords((int) Math.ceil((double) total / (double) row));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeDetailVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 创建时间
     *
     * @param feeDetails    返回对象
     * @param feeDetailDtos 数据传输对象
     */
    private void reFreshCreateTime(List<ApiFeeDetailDataVo> feeDetails, List<FeeDetailDto> feeDetailDtos) {
        for (ApiFeeDetailDataVo feeDetailDataVo : feeDetails) {
            for (FeeDetailDto feeDetailDto : feeDetailDtos) {
                if (feeDetailDataVo.getDetailId().equals(feeDetailDto.getDetailId())) {
                    feeDetailDataVo.setCreateTime(DateUtil.getFormatTimeString(feeDetailDto.getCreateTime(), DateUtil.DATE_FORMATE_STRING_A));
                }
            }
        }
    }
}
