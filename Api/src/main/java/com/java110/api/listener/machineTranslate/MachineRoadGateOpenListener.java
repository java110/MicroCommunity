package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.ICarBlackWhiteInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.ICarInoutInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerCarInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.hardwareAdapation.CarBlackWhiteDto;
import com.java110.dto.hardwareAdapation.CarInoutDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.CommunityMemberTypeConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.machine.MachineResDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(MachineRoadGateOpenListener.class);


    private static final String MACHINE_DIRECTION_IN = "3306"; // 进入

    private static final String MACHINE_DIRECTION_OUT = "3307"; //出去

    private static final String HIRE_SELL_OUT = "hireSellOut"; // 出租或出售车辆出场

    private static final String CAR_BLACK = "1111"; // 车辆黑名单
    private static final String CAR_WHITE = "2222"; // 车辆白名单

    @Autowired
    private IMachineTranslateBMO machineTranslateBMOImpl;

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

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validateMachineHeader(event, reqJson);

        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含车牌号");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        //JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");
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
            responseEntity = MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "该设备【" + machineCode + "】未在该小区【" + communityId + "】注册");
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

        //标识支付完成，检查是否为支付超时

        if (judgeCarOutTimeOut(event, context, reqJson, tmpCarInoutDto, machineDto)) {
            JSONObject data = computeHourAndMoney(tmpCarInoutDto.getCommunityId(), new Date(), reqJson.getDate("feeRestartTime"));
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "支付已超时，请重新支付", data));
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
     * 判断车辆出场是否已经超时
     *
     * @param event
     * @param context
     * @param reqJson
     * @param tmpCarInoutDto
     * @param machineDto
     * @return
     */
    private boolean judgeCarOutTimeOut(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(machineDto.getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        String storeId = "-1";
        if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
            storeId = communityMemberDtos.get(0).getMemberId();
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(machineDto.getCommunityId());
        feeDto.setPayerObjId(reqJson.getString("inoutId"));
        feeDto.setIncomeObjId(storeId);
        feeDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeDto.setState("2009001");
        feeDto.setFeeFlag("2006012");
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return false;
        }

        FeeDto tmpFeeDto = feeDtos.get(0);


        long dffMin = new Date().getTime() - tmpFeeDto.getEndTime().getTime();

        if (dffMin < 15 * 1000 * 60) {
            return false;
        }

        //重新插入 一条 收费记录 收费

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        AppService service = event.getAppService();
        //添加单元信息
        businesses.add(machineTranslateBMOImpl.modifyCarInout(reqJson, context, tmpCarInoutDto, "100600", null));
        businesses.add(machineTranslateBMOImpl.addCarInoutFee(reqJson, context, tmpCarInoutDto.getCommunityId(), DateUtil.getFormatTimeString(tmpFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A)));

        ResponseEntity<String> responseEntity = machineTranslateBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
        reqJson.put("feeRestartTime", tmpFeeDto.getEndTime());

        return true;
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
        if (ownerCarDtos != null && ownerCarDtos.size() > 0) {
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
                modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto, HIRE_SELL_OUT);
                context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功", data));
                return;
            }
        }

        //计算缴费金额
        Date inTime = null;
        try {
            inTime = DateUtil.getDateFromString(tmpCarInoutDto.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
        } catch (Exception e) {
            logger.error("格式化出错", e);
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "后台异常，数据格式错误"));
            return;
        }

        JSONObject data = computeHourAndMoney(tmpCarInoutDto.getCommunityId(), nowTime, inTime);

        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, "车辆未支付，请先支付", data));
    }

    private JSONObject computeHourAndMoney(String communityId, Date nowTime, Date inTime) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityId);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);
        long diff = nowTime.getTime() - inTime.getTime();
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
            money = Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount());
        } else {
            double lastHour = newHour - 2;
            money = lastHour * Double.parseDouble(tmpFeeConfigDto.getSquarePrice()) + Double.parseDouble(tmpFeeConfigDto.getAdditionalAmount());
        }

        JSONObject data = new JSONObject();
        data.put("money", money);//缴费金额
        data.put("hour", new Double(hour).intValue());//停车时间
        data.put("min", new Double(min).intValue());//停车时间
        return data;
    }

    private void modifyCarInoutInfo(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
        modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto, "");
    }

    private void modifyCarInoutInfo(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto, String from) {
        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        AppService service = event.getAppService();
        //添加单元信息
        businesses.add(machineTranslateBMOImpl.modifyCarInout(reqJson, context, tmpCarInoutDto));
        reqJson.put("inoutId", tmpCarInoutDto.getInoutId());
        businesses.add(machineTranslateBMOImpl.addCarInoutDetail(reqJson, context, tmpCarInoutDto.getCommunityId(), machineDto));
        if (HIRE_SELL_OUT.equals(from)) {
            JSONObject tmpModifyCarInoutFee = modifyCarInoutFee(reqJson, context, tmpCarInoutDto.getCommunityId(), machineDto);
            if (tmpModifyCarInoutFee != null) {
                businesses.add(tmpModifyCarInoutFee);
            }
        }

        ResponseEntity<String> responseEntity = machineTranslateBMOImpl.callService(context, service.getServiceCode(), businesses);
        context.setResponseEntity(responseEntity);
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
        businesses.add(machineTranslateBMOImpl.addCarInout(reqJson, context, communityId));
        businesses.add(machineTranslateBMOImpl.addCarInoutDetail(reqJson, context, communityId, machineDto));
        businesses.add(machineTranslateBMOImpl.addCarInoutFee(reqJson, context, communityId));


        ResponseEntity<String> responseEntity = machineTranslateBMOImpl.callService(context, service.getServiceCode(), businesses);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, responseEntity.getBody()));
            return;
        }
        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功"));
    }


    /**
     * 出租或出售 车辆出场
     *
     * @param reqJson
     * @param context
     * @param communityId
     * @param machineDto
     * @return
     */
    private JSONObject modifyCarInoutFee(JSONObject reqJson, DataFlowContext context, String communityId, MachineDto machineDto) {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityId);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        String storeId = "-1";
        if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
            storeId = communityMemberDtos.get(0).getMemberId();
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setPayerObjId(reqJson.getString("inoutId"));
        feeDto.setIncomeObjId(storeId);
        feeDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeDto.setState("2008001");
        feeDto.setFeeFlag("2006012");
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return null;
        }

        FeeDto tmpFeeDto = feeDtos.get(0);

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(tmpFeeDto));
        businessUnit.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("state", "2009001"); // 收费中
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFee", businessUnit);

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
