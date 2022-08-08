package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.entity.center.AppService;
import com.java110.intf.common.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.po.car.CarInoutPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.constant.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.machine.MachineResDataVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "machineTranslate.machineRoadGateOpen")
public class MachineRoadGateOpenCmd extends BaseMachineCmd {
    private static Logger logger = LoggerFactory.getLogger(MachineRoadGateOpenCmd.class);

    private static final String MACHINE_DIRECTION_IN = "3306"; // 进入

    private static final String MACHINE_DIRECTION_OUT = "3307"; //出去

    private static final String HIRE_SELL_OUT = "hireSellOut"; // 出租或出售车辆出场

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

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutDetailV1InnerServiceSMO carInoutDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        super.validateMachineHeader(event, reqJson);

        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含车牌号");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
//JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getReqHeaders();
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
    private void dealCarOut(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, MachineDto machineDto, String communityId) {

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
    private boolean judgeCarOutTimeOut(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
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
        //添加单元信息
        modifyCarInout(reqJson, tmpCarInoutDto, "100600", null);
        addCarInoutFee(reqJson, tmpCarInoutDto.getCommunityId(), DateUtil.getFormatTimeString(tmpFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));

        reqJson.put("feeRestartTime", tmpFeeDto.getEndTime());

        return true;
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addCarInoutFee(JSONObject paramInJson,  String communityId) {
        addCarInoutFee(paramInJson, communityId, DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
    }

    public void addCarInoutFee(JSONObject paramInJson, String communityId, String startTime) {
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityId);
        communityMemberDto.setMemberTypeCd(CommunityMemberTypeConstant.PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        String storeId = "-1";
        if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
            storeId = communityMemberDtos.get(0).getMemberId();
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeConfigDto.setIsDefault("T");
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查到费用配置信息，查询多条数据");
        }

        feeConfigDto = feeConfigDtos.get(0);

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        businessUnit.put("configId", feeConfigDto.getConfigId());
        businessUnit.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        businessUnit.put("incomeObjId", storeId);
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", startTime);
        businessUnit.put("endTime", DateUtil.getLastTime()); // 临时车将结束时间刷成2038年
        businessUnit.put("communityId", communityId);
        businessUnit.put("payerObjId", paramInJson.getString("inoutId"));
        businessUnit.put("payerObjType", "9999");
        businessUnit.put("feeFlag", "2006012"); // 一次性费用
        businessUnit.put("state", "2008001"); // 收费中
        businessUnit.put("userId", "-1");
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("保存费用失败");
        }
    }


    public void modifyCarInout(JSONObject reqJson, CarInoutDto carInoutDto, String state, String endTime) {

        JSONObject businessCarInout = new JSONObject();
        businessCarInout.putAll(BeanConvertUtil.beanCovertMap(carInoutDto));
        businessCarInout.put("state", state);
        businessCarInout.put("outTime", endTime);
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(businessCarInout, CarInoutPo.class);
        int flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);

        if (flag < 1) {
            throw new CmdException("修改车辆进出记录");
        }
    }


    /**
     * 处理车辆未完成支付出场处理
     *
     * @param event
     * @param context
     * @param tmpCarInoutDto
     * @param machineDto
     */
    private void dealCarOutIncomplete(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
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

    private void modifyCarInoutInfo(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto) {
        modifyCarInoutInfo(event, context, reqJson, tmpCarInoutDto, machineDto, "");
    }

    private void modifyCarInoutInfo(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, CarInoutDto tmpCarInoutDto, MachineDto machineDto, String from) {

        //添加单元信息
        modifyCarInout(reqJson, tmpCarInoutDto);
        reqJson.put("inoutId", tmpCarInoutDto.getInoutId());
        addCarInoutDetail(reqJson, tmpCarInoutDto.getCommunityId(), machineDto);
        if (HIRE_SELL_OUT.equals(from)) {
            modifyCarInoutFee(reqJson, tmpCarInoutDto.getCommunityId(), machineDto);

        }

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addCarInoutDetail(JSONObject paramInJson, String communityId, MachineDto machineDto) {

        JSONObject businessCarInoutDetail = new JSONObject();
        businessCarInoutDetail.put("carNum", paramInJson.getString("carNum"));
        businessCarInoutDetail.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInoutDetail.put("communityId", communityId);
        businessCarInoutDetail.put("machineId", machineDto.getMachineId());
        businessCarInoutDetail.put("machineCode", machineDto.getMachineCode());
        businessCarInoutDetail.put("carInout", machineDto.getDirection());
        businessCarInoutDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        paramInJson.put("detailId",businessCarInoutDetail.getString("detailId"));
        CarInoutDetailPo carInoutDetailPo = BeanConvertUtil.covertBean(businessCarInoutDetail, CarInoutDetailPo.class);
        int flag = carInoutDetailV1InnerServiceSMOImpl.saveCarInoutDetail(carInoutDetailPo);
        if(flag < 1){
            throw new CmdException("保存明细");
        }

    }

    public void modifyCarInout(JSONObject reqJson, CarInoutDto carInoutDto) {
        modifyCarInout(reqJson, carInoutDto, "100500", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
    }


    /**
     * 处理车辆进入
     *
     * @param reqJson
     * @param machineDto
     * @param communityId
     */
    private void dealCarIn(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson, MachineDto machineDto, String communityId) {
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
        context.getReqHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();


        //添加单元信息
        addCarInout(reqJson, communityId);
        addCarInoutDetail(reqJson, communityId, machineDto);
        addCarInoutFee(reqJson, communityId);

        context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_SUCCESS, "成功"));
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addCarInout(JSONObject paramInJson,  String communityId) {

        if (!paramInJson.containsKey("inoutId") || "-1".equals(paramInJson.getString("inoutId"))) {
            paramInJson.put("inoutId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inoutId));
        }
        JSONObject businessCarInout = new JSONObject();
        businessCarInout.put("carNum", paramInJson.getString("carNum"));
        businessCarInout.put("inoutId", paramInJson.getString("inoutId"));
        businessCarInout.put("communityId", communityId);
        businessCarInout.put("state", "100300");
        businessCarInout.put("inTime", DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        CarInoutPo carInoutPo = BeanConvertUtil.covertBean(businessCarInout, CarInoutPo.class);
        int flag = carInoutV1InnerServiceSMOImpl.saveCarInout(carInoutPo);
        if(flag < 1){
            throw new CmdException("保存明细失败");
        }
    }


    /**
     * 出租或出售 车辆出场
     *
     * @param reqJson
     * @param communityId
     * @param machineDto
     * @return
     */
    private void modifyCarInoutFee(JSONObject reqJson, String communityId, MachineDto machineDto) {

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
            return;
        }

        FeeDto tmpFeeDto = feeDtos.get(0);

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(BeanConvertUtil.beanCovertMap(tmpFeeDto));
        businessUnit.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("state", "2009001"); // 收费中

        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);

        if(flag < 1){
            throw new CmdException("修改费用失败");
        }

    }
}
