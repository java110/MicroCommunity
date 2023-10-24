package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeRuleBill.PayFeeRuleBillDto;
import com.java110.fee.feeMonth.IPayFeeMonth;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeRuleBill.PayFeeRuleBillPo;
import com.java110.po.reportFee.ReportOweFeePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "fee.deleteFee")
public class DeleteFeeCmd extends Cmd {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleBillV1InnerServiceSMO payFeeRuleBillV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含feeId");

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setFeeId(reqJson.getString("feeId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "未查询到费用信息 或查询到多条" + reqJson);


        String feeValidate = MappingCache.getValue("DELETE_FEE_VALIDATE");
        if ("ON".equals(feeValidate)) {
            return;
        }

        //判断是否已经存在 缴费记录
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        feeDetailDto.setFeeId(reqJson.getString("feeId"));
        feeDetailDto.setStates(new String[]{"1400", "1000"});
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        //Assert.listOnlyOne(feeDetailDtos, "存在缴费记录，不能取消，如果需要取消，请先退费");

        if (feeDetailDtos != null && feeDetailDtos.size() > 0) {
            throw new IllegalArgumentException("存在缴费记录，不能取消，如果需要取消，请先退费");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", reqJson.getString("feeId"));
        businessUnit.put("communityId", reqJson.getString("communityId"));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);

        int flag = payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);
        if (flag < 1) {
            throw new IllegalArgumentException("删除失败");
        }

        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessUnit, PayFeeDetailPo.class);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(BeanConvertUtil.covertBean(payFeeDetailPo, FeeDetailDto.class));
        if(feeDetailDtos != null && !feeDetailDtos.isEmpty()) {
            int flag2 = payFeeDetailV1InnerServiceSMOImpl.deletePayFeeDetailNew(payFeeDetailPo);
            if (flag2 < 1) {
                throw new IllegalArgumentException("删除失败");
            }
        }

        //todo 检查费用是否有账单数据 如果有直接删除
        PayFeeRuleBillDto payFeeRuleBillDto = new PayFeeRuleBillDto();
        payFeeRuleBillDto.setFeeId(payFeePo.getFeeId());
        payFeeRuleBillDto.setCommunityId(payFeePo.getCommunityId());
        List<PayFeeRuleBillDto> payFeeRuleBillDtos = payFeeRuleBillV1InnerServiceSMOImpl.queryPayFeeRuleBills(payFeeRuleBillDto);
        if(payFeeRuleBillDtos != null && !payFeeRuleBillDtos.isEmpty()) {
            PayFeeRuleBillPo payFeeRuleBillPo = new PayFeeRuleBillPo();
            payFeeRuleBillPo.setBillId(payFeeRuleBillDtos.get(0).getBillId());
            payFeeRuleBillPo.setCommunityId(payFeeRuleBillDtos.get(0).getCommunityId());
            payFeeRuleBillV1InnerServiceSMOImpl.deletePayFeeRuleBill(payFeeRuleBillPo);
        }
        // todo 删除欠费信息
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setFeeId(payFeePo.getFeeId());
        reportOweFeePo.setCommunityId(payFeePo.getCommunityId());
        reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

        //todo 离散的月
        payFeeMonthImpl.deleteFeeMonth(payFeePo.getFeeId(),payFeePo.getCommunityId());
    }
}
