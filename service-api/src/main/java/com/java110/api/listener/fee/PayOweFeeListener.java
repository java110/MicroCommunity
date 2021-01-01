package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.repair.RepairDto;
import com.java110.entity.center.AppService;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PayFeeListener
 * @Description TODO 缴费侦听
 * @Author wuxw
 * @Date 2019/6/3 13:46
 * @Version 1.0
 * add by wuxw 2019/6/3
 **/
@Java110Listener("payOweFeeListener")
public class PayOweFeeListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(PayOweFeeListener.class);

    @Autowired
    private IFeeBMO feeBMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountBMO payFeeDetailDiscountBMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_PAY_OWE_FEE;
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
        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        //添加单元信息
        List<FeeReceiptPo> feeReceiptPos = new ArrayList<>();
        List<FeeReceiptDetailPo> feeReceiptDetailPos = new ArrayList<>();
        JSONArray fees = paramObj.getJSONArray("fees");
        JSONObject feeObj = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feeObj = fees.getJSONObject(feeIndex);
            feeObj.put("communityId", paramObj.getString("communityId"));
            feeObj.put("remark", paramObj.getString("remark"));
            getFeeReceiptDetailPo(dataFlowContext, feeObj, businesses, feeReceiptDetailPos, feeReceiptPos);
        }

        ResponseEntity<String> responseEntity = feeBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);
        dataFlowContext.setResponseEntity(responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        //这里只是写入 收据表，暂不考虑 事务一致性问题，就算写入失败 也只是影响 收据打印，如果 贵公司对 收据要求 比较高，不能有失败的情况 请加入事务管理
        feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetails(feeReceiptDetailPos);


        feeReceiptInnerServiceSMOImpl.saveFeeReceipts(feeReceiptPos);

        dataFlowContext.setResponseEntity(ResultVo.createResponseEntity(feeReceiptPos));
    }

    private void getFeeReceiptDetailPo(DataFlowContext dataFlowContext, JSONObject paramObj, JSONArray businesses, List<FeeReceiptDetailPo> feeReceiptDetailPos, List<FeeReceiptPo> feeReceiptPos) {
        paramObj.put("primeRate", "6");
        //paramObj.put("remark", "微信支付");
        businesses.add(feeBMOImpl.addOweFeeDetail(paramObj, dataFlowContext, feeReceiptDetailPos, feeReceiptPos));
        businesses.add(feeBMOImpl.modifyOweFee(paramObj, dataFlowContext));

        //判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(paramObj.getString("communityId"));
        feeAttrDto.setFeeId(paramObj.getString("feeId"));
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        //修改 派单状态
        if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
            JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
            business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
            business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
            business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
            repairPoolPo.setCommunityId(paramObj.getString("communityId"));
            repairPoolPo.setState(RepairDto.STATE_APPRAISE);
            business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(RepairPoolPo.class.getSimpleName(), BeanConvertUtil.beanCovertMap(repairPoolPo));
            businesses.add(business);
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
        JSONObject reqJson = JSONObject.parseObject(paramIn);
        Assert.hasKey(reqJson, "fees", "请求报文中未包含费用信息");

        JSONArray fees = reqJson.getJSONArray("fees");

        JSONObject feeObject = null;

        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feeObject = fees.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(feeObject, "feeId", "未包含费用信息");
            Assert.hasKeyAndValue(feeObject, "startTime", "未包含开始时间");
            Assert.hasKeyAndValue(feeObject, "endTime", "未包含结束时间");
            Assert.hasKeyAndValue(feeObject, "receivedAmount", "未包含实收金额");
        }

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
