package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.common.ICarInoutInnerServiceSMO;
import com.java110.intf.common.ICarInoutV1InnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.po.car.CarInoutPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "fee.payFeePreTempCarInout")
public class PayFeePreTempCarInoutCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;
    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求报文中未包含feeId节点");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(reqJson.getString("feeId"), "费用ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject paramObj) throws CmdException, ParseException {

        paramObj.put("cycles", 0);
        //添加单元信息
        addFeeTempDetail(paramObj, context);
        modifyTempFee(paramObj, context);
        modifyTempCarInout(paramObj, context);


        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId((String) paramObj.get("communityId"));
        smallWeChatDto.setAppId((String) paramObj.get("appId"));
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDtos.size() <= 0) {
            throw new IllegalArgumentException("支付失败,小区未配置小程序信息");
        }
        //指定支付小区
        if ("1000".equals(smallWeChatDtos.get(0).getObjType())) {
            paramObj.put("payAppId", smallWeChatDtos.get(0).getAppId());
            paramObj.put("payMchId", smallWeChatDtos.get(0).getMchId());
        }

        ResponseEntity responseEntity = new ResponseEntity<>(paramObj.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    public void addFeeTempDetail(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {

        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        if (!businessFeeDetail.containsKey("state")) {
            businessFeeDetail.put("state", "1400");
        }
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        feeDto = feeDtos.get(0);
        paramInJson.put("feeInfo", feeDto);
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeDto.getFeeTypeCd());
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(feeDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }
        feeConfigDto = feeConfigDtos.get(0);
        Date nowTime = new Date();

        long diff = nowTime.getTime() - feeDto.getStartTime().getTime();
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        double day = 0;
        double hour = 0;
        double min = 0;
        day = diff / nd;// 计算差多少天
        hour = diff % nd / nh + day * 24;// 计算差多少小时
        min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
        double money = 0.00;
        double newHour = hour;
        if (min > 0) { //一小时超过
            newHour += 1;
        }
        if (newHour <= 2) {
            money = Double.parseDouble(feeConfigDto.getAdditionalAmount());
        } else {
            BigDecimal lastHour = new BigDecimal(newHour - 2);
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            money = squarePrice.multiply(lastHour).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }
        double receivableAmount = money;
        businessFeeDetail.put("receivableAmount", receivableAmount);
        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessFeeDetail, PayFeeDetailPo.class);
        payFeeDetailPo.setPayOrderId(payFeeDetailPo.getDetailId());
        payFeeDetailPo.setCashierId("-1");
        payFeeDetailPo.setCashierName("系统收银");
        int flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);

        if (flag < 1) {
            throw new CmdException("保存明细失败");
        }
        paramInJson.put("receivableAmount", receivableAmount);
    }

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void modifyTempFee(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {

        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("amount", paramInJson.getString("receivableAmount"));
        feeMap.put("state", "2009001");
        businessFee.putAll(feeMap);
        PayFeePo payFeePo =  BeanConvertUtil.covertBean(businessFee,PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("保存失败");
        }
    }


    public void modifyTempCarInout(JSONObject reqJson, ICmdDataFlowContext context) {
        FeeDto feeDto = (FeeDto) reqJson.get("feeInfo");
        CarInoutDto tempCarInoutDto = new CarInoutDto();
        tempCarInoutDto.setCommunityId(reqJson.getString("communityId"));
        tempCarInoutDto.setInoutId(feeDto.getPayerObjId());
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(tempCarInoutDto);
        Assert.listOnlyOne(carInoutDtos, "根据费用信息反差车辆进场记录未查到 或查到多条");
        CarInoutDto carInoutDto = carInoutDtos.get(0);

        JSONObject businessCarInout = new JSONObject();
        businessCarInout.putAll(BeanConvertUtil.beanCovertMap(carInoutDto));
        businessCarInout.put("state", "100400");
        //计算 应收金额
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(businessCarInout,CarInoutPo.class);
       int flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);
        if (flag < 1) {
            throw new CmdException("保存失败");
        }
    }
}
