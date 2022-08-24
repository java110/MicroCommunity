package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDetailDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.intf.common.ICarInoutDetailV1InnerServiceSMO;
import com.java110.intf.common.ICarInoutPaymentV1InnerServiceSMO;
import com.java110.intf.common.ICarInoutV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.CarInoutDetailPo;
import com.java110.po.car.CarInoutPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.carInoutPayment.CarInoutPaymentPo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：车辆进出场
 * 服务编码：machineTranslate.machineUploadCarLog
 * 请求路劲：/app/machineTranslate.machineUploadCarLog
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machineTranslate.machineUploadCarLog")
public class MachineUploadCarLogCmd extends Cmd {

    public static final int CAR_TYPE_MONTH = 0; //月租车
    public static final int CAR_TYPE_SUB = 1; //成员车辆
    public static final int CAR_TYPE_TEMP = 2; //临时车辆
    public static final int CAR_TYPE_NO_DATA = 3; //没有数据

    public static final String TEMP_CAR_OWNER = "临时车车主";

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;

    @Autowired
    private ICarInoutDetailV1InnerServiceSMO carInoutDetailV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private ICarInoutPaymentV1InnerServiceSMO carInoutPaymentV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //是否是临时车
        int tempCar = CAR_TYPE_MONTH;


        //查询设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

        //查询车辆
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        //说明是临时车
        if (ownerCarDtos == null || ownerCarDtos.size() == 0) {
            tempCar = CAR_TYPE_NO_DATA;
        } else {
            reqJson.put("carId", ownerCarDtos.get(0).getCarId());
            if (OwnerCarDto.CAR_TYPE_TEMP.equals(ownerCarDtos.get(0).getCarTypeCd())) {
                tempCar = CAR_TYPE_TEMP;
            }
        }

