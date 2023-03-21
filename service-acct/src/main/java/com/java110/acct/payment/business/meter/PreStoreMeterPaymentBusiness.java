package com.java110.acct.payment.business.meter;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 欠费缴费
 */
@Service("preStoreMeter")
public class PreStoreMeterPaymentBusiness implements IPaymentBusiness {


    private final static Logger logger = LoggerFactory.getLogger(PreStoreMeterPaymentBusiness.class);

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;


    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "machineId", "请求报文中未包含machineId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setMachineId(reqJson.getString("machineId"));
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

        Assert.listOnlyOne(meterMachineDtos, "表不存在");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        feeConfigDto.setConfigId(meterMachineDtos.get(0).getFeeConfigId());
        feeConfigDto.setComputingFormula("6006");
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项公式设置错误，请选择 用量*单价+附加费");

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(reqJson.getDoubleValue("receivedAmount"));
        paymentOrderDto.setName(reqJson.getString("feeName"));

        reqJson.put("receivableAmount", reqJson.getDoubleValue("receivedAmount"));
        reqJson.put("receivedAmount", reqJson.getDoubleValue("receivedAmount"));
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setMachineId(reqJson.getString("machineId"));
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

        Assert.listOnlyOne(meterMachineDtos, "表不存在");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        feeConfigDto.setConfigId(meterMachineDtos.get(0).getFeeConfigId());
        feeConfigDto.setComputingFormula("6006");
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项公式设置错误，请选择 用量*单价+附加费");

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(feeConfigDtos.get(0).getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "物业不存在");

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        payFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        payFeePo.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setAmount(reqJson.getString("receivedAmount"));
        payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeePo.setIncomeObjId(communityMemberDtos.get(0).getMemberId());
        payFeePo.setBatchId("-1");
        payFeePo.setState(FeeDto.STATE_FINISH);

        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setPayerObjId(reqJson.getString("roomId"));
        payFeePo.setUserId("-1");
        payFeePo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<PayFeePo> payFeePos = new ArrayList<>();
        payFeePos.add(payFeePo);
        feeInnerServiceSMOImpl.saveFee(payFeePos);

        //查询业主信息
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
            List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                    payFeePo.getEndTime()));

            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_ID, ownerRoomRelDtos.get(0).getOwnerId()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerRoomRelDtos.get(0).getLink()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerRoomRelDtos.get(0).getOwnerName()));
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);
        }

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setCommunityId(feeConfigDtos.get(0).getCommunityId());
        payFeeDetailPo.setReceivedAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setReceivableAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_WECHAT);
        payFeeDetailPo.setFeeId(payFeePo.getFeeId());
        payFeeDetailPo.setStartTime(payFeePo.getStartTime());
        payFeeDetailPo.setEndTime(payFeePo.getEndTime());
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setRemark("手机端充值");
        payFeeDetailPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setState("1400");
        payFeeDetailPo.setPayableAmount(reqJson.getString("receivedAmount"));
        feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);

    }

    public FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        return feeAttrPo;

    }


}
