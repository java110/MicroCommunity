package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "fee.rentingPayFeeConfirm")
public class RentingPayFeeConfirmCmd extends Cmd {


    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "oId", "请求报文中未包含订单信息");

        Assert.hasLength(reqJson.getString("oId"), "订单信息不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject paramObj) throws CmdException, ParseException {
        String oId = paramObj.getString("oId");
        String money = paramObj.getString("money");
        String feeName = paramObj.getString("feeName");
        context.getReqHeaders().put(CommonConstant.O_ID, oId);
        RentingPoolDto rentingPoolDto = BeanConvertUtil.covertBean(paramObj, RentingPoolDto.class);
        int flag = 0;

        JSONArray businesses = new JSONArray();
        BigDecimal serviceDec = null;
        BigDecimal payMoney = null;
        double receivableAmount = 0.0;
        PayFeeDetailPo payFeeDetailPo = null;
        CommunityMemberDto communityMemberDto = null;
        List<CommunityMemberDto> communityMemberDtos = null;
        PayFeePo payFeePo = null;
        //物业 收取费用
        double propertyRate = Double.parseDouble(rentingPoolDto.getPropertySeparateRate());
        if (propertyRate > 0) {
            serviceDec = new BigDecimal(propertyRate);
            payMoney = new BigDecimal(money);
            receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeeDetailPo.setPrimeRate("1.0");
            payFeeDetailPo.setCycles("1");
            payFeeDetailPo.setReceivableAmount(receivableAmount + "");
            payFeeDetailPo.setReceivedAmount(receivableAmount + "");
            payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeeDetailPo.setPayOrderId(payFeeDetailPo.getDetailId());
            payFeeDetailPo.setCashierId("-1");
            payFeeDetailPo.setCashierName("系统收银");
            payFeeDetailPo.setState("1400");
            //添加单元信息
            flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }

            communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(rentingPoolDto.getCommunityId());
            communityMemberDto.setMemberTypeCd("390001200002");//查询物业
            communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
            communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

            Assert.listOnlyOne(communityMemberDtos, "物业信息查询有误");

            String propertyId = communityMemberDtos.get(0).getMemberId();
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(payFeeDetailPo.getFeeId());
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setIncomeObjId(propertyId);
            payFeePo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeePo.setUserId("-1");
            payFeePo.setPayerObjId(rentingPoolDto.getRentingId());
            payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_SYSTEM);
            payFeePo.setAmount(receivableAmount + "");
            payFeePo.setState(FeeDto.STATE_FINISH);
            payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_RENTING);
            payFeePo.setConfigId(FeeConfigDto.CONFIG_ID_RENTING);
            flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }
        }

        //代理商分成
        //物业 收取费用
        double proxyRate = Double.parseDouble(rentingPoolDto.getProxySeparateRate());
        if (proxyRate > 0) {
            serviceDec = new BigDecimal(proxyRate);
            payMoney = new BigDecimal(money);
            receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeeDetailPo.setPrimeRate("1.0");
            payFeeDetailPo.setCycles("1");
            payFeeDetailPo.setReceivableAmount(receivableAmount + "");
            payFeeDetailPo.setReceivedAmount(receivableAmount + "");
            payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeeDetailPo.setPayOrderId(payFeeDetailPo.getDetailId());
            payFeeDetailPo.setCashierId("-1");
            payFeeDetailPo.setCashierName("系统收银");
            //添加单元信息
            flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }

            communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(rentingPoolDto.getCommunityId());
            communityMemberDto.setMemberTypeCd("390001200003");//查询代理商
            communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
            communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

            Assert.listOnlyOne(communityMemberDtos, "代理商信息查询有误");

            String proxyId = communityMemberDtos.get(0).getMemberId();
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(payFeeDetailPo.getFeeId());
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setIncomeObjId(proxyId);
            payFeePo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeePo.setUserId("-1");
            payFeePo.setPayerObjId(rentingPoolDto.getRentingId());
            payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_SYSTEM);
            payFeePo.setAmount(receivableAmount + "");
            payFeePo.setState(FeeDto.STATE_FINISH);
            payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_RENTING);
            payFeePo.setConfigId(FeeConfigDto.CONFIG_ID_RENTING);
            flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }
        }
        //运营分成
        //物业 收取费用
        double adminRate = Double.parseDouble(rentingPoolDto.getAdminSeparateRate());

        if (adminRate > 0) {
            serviceDec = new BigDecimal(adminRate);
            payMoney = new BigDecimal(money);
            receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            payFeeDetailPo = new PayFeeDetailPo();
            payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeeDetailPo.setPrimeRate("1.0");
            payFeeDetailPo.setCycles("1");
            payFeeDetailPo.setReceivableAmount(receivableAmount + "");
            payFeeDetailPo.setReceivedAmount(receivableAmount + "");
            payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeeDetailPo.setPayOrderId(payFeeDetailPo.getDetailId());
            payFeeDetailPo.setCashierId("-1");
            payFeeDetailPo.setCashierName("系统收银");
            //添加单元信息
            flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }

            communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(rentingPoolDto.getCommunityId());
            communityMemberDto.setMemberTypeCd("390001200000");//查询代理商
            communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
            communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

            Assert.listOnlyOne(communityMemberDtos, "代理商信息查询有误");

            String adminId = communityMemberDtos.get(0).getMemberId();
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(payFeeDetailPo.getFeeId());
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setIncomeObjId(adminId);
            payFeePo.setCommunityId(rentingPoolDto.getCommunityId());
            payFeePo.setUserId("-1");
            payFeePo.setPayerObjId(rentingPoolDto.getRentingId());
            payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_SYSTEM);
            payFeePo.setAmount(receivableAmount + "");
            payFeePo.setState(FeeDto.STATE_FINISH);
            payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_RENTING);
            payFeePo.setConfigId(FeeConfigDto.CONFIG_ID_RENTING);
            flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("添加费用异常");
            }
        }

        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setRentingId(rentingPoolDto.getRentingId());
        rentingPoolPo.setCommunityId(rentingPoolDto.getCommunityId());
        rentingPoolPo.setState(RentingPoolDto.STATE_TO_PAY.equals(rentingPoolDto.getState()) ? RentingPoolDto.STATE_OWNER_TO_PAY : RentingPoolDto.STATE_APPLY_AGREE);
        rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
    }


}
