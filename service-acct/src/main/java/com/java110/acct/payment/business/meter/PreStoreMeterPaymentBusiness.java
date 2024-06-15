package com.java110.acct.payment.business.meter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meter.MeterMachineDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 水电充值
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
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;


    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "machineId", "请求报文中未包含machineId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        JSONObject paramIn = new JSONObject();
        paramIn.put("machineId", reqJson.getString("machineId"));
        paramIn.put("communityId", reqJson.getString("communityId"));
        paramIn.put("page", 1);
        paramIn.put("row", 1);
        paramIn.put("iotApiCode", "listMeterMachineBmoImpl");
        ResultVo resultVo = iotInnerServiceSMOImpl.postIot(paramIn);
        JSONArray machines = (JSONArray) resultVo.getData();

        Assert.listOnlyOne(machines, "表不存在");

        String typeCd = machines.getJSONObject(0).getString("typeCd");
        String address = machines.getJSONObject(0).getString("address");
        String typeCdName = "";
        if (FeeConfigDto.FEE_TYPE_CD_WATER.equals(typeCd)) {
            typeCdName = "水费充值";
        } else if (FeeConfigDto.FEE_TYPE_CD_METER.equals(typeCd)) {
            typeCdName = "电费充值";
        } else {
            typeCdName = "煤气充值";
        }
        String userId = context.getReqHeaders().get("user-id");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setFeeTypeCd(typeCd);
        feeConfigDto.setComputingFormula("6006");
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException(typeCdName + "费用项公式设置错误，请选择 用量*单价+附加费");
        }

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(reqJson.getDoubleValue("receivedAmount"));
        paymentOrderDto.setName(typeCdName);
        paymentOrderDto.setUserId(userId);
        paymentOrderDto.setCycles("1");

        reqJson.put("receivableAmount", reqJson.getDoubleValue("receivedAmount"));
        reqJson.put("receivedAmount", reqJson.getDoubleValue("receivedAmount"));
        reqJson.put("configId", feeConfigDtos.get(0).getConfigId());
        reqJson.put("address", address);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {


        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "物业不存在");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        feeConfigDto.setComputingFormula("6006");
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (ListUtil.isNull(feeConfigDtos)) {
            throw new IllegalArgumentException("费用项公式设置错误，请选择 用量*单价+附加费");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setCommunityId(reqJson.getString("communityId"));
        payFeePo.setConfigId(reqJson.getString("configId"));
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

        //todo 计算用量
        BigDecimal receivedAmountDec = new BigDecimal(reqJson.getString("receivedAmount"));
        receivedAmountDec = receivedAmountDec.subtract(new BigDecimal(feeConfigDtos.get(0).getAdditionalAmount()));
        receivedAmountDec = receivedAmountDec.divide(new BigDecimal(feeConfigDtos.get(0).getSquarePrice()), 0, BigDecimal.ROUND_HALF_UP);
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_PROXY_CONSUMPTION, receivedAmountDec.doubleValue() + ""));
        feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_METER_ADDRESS, reqJson.getString("address")));

        //todo 查询房屋信息
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(feeConfigDtos.get(0).getCommunityId());
        roomDto.setRoomId(reqJson.getString("roomId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        if (!ListUtil.isNull(roomDtos)) {
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum()));
        }

        //todo 查询业主信息
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(reqJson.getString("roomId"));
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (!ListUtil.isNull(ownerRoomRelDtos)) {

            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                    payFeePo.getEndTime()));

            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_ID, ownerRoomRelDtos.get(0).getOwnerId()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerRoomRelDtos.get(0).getLink()));
            feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerRoomRelDtos.get(0).getOwnerName()));

        }

        if (!ListUtil.isNull(feeConfigDtos)) {
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
        payFeeDetailPo.setEndTime(DateUtil.getPreSecTime(payFeePo.getEndTime()));
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setRemark("手机端充值");
        payFeeDetailPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setState("1400");
        payFeeDetailPo.setPayableAmount(reqJson.getString("receivedAmount"));
        payFeeDetailPo.setPayOrderId(paymentOrderDto.getOrderId());
        payFeeDetailPo.setOpenInvoice("N");
        payFeeDetailPo.setDeductionAmount("0");
        payFeeDetailPo.setGiftAmount("0");
        payFeeDetailPo.setDiscountAmount("0");
        payFeeDetailPo.setLateAmount("0");
        payFeeDetailPo.setCashierName("手机缴费");
        payFeeDetailPo.setCashierId("-1");

        payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);

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
