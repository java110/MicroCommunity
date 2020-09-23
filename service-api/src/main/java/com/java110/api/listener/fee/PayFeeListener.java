package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.repair.RepairDto;
import com.java110.entity.center.AppService;
import com.java110.intf.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

/**
 * @ClassName PayFeeListener
 * @Description TODO 缴费侦听
 * @Author wuxw
 * @Date 2019/6/3 13:46
 * @Version 1.0
 * add by wuxw 2019/6/3
 **/
@Java110Listener("payFeeListener")
public class PayFeeListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(PayFeeListener.class);

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


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_PAY_FEE;
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
        FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        businesses.add(feeBMOImpl.addFeeDetail(paramObj, dataFlowContext, feeReceiptDetailPo, feeReceiptPo));
        businesses.add(feeBMOImpl.modifyFee(paramObj, dataFlowContext));


        //为停车费单独处理
        if (paramObj.containsKey("carPayerObjType") && FeeDto.PAYER_OBJ_TYPE_CAR.equals(paramObj.getString("carPayerObjType"))) {
            Date feeEndTime = (Date) paramObj.get("carFeeEndTime");
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCommunityId(paramObj.getString("communityId"));
            ownerCarDto.setCarId(paramObj.getString("carPayerObjId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            //车位费用续租
            if (ownerCarDtos != null && ownerCarDtos.size() == 1 && ownerCarDtos.get(0).getEndTime().getTime() < feeEndTime.getTime()) {
                JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
                business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR);
                business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
                business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
                OwnerCarPo ownerCarPo = new OwnerCarPo();
                ownerCarPo.setCarId(ownerCarDtos.get(0).getCarId());
                ownerCarPo.setEndTime(DateUtil.getFormatTimeString(feeEndTime, DateUtil.DATE_FORMATE_STRING_A));
                business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(OwnerCarPo.class.getSimpleName(), BeanConvertUtil.beanCovertMap(ownerCarPo));
                businesses.add(business);
            }
        }

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

        ResponseEntity<String> responseEntity = feeBMOImpl.callService(dataFlowContext, service.getServiceCode(), businesses);
        dataFlowContext.setResponseEntity(responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }

        //这里只是写入 收据表，暂不考虑 事务一致性问题，就算写入失败 也只是影响 收据打印，如果 贵公司对 收据要求 比较高，不能有失败的情况 请加入事务管理
        feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
        feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);
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

        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramInObj.getString("cycles"), "周期不能为空");
        Assert.hasLength(paramInObj.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(paramInObj.getString("feeId"), "费用ID不能为空");

        //判断是否 费用状态为缴费结束
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInObj.getString("feeId"));
        feeDto.setCommunityId(paramInObj.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "传入费用ID错误");

        feeDto = feeDtos.get(0);

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            throw new IllegalArgumentException("收费已经结束，不能再缴费");
        }

        Date endTime = feeDto.getEndTime();

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(paramInObj.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (feeConfigDtos != null && feeConfigDtos.size() == 1) {
            try {
                Date configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);

                Date newDate = DateUtil.stepMonth(endTime, paramInObj.getInteger("cycles") - 1);

                if (newDate.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("缴费周期超过 缴费结束时间");
                }

            } catch (Exception e) {
                logger.error("比较费用日期失败", e);
            }
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
