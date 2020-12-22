package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.app.AppDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.feeDiscount.ComputeDiscountDto;
import com.java110.entity.center.AppService;
import com.java110.entity.order.Orders;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
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
 * @Description TODO 预交费侦听
 * @Author wuxw
 * @Date 2019/6/3 13:46
 * @Version 1.0
 * add by wuxw 2019/6/3
 **/
@Java110Listener("payFeePreListener")
public class PayFeePreListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(PayFeePreListener.class);

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
    private IPayFeeDetailDiscountBMO payFeeDetailDiscountBMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_PAY_FEE_PRE;
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

        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        //判断是否有折扣情况
        judgeDiscount(paramObj);

        String appId = event.getDataFlowContext().getAppId();

        if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {  //微信小程序支付
            paramObj.put("primeRate", "5");
            paramObj.put("remark", "线上小程序支付");
        } else if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) {  //微信公众号支付
            paramObj.put("primeRate", "6");
            paramObj.put("remark", "线上公众号支付");
        }

        //添加单元信息
        businesses.add(feeBMOImpl.addFeePreDetail(paramObj, dataFlowContext));
        businesses.add(feeBMOImpl.modifyPreFee(paramObj, dataFlowContext));

        double discountPrice = paramObj.getDouble("discountPrice");
        if (discountPrice > 0) {
            addDiscount(paramObj, businesses, dataFlowContext);
        }


        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.ORDER_PROCESS, Orders.ORDER_PROCESS_ORDER_PRE_SUBMIT);
        ResponseEntity<String> responseEntity = feeBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        //这里调整为实收金额
        paramOut.put("receivableAmount", paramObj.getString("receivableAmount"));
        paramOut.put("receivedAmount", paramObj.getString("receivedAmount"));
        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    private void judgeDiscount(JSONObject paramObj) {
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(paramObj.getString("communityId"));
        feeDetailDto.setFeeId(paramObj.getString("feeId"));
        feeDetailDto.setCycles(paramObj.getString("cycles"));
        feeDetailDto.setRow(20);
        feeDetailDto.setPage(1);
        List<ComputeDiscountDto> computeDiscountDtos = feeDiscountInnerServiceSMOImpl.computeDiscount(feeDetailDto);

        if (computeDiscountDtos == null || computeDiscountDtos.size() < 1) {
            paramObj.put("discountPrice", 0.0);
            return;
        }
        BigDecimal discountPrice = new BigDecimal(0);
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            discountPrice = discountPrice.add(new BigDecimal(computeDiscountDto.getDiscountPrice()));
        }

        paramObj.put("discountPrice", discountPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        paramObj.put("computeDiscountDtos", computeDiscountDtos);
    }

    private void addDiscount(JSONObject paramObj, JSONArray businesses, DataFlowContext dataFlowContext) {
        List<ComputeDiscountDto> computeDiscountDtos = (List<ComputeDiscountDto>) paramObj.get("computeDiscountDtos");
        JSONObject discountBusiness = null;
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            if (computeDiscountDto.getDiscountPrice() <= 0) {
                continue;
            }
            JSONObject paramIn = new JSONObject();
            paramIn.put("discountPrice", computeDiscountDto.getDiscountPrice());
            paramIn.put("discountId", computeDiscountDto.getDiscountId());
            paramIn.put("detailId", paramObj.getString("detailId"));
            paramIn.put("communityId", paramObj.getString("communityId"));
            paramIn.put("feeId", paramObj.getString("feeId"));
            discountBusiness = payFeeDetailDiscountBMOImpl.addPayFeeDetailDiscount(paramObj,
                    paramIn, dataFlowContext);
            if (discountBusiness != null) {
                businesses.add(discountBusiness);
            }
        }
    }

    /**
     * 数据校验
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(paramIn, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(paramIn, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(paramIn, "feeId", "请求报文中未包含feeId节点");
        Assert.jsonObjectHaveKey(paramIn, "appId", "请求报文中未包含appId节点");

        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramInObj.getString("cycles"), "周期不能为空");
        Assert.hasLength(paramInObj.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(paramInObj.getString("feeId"), "费用ID不能为空");
        Assert.hasLength(paramInObj.getString("appId"), "appId不能为空");


    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IFeeInnerServiceSMO getFeeInnerServiceSMOImpl() {
        return feeInnerServiceSMOImpl;
    }

    public void setFeeInnerServiceSMOImpl(IFeeInnerServiceSMO feeInnerServiceSMOImpl) {
        this.feeInnerServiceSMOImpl = feeInnerServiceSMOImpl;
    }

    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
