package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.entity.center.AppService;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName PayFeeListener
 * @Description TODO 交费通知侦听
 * @Author wuxw
 * @Date 2019/6/3 13:46
 * @Version 1.0
 * add by wuxw 2019/6/3
 **/
@Java110Listener("rentingPayFeeConfirmListener")
public class RentingPayFeeConfirmListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(RentingPayFeeConfirmListener.class);

    @Autowired
    private IFeeBMO feeBMOImpl;

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

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_RENTING_PAY_CONFIRM_PRE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        String oId = paramObj.getString("oId");
        String money = paramObj.getString("money");
        String feeName = paramObj.getString("feeName");
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.O_ID, oId);
        RentingPoolDto rentingPoolDto = BeanConvertUtil.covertBean(paramObj, RentingPoolDto.class);

        JSONArray businesses = new JSONArray();

        //物业 收取费用
        double propertyRate = Double.parseDouble(rentingPoolDto.getPropertySeparateRate());
        BigDecimal serviceDec = new BigDecimal(propertyRate);
        BigDecimal payMoney = new BigDecimal(money);
        double receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId("-1");
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(receivableAmount + "");
        payFeeDetailPo.setReceivedAmount(receivableAmount + "");
        payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
        //添加单元信息
        businesses.add(feeBMOImpl.addSimpleFeeDetail(payFeeDetailPo, dataFlowContext));

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(rentingPoolDto.getCommunityId());
        communityMemberDto.setMemberTypeCd("390001200002");//查询物业
        communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "物业信息查询有误");

        String propertyId = communityMemberDtos.get(0).getMemberId();
        PayFeePo payFeePo = new PayFeePo();
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
        businesses.add(feeBMOImpl.addSimpleFee(payFeePo, dataFlowContext));


        //代理商分成
        //物业 收取费用
        double proxyRate = Double.parseDouble(rentingPoolDto.getProxySeparateRate());
        serviceDec = new BigDecimal(proxyRate);
        payMoney = new BigDecimal(money);
        receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId("-2");
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(receivableAmount + "");
        payFeeDetailPo.setReceivedAmount(receivableAmount + "");
        payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
        //添加单元信息
        businesses.add(feeBMOImpl.addSimpleFeeDetail(payFeeDetailPo, dataFlowContext));

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
        businesses.add(feeBMOImpl.addSimpleFee(payFeePo, dataFlowContext));

        //运营分成
        //物业 收取费用
        double adminRate = Double.parseDouble(rentingPoolDto.getAdminSeparateRate());
        serviceDec = new BigDecimal(adminRate);
        payMoney = new BigDecimal(money);
        receivableAmount = serviceDec.multiply(payMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId("-3");
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(receivableAmount + "");
        payFeeDetailPo.setReceivedAmount(receivableAmount + "");
        payFeeDetailPo.setCommunityId(rentingPoolDto.getCommunityId());
        //添加单元信息
        businesses.add(feeBMOImpl.addSimpleFeeDetail(payFeeDetailPo, dataFlowContext));

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
        businesses.add(feeBMOImpl.addSimpleFee(payFeePo, dataFlowContext));

        ResponseEntity<String> responseEntity = feeBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);
        dataFlowContext.setResponseEntity(responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setRentingId(rentingPoolDto.getRentingId());
        rentingPoolPo.setCommunityId(rentingPoolDto.getCommunityId());
        rentingPoolPo.setState(RentingPoolDto.STATE_TO_PAY.equals(rentingPoolDto.getState()) ? RentingPoolDto.STATE_OWNER_TO_PAY : RentingPoolDto.STATE_APPLY_AGREE);
        rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
    }


    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "oId", "请求报文中未包含订单信息");
        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("oId"), "订单信息不能为空");

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