        //进场处理
        if (MachineDto.DIRECTION_IN.equals(machineDtos.get(0).getDirection())) {
            carIn(reqJson, machineDtos.get(0), tempCar);
        } else {
            carOut(reqJson, machineDtos.get(0), tempCar);
        }

    }

    /**
     * 车辆出场
     *
     * @param reqJson
     * @param machineDto
     * @param tempCar
     */
    private void carOut(JSONObject reqJson, MachineDto machineDto, int tempCar) {

        CarInoutDto carInoutDto = new CarInoutDto();
        carInoutDto.setCommunityId(reqJson.getString("communityId"));
        carInoutDto.setCarNum(reqJson.getString("carNum"));
        carInoutDto.setPaId(machineDto.getLocationObjId());
        carInoutDto.setStates(new String[]{
                CarInoutDto.STATE_IN,
                CarInoutDto.STATE_PAY,
                CarInoutDto.STATE_REPAY
        });
        List<CarInoutDto> carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(carInoutDto);

        if (carInoutDtos == null || carInoutDtos.size() < 1) {
            //可能车辆异常情况 没有 进场 补充进场记录
            reqJson.put("inTime", reqJson.getString("outTime"));
            carIn(reqJson, machineDto, tempCar);
            carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(carInoutDto);
        }

        //保存出场明细
        CarInoutDetailPo carInoutDetailPo = new CarInoutDetailPo();
        carInoutDetailPo.setCarInout(CarInoutDetailDto.CAR_INOUT_OUT);
        carInoutDetailPo.setCarNum(reqJson.getString("carNum"));
        carInoutDetailPo.setCommunityId(reqJson.getString("communityId"));
        carInoutDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        carInoutDetailPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutDetailPo.setMachineCode(machineDto.getMachineCode());
        carInoutDetailPo.setMachineId(machineDto.getMachineId());
        carInoutDetailPo.setPaId(machineDto.getLocationObjId());
        int flag = carInoutDetailV1InnerServiceSMOImpl.saveCarInoutDetail(carInoutDetailPo);

        if (flag < 1) {
            throw new CmdException("保存出记录明细失败");
        }

        //将状态更新为 出场状态

        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setPaId(carInoutDtos.get(0).getPaId());
        carInoutPo.setOutTime(reqJson.getString("outTime"));
        carInoutPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutPo.setCommunityId(carInoutDtos.get(0).getCommunityId());
        carInoutPo.setState(CarInoutDto.STATE_OUT);
        flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);

        if (flag < 1) {
            throw new CmdException("更新出场时间失败");
        }

        //写支付记录
        CarInoutPaymentPo carInoutPaymentPo = new CarInoutPaymentPo();
        carInoutPaymentPo.setPaymentId(GenerateCodeFactory.getGeneratorId("10"));
        carInoutPaymentPo.setCommunityId(carInoutDtos.get(0).getCommunityId());
        carInoutPaymentPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutPaymentPo.setPaId(carInoutDtos.get(0).getPaId());
        carInoutPaymentPo.setPayCharge(reqJson.getString("payCharge"));
        carInoutPaymentPo.setPayType(reqJson.getString("payType"));
        carInoutPaymentPo.setRealCharge(reqJson.getString("realCharge"));
        flag = carInoutPaymentV1InnerServiceSMOImpl.saveCarInoutPayment(carInoutPaymentPo);
        if (flag < 1) {
            throw new CmdException("更新出场时间失败");
        }

        //月租车
        if (tempCar != CAR_TYPE_NO_DATA && tempCar != CAR_TYPE_TEMP) {
            return;
        }

        //如果有费用 则缴费
        boolean hasFee = hasFeeAndPayFee(carInoutDtos.get(0), reqJson, carInoutPo, carInoutPaymentPo);

        double realCharge = Double.parseDouble(carInoutPaymentPo.getRealCharge());

        //有费用 或者 缴费为0 时结束
        if (hasFee || realCharge == 0) {
            return;
        }
        // 判断是否存在 临时车 虚拟业主
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setOwnerFlag(OwnerDto.OWNER_FLAG_FALSE);
        ownerDto.setName(TEMP_CAR_OWNER);
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        JSONObject paramIn = new JSONObject();
        paramIn.put("inTime", carInoutDtos.get(0).getInTime());
        paramIn.put("carId", reqJson.getString("carId"));
        paramIn.put("communityId", carInoutDtos.get(0).getCommunityId());
        paramIn.put("inoutId", carInoutDtos.get(0).getInoutId());
        paramIn.put("ownerId", ownerDtos.get(0).getMemberId());
        saveTempCarFee(paramIn, machineDto);

        //再去缴费
        hasFeeAndPayFee(carInoutDtos.get(0), reqJson, carInoutPo, carInoutPaymentPo);
    }

    private boolean hasFeeAndPayFee(CarInoutDto carInoutDto, JSONObject reqJson, CarInoutPo carInoutPo, CarInoutPaymentPo carInoutPaymentPo) {

        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(carInoutPo.getCommunityId());
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_CAR_INOUT_ID);
        feeAttrDto.setValue(carInoutPo.getInoutId());
        feeAttrDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFeeByAttr(feeAttrDto);
        if (feeDtos == null || feeDtos.size() < 1) {
            return false;
        }
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setState(FeeDto.STATE_FINISH);
        payFeePo.setFeeId(feeDtos.get(0).getFeeId());
        payFeePo.setEndTime(carInoutPo.getOutTime());
        payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
        int flag = feeInnerServiceSMOImpl.updateFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setPrimeRate("1.00");
        FeeDto feeDto = feeDtos.get(0);
        payFeeDetailPo.setStartTime(DateUtil.getFormatTimeString(feeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setEndTime(carInoutPo.getOutTime());
        payFeeDetailPo.setCommunityId(carInoutDto.getCommunityId());
        payFeeDetailPo.setCycles("1");
        payFeeDetailPo.setReceivableAmount(carInoutPaymentPo.getPayCharge());
        payFeeDetailPo.setReceivedAmount(carInoutPaymentPo.getRealCharge());
        payFeeDetailPo.setFeeId(feeDto.getFeeId());

        flag = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }
        return true;
    }

    /**
     * 车辆入场
     *
     * @param reqJson
     * @param machineDto
     * @param tempCar
     */
    private void carIn(JSONObject reqJson, MachineDto machineDto, int tempCar) {
        String state = CarInoutDto.STATE_IN;
        //进场失败记录
        if(reqJson.containsKey("state") && "5".equals(reqJson.getString("state"))){
            state = CarInoutDto.STATE_IN_FAIL;
        }

        //保存
        CarInoutPo carInoutPo = new CarInoutPo();
        carInoutPo.setCarNum(reqJson.getString("carNum"));
        carInoutPo.setCommunityId(reqJson.getString("communityId"));
        carInoutPo.setInoutId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        carInoutPo.setInTime(reqJson.getString("inTime"));
        carInoutPo.setState(state);
        carInoutPo.setPaId(machineDto.getLocationObjId());
        int flag = carInoutV1InnerServiceSMOImpl.saveCarInout(carInoutPo);

        if (flag < 1) {
            throw new CmdException("保存入记录失败");
        }
        reqJson.put("inoutId", carInoutPo.getInoutId());

        //保存明细
        CarInoutDetailPo carInoutDetailPo = new CarInoutDetailPo();
        carInoutDetailPo.setCarInout(CarInoutDetailDto.CAR_INOUT_IN);
        carInoutDetailPo.setCarNum(reqJson.getString("carNum"));
        carInoutDetailPo.setCommunityId(reqJson.getString("communityId"));
        carInoutDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        carInoutDetailPo.setInoutId(carInoutPo.getInoutId());
        carInoutDetailPo.setMachineCode(machineDto.getMachineCode());
        carInoutDetailPo.setMachineId(machineDto.getMachineId());
        carInoutDetailPo.setPaId(machineDto.getLocationObjId());
        carInoutDetailPo.setRemark(reqJson.getString("remark"));
        flag = carInoutDetailV1InnerServiceSMOImpl.saveCarInoutDetail(carInoutDetailPo);

        if (flag < 1) {
            throw new CmdException("保存入记录明细失败");
        }
        //月租车
        if (tempCar != CAR_TYPE_NO_DATA && tempCar != CAR_TYPE_TEMP) {
            return;
        }

        //不是自己的道闸跳过
        if (!MachineDto.MACHINE_TYPE_CAR.equals(machineDto.getMachineTypeCd())) {
            return;
        }


        // 判断是否存在 临时车 虚拟业主
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setOwnerFlag(OwnerDto.OWNER_FLAG_FALSE);
        ownerDto.setName(TEMP_CAR_OWNER);
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            saveTempOwner(reqJson, machineDto);
        } else {
            reqJson.put("ownerId", ownerDtos.get(0).getMemberId());
        }
        if (tempCar == CAR_TYPE_NO_DATA) {
            saveTempCar(reqJson, machineDto);
        }

        saveTempCarFee(reqJson, machineDto);
    }

    /**
     * 创建临时车费用
     *
     * @param reqJson
     * @param machineDto
     */
    private void saveTempCarFee(JSONObject reqJson, MachineDto machineDto) {

        //创建费用
        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setCommunityId(reqJson.getString("communityId"));
        tempCarFeeConfigDto.setPaId(machineDto.getLocationObjId());
        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);

        if (tempCarFeeConfigDtos == null || tempCarFeeConfigDtos.size() < 1) { // 停车场未配置收费规则 则不创建费用
            return;
        }

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "小区成员不存在");
        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setEndTime(reqJson.getString("inTime"));
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(reqJson.getString("communityId"));
        payFeePo.setConfigId(tempCarFeeConfigDto.getFeeConfigId());
        payFeePo.setPayerObjId(reqJson.getString("carId"));
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        payFeePo.setUserId("-1");
        payFeePo.setIncomeObjId(communityMemberDtos.get(0).getMemberId());
        payFeePo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_PARKING);
        payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setAmount("-1");
        payFeePo.setBatchId("-1");
        //payFeePo.setStartTime(importRoomFee.getStartTime());
        payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeePos.add(payFeePo);
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
        feeAttrPo.setValue(reqJson.getString("inTime"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
        feeAttrPo.setValue(reqJson.getString("ownerId"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
        feeAttrPo.setValue(TEMP_CAR_OWNER);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
        feeAttrPo.setValue("11111111111");
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(reqJson.getString("communityId"));
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_CAR_INOUT_ID);
        feeAttrPo.setValue(reqJson.getString("inoutId"));
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);
        int flag = feeInnerServiceSMOImpl.saveFee(payFeePos);
        if (flag < 1) {
            throw new CmdException("保存临时车费用失败");
        }
        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);
        if (flag < 1) {
            throw new CmdException("保存临时车费用属性失败");
        }
    }

    /**
     * 保存业主
     *
     * @param reqJson
     * @param machineDto
     */
    private void saveTempOwner(JSONObject reqJson, MachineDto machineDto) {

        OwnerPo ownerPo = new OwnerPo();
        ownerPo.setSex("1");
        ownerPo.setCommunityId(reqJson.getString("communityId"));
        ownerPo.setMemberId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId));
        reqJson.put("ownerId", ownerPo.getMemberId());
        ownerPo.setLink("11111111111");
        ownerPo.setUserId("-1");
        ownerPo.setAge("1");
        ownerPo.setIdCard("111111111111111111");
        ownerPo.setName(TEMP_CAR_OWNER);
        ownerPo.setOwnerId(ownerPo.getMemberId());
        ownerPo.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerPo.setRemark("虚拟业主 物联网同步");
        ownerPo.setbId("-1");
        ownerPo.setOwnerFlag(OwnerDto.OWNER_FLAG_FALSE);
        ownerPo.setState(OwnerDto.STATE_FINISH);
        int flag = buildingOwnerV1InnerServiceSMOImpl.saveBuildingOwner(ownerPo);
        if (flag < 1) {
            throw new CmdException("保存临时车主");
        }
    }

    /**
     * 保存车辆
     *
     * @param reqJson
     * @param machineDto
     */
    private void saveTempCar(JSONObject reqJson, MachineDto machineDto) {

        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        ownerCarPo.setCarId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
        ownerCarPo.setCommunityId(machineDto.getCommunityId());
        ownerCarPo.setPsId("-1");
        ownerCarPo.setMemberId(ownerCarPo.getCarId());
        ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_CD_TEMP);
        ownerCarPo.setCarType("9901");
        ownerCarPo.setCarBrand("未知");
        ownerCarPo.setCarColor("未知");
        ownerCarPo.setCarNum(reqJson.getString("carNum"));
        ownerCarPo.setOwnerId(reqJson.getString("ownerId"));
        ownerCarPo.setRemark("临时车 物联网同步");
        ownerCarPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        ownerCarPo.setUserId("-1");
        int flag = ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);

        if (flag < 1) {
            throw new CmdException("保存临时车失败");
        }

        reqJson.put("carId", ownerCarPo.getCarId());
    }
}
