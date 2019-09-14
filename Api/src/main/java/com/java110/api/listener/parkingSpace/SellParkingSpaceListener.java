package com.java110.api.listener.parkingSpace;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.*;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.common.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.parkingSpace.IParkingSpaceInnerServiceSMO;
import com.java110.dto.FeeConfigDto;
import com.java110.dto.ParkingSpaceDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName SaveParkingSpaceListener
 * @Description 保存小区楼信息
 * @Author wuxw
 * @Date 2019/4/26 14:51
 * @Version 1.0
 * add by wuxw 2019/4/26
 **/

@Java110Listener("sellParkingSpaceListener")
public class SellParkingSpaceListener extends AbstractServiceApiDataFlowListener {


    private static Logger logger = LoggerFactory.getLogger(SellParkingSpaceListener.class);

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SELL_PARKING_SPACE;
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

        String feeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId);
        paramObj.put("feeId", feeId);

        //添加小区楼
        businesses.add(sellParkingSpace(paramObj));

        businesses.add(modifyParkingSpaceState(paramObj));

        //计算 费用信息
        computeFeeInfo(paramObj);
        //添加物业费用信息
        businesses.add(addParkingSpaceFee(paramObj, dataFlowContext));

        businesses.add(addFeeDetail(paramObj, dataFlowContext));


        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 添加小区楼信息
     * <p>
     * * name:'',
     * *                 age:'',
     * *                 link:'',
     * *                 sex:'',
     * *                 remark:''
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private JSONObject sellParkingSpace(JSONObject paramInJson) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessOwnerCar = new JSONObject();
        businessOwnerCar.putAll(paramInJson);
        businessOwnerCar.put("carId", "-1");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessOwnerCar", businessOwnerCar);

        return business;
    }

    /**
     * 修改停车位状态信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private JSONObject modifyParkingSpaceState(JSONObject paramInJson) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", this.isHireParkingSpace(paramInJson) ? "H" : "S");
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessParkingSpace", businessParkingSpace);

        return business;
    }


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addParkingSpaceFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {



        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 2);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", paramInJson.getString("feeId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", paramInJson.getString("amount") );
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", paramInJson.getString("endTime"));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", paramInJson.getString("psId"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFee", businessUnit);

        return business;
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 3);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", "-1");
        businessFeeDetail.put("primeRate", "1.00");
        businessFeeDetail.put("cycles", paramInJson.getString("cycles"));
        businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeDetail", businessFeeDetail);

        return business;
    }

    /**
     * 数据校验
     * <p>
     * name:'',
     * age:'',
     * link:'',
     * sex:'',
     * remark:''
     *
     * @param paramIn "communityId": "7020181217000001",
     *                "memberId": "3456789",
     *                "memberTypeCd": "390001200001"
     */
    private void validate(String paramIn) {

        Assert.jsonObjectHaveKey(paramIn, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(paramIn, "ownerId", "请求报文中未包含ownerId");
        Assert.jsonObjectHaveKey(paramIn, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(paramIn, "carBrand", "请求报文中未包含carBrand");
        Assert.jsonObjectHaveKey(paramIn, "carType", "请求报文中未包含carType");
        Assert.jsonObjectHaveKey(paramIn, "carColor", "未包含carColor");
        Assert.jsonObjectHaveKey(paramIn, "psId", "未包含psId");
        Assert.jsonObjectHaveKey(paramIn, "storeId", "未包含storeId");
        Assert.jsonObjectHaveKey(paramIn, "receivedAmount", "未包含receivedAmount");
        Assert.jsonObjectHaveKey(paramIn, "sellOrHire", "未包含sellOrHire");

        JSONObject paramInObj = JSONObject.parseObject(paramIn);
        Assert.hasLength(paramInObj.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramInObj.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(paramInObj.getString("psId"), "psId不能为空");
        Assert.isMoney(paramInObj.getString("receivedAmount"), "不是有效的实收金额");

        if(!"H".equals(paramInObj.getString("sellOrHire"))
                && !"S".equals(paramInObj.getString("sellOrHire"))){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请求报文中sellOrFire值错误 ，出售为S 出租为H");
        }
    }

    /**
     * 校验 是否是车位出租
     * @param paramObj
     * @return
     */
    private boolean isHireParkingSpace(JSONObject paramObj){
        if("H".equals(paramObj.getString("sellOrHire"))){
            return true;
        }
        return false;
    }

    /**
     * 计算费用信息
     * @param paramInJson 传入数据字段
     */
    private void computeFeeInfo(JSONObject paramInJson){

        //根据停车位ID查询是地上还是地下停车位
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(paramInJson.getString("psId"));
        parkingSpaceDto.setCommunityId(paramInJson.getString("communityId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未找到或找到多条车位信息");
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);
        paramInJson.put("parkingSpaceDto", parkingSpaceDto);

        // 计算feeTypeCd

        String feeTypeCd = "1001".equals(parkingSpaceDto.getTypeCd())
                ? (this.isHireParkingSpace(paramInJson)
                        ? FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE
                        :FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE)
                : (this.isHireParkingSpace(paramInJson)
                        ? FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE
                        :FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE);

        paramInJson.put("feeTypeCd", feeTypeCd);

        //计算 receivableAmount


        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        double receivableAmount = isHireParkingSpace(paramInJson) ? Double.parseDouble(feeConfigDto.getAdditionalAmount())
                * Double.parseDouble(paramInJson.getString("cycles")) : Double.parseDouble(feeConfigDto.getAdditionalAmount());

        paramInJson.put("receivableAmount", receivableAmount);

        //计算 amount
        String amount = isHireParkingSpace(paramInJson) ? "-1.00" : String.valueOf(receivableAmount);
        paramInJson.put("amount", amount);

        //计算 cycles
        String cycles = isHireParkingSpace(paramInJson) ? paramInJson.getString("cycles") : "1";
        paramInJson.put("cycles", cycles);

        //计算结束时间

        String endTime = isHireParkingSpace(paramInJson) ? DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A) : "2038-01-01 00:00:00";
        paramInJson.put("endTime", endTime);

    }


    @Override
    public int getOrder() {
        return 0;
    }

    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }

    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }
}
