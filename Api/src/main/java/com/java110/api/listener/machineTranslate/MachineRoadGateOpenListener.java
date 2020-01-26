package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.ICarBlackWhiteInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.ICarInoutInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.dto.FeeConfigDto;
import com.java110.dto.FeeDto;
import com.java110.dto.hardwareAdapation.CarBlackWhiteDto;
import com.java110.dto.hardwareAdapation.CarInoutDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.machine.MachineResDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MachineRoadGateOpenListener
 * @Description 道闸开门侦听类
 * @Author wuxw
 * @Date 2020/1/25 21:50
 * @Version 1.0
 * add by wuxw 2020/1/25
 **/
@Java110Listener("machineRoadGateOpenListener")
public class MachineRoadGateOpenListener extends BaseMachineListener {

    private static final String MACHINE_DIRECTION_IN = "3306"; // 进入

    private static final String MACHINE_DIRECTION_OUT = "3307"; //出去

    private static final String CAR_BLACK = "1111"; // 车辆黑名单
    private static final String CAR_WHITE = "2222"; // 车辆白名单

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICarInoutInnerServiceSMO carInoutInnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO carInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validateMachineHeader(event, reqJson);

        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含车牌号");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        String communityId = reqHeader.get("communityId");
        String machineCode = reqHeader.get("machinecode");
        HttpHeaders headers = new HttpHeaders();
        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }
        //根据设备编码查询 设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(machineCode);
        machineDto.setCommunityId(communityId);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + machineCode + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        //设备方向
        String direction = machineDtos.get(0).getDirection();

        //进入
        if (MACHINE_DIRECTION_IN.equals(direction)) {
            dealCarIn(event, context, reqJson, machineDtos.get(0), communityId);
        } else {
            dealCarOut(event, context, reqJson, machineDtos.get(0), communityId);
        }
    }

    /**
     * 车辆出场
     *
     * @param event
     * @param context
     * @param reqJson
     * @param machineDto
     * @param communityId
     */
    private void dealCarOut(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, MachineDto machineDto, String communityId) {

        //首先查询是否车辆有进场
        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setStates(new String[]{"100300", "100400", "100600"});
        carInoutDto.setCommunityId(communityId);
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        List<CarInoutDto> carInoutDtos = carInoutInnerServiceSMOImpl.queryCarInouts(carInoutDto);

        if (carInoutDtos == null || carInoutDtos.size() < 1) {//数据有问题， 直接出场
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "数据有问题，未查到入场记录，直接放行"));
            return;
        }

        CarInoutDto tmpCarInoutDto = carInoutDtos.get(0);
        reqJson.put("inoutId", tmpCarInoutDto.getInoutId());

        if (!"100400".equals(tmpCarInoutDto.getState())) {

            dealCarOutIncomplete(event, context, reqJson, tmpCarInoutDto, machineDto);
            return;
        }

        modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto);
        ResponseEntity<String> responseEntity = context.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "后台处理数据异常" + responseEntity.getBody()));
            return;
        }
        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功"));
    }

    /**
     * 处理车辆未完成支付出场处理
     *
     * @param event
     * @param context
     * @param tmpCarInoutDto
     * @param machineDto
     */
    private void dealCarOutIncomplete(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
        //判断车辆是否在白名单中
        String carNum = reqJson.getString("carNum");
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setCommunityId(tmpCarInoutDto.getCommunityId());
        carBlackWhiteDto.setCarNum(carNum);
        carBlackWhiteDto.setBlackWhite(CAR_WHITE);
        int count = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);
        if (count > 0) {
            modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto);
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "白名单中直接放行"));
            return;
        }

        //判断车辆出租和出售 时间是否到期
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(carNum);
        ownerCarDto.setCommunityId(tmpCarInoutDto.getCommunityId());
        List<OwnerCarDto> ownerCarDtos = carInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        Date nowTime = new Date();
        if(ownerCarDtos != null && ownerCarDtos.size() >0) {
            //这里获取最新的一条记录 我们认为 一个小区中一辆车不能同时有两个车位
            OwnerCarDto tmpOwnerCarDto = ownerCarDtos.get(0);
            FeeDto feeDto = new FeeDto();
            feeDto.setPayerObjId(tmpOwnerCarDto.getPsId());
            feeDto.setCommunityId(tmpCarInoutDto.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

            FeeDto tmpFeeDto = feeDtos.get(0);

            Date endTime = tmpFeeDto.getEndTime();

            long betweenTime = (endTime.getTime() - nowTime.getTime());

            if (betweenTime > 0) {
                long day = betweenTime / (60 * 60 * 24 * 1000);
                JSONObject data = new JSONObject();
                data.put("day", day);//还剩余多少天
                modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto);
                context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功", data));
                return;
            }
        }

        //计算缴费金额

        Date inTime = tmpCarInoutDto.getInTime();

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(tmpCarInoutDto.getCommunityId());
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);
        Double hour = Math.ceil((nowTime.getTime() - inTime.getTime()) / (60 * 60 * 1000));
        double money = 0.00;
        if (hour <= 2) {
            money = Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount());
        } else {
            double lastHour = hour - 2;
            money = lastHour * Double.parseDouble(tmpFeeConfigDto.getSquarePrice()) + Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount());
        }

        JSONObject data = new JSONObject();
        data.put("money", money);//缴费金额
        data.put("hour", hour);//停车时间

        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "车辆未支付，请先支付", data));
    }

    private void modifyCarInoutInfo(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        AppService service = event.getAppService();
        //添加单元信息
        businesses.add(modifyCarInout(reqJson, context, tmpCarInoutDto));
        reqJson.put("inoutId", tmpCarInoutDto.getInoutId());
        businesses.add(addCarInoutDetail(reqJson, context, tmpCarInoutDto.getCommunityId(), machineDto));
        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());
        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());
        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);
        context.setResponseEntity(responseEntity);
    }

    private Object modifyCarInout(JSONObject reqJson, DataFlowContext context, CarInoutDto carInoutDto) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_INOUT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCarInout = new JSONObject();
        businessCarInout.putAll(BeanConvertUtil.beanCovertMap(carInoutDto));
        businessCarInout.put("state", "100500");
        businessCarInout.put("outTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCarInout", businessCarInout);
        return business;
    }

    /**
     * 处理车辆进入
     *
     * @param reqJson
     * @param machineDto
     * @param communityId
     */
    private void dealCarIn(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, MachineDto machineDto, String communityId) {
        //车辆是否黑名单 车辆
        String carNum = reqJson.getString("carNum");
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setCommunityId(communityId);
        carBlackWhiteDto.setCarNum(carNum);
        carBlackWhiteDto.setBlackWhite(CAR_BLACK);
        int count = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);
        if (count > 0) {
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, carNum + "被加入黑名单，无法放行"));
            return;
        }

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        businesses.add(addCarInout(reqJson, context, communityId));
        businesses.add(addCarInoutDetail(reqJson, context, communityId, machineDto));

        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, responseEntity.getBody()));
            return;
        }
        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功"));
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId) {

        paramInJson.put("inoutId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inoutId));
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCarInout = new JSONObject();
        businessCarInout.put("carNum", paramInJson.getString("carNum"));
        businessCarInout.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInout.put("communityId", communityId);
        businessCarInout.put("state", "100300");
        businessCarInout.put("inTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCarInout", businessCarInout);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addCarInoutDetail(JSONObject paramInJson, DataFlowContext dataFlowContext, String communityId, MachineDto machineDto) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCarInoutDetail = new JSONObject();
        businessCarInoutDetail.put("carNum", paramInJson.getString("carNum"));
        businessCarInoutDetail.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInoutDetail.put("communityId", communityId);
        businessCarInoutDetail.put("machineId", machineDto.getMachineId());
        businessCarInoutDetail.put("machineCode", machineDto.getMachineCode());
        businessCarInoutDetail.put("carInout", machineDto.getDirection());
        businessCarInoutDetail.put("detailId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCarInoutDetail", businessCarInoutDetail);
        return business;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_ROAD_GATE_OPEN;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
