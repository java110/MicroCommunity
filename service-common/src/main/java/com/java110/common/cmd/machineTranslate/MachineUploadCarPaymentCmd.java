package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.carInoutPayment.CarInoutPaymentDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
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
import com.java110.po.car.CarInoutPo;
import com.java110.po.carInoutPayment.CarInoutPaymentPo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：车辆支付通知
 * 服务编码：machineTranslate.machineUploadCarLog
 * 请求路劲：/app/machineTranslate.machineUploadCarLog
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machineTranslate.machineUploadCarPaymentCmd")
public class MachineUploadCarPaymentCmd extends Cmd {

    public static final String TEMP_CAR_OWNER = "临时车车主";

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private ICarInoutV1InnerServiceSMO carInoutV1InnerServiceSMOImpl;

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

    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;

    /**
     * data.put("carNum", carInoutDto.getCarNum());
     * data.put("machineCode", carInoutDto.getMachineCode());
     * data.put("communityId", communityDtos.get(0).getExtCommunityId());
     * data.put("payCharge", carInoutDto.getPayCharge());
     * data.put("realCharge", carInoutDto.getRealCharge());
     * data.put("payType", carInoutDto.getPayType());
     *
     * @param event              事件对象
     * @param cmdDataFlowContext 请求报文数据
     * @param reqJson
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");
        Assert.hasKeyAndValue(reqJson, "payCharge", "必填，请填写应收费用");
        Assert.hasKeyAndValue(reqJson, "realCharge", "必填，请填写实收费用");
        Assert.hasKeyAndValue(reqJson, "payType", "必填，请填写支付类型");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //查询设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("communityId"));
        machineDto.setMachineTypeCd(MachineDto.MACHINE_TYPE_CAR);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

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
            throw new CmdException("没有待支付记录");
        }

        //写支付记录
        CarInoutPaymentPo carInoutPaymentPo = new CarInoutPaymentPo();
        carInoutPaymentPo.setPaymentId(GenerateCodeFactory.getGeneratorId("10"));
        carInoutPaymentPo.setCommunityId(reqJson.getString("communityId"));
        carInoutPaymentPo.setInoutId(carInoutDtos.get(0).getInoutId());
        carInoutPaymentPo.setPaId(carInoutDtos.get(0).getPaId());
        carInoutPaymentPo.setPayCharge(reqJson.getString("payCharge"));
        carInoutPaymentPo.setPayType(reqJson.getString("payType"));
        carInoutPaymentPo.setRealCharge(reqJson.getString("realCharge"));
        int flag = carInoutPaymentV1InnerServiceSMOImpl.saveCarInoutPayment(carInoutPaymentPo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }

        CarInoutPo carInoutPo = updateCarInoutState(reqJson,machineDto,carInoutDtos.get(0));

        //如果有费用 则缴费
        boolean hasFee = hasFeeAndPayFee(carInoutPo, carInoutPaymentPo);

        double realCharge = Double.parseDouble(carInoutPaymentPo.getRealCharge());

        //有费用 或者 缴费为0 时结束
        if (hasFee || realCharge == 0) {
            return;
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarType(OwnerCarDto.CAR_TYPE_TEMP);
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
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
        paramIn.put("carNum", reqJson.getString("carNum"));
        paramIn.put("carId", ownerCarDtos.get(0).getCarId());
        paramIn.put("communityId", carInoutDtos.get(0).getCommunityId());
        paramIn.put("inoutId", carInoutDtos.get(0).getInoutId());
        paramIn.put("ownerId", ownerDtos.get(0).getMemberId());
        saveTempCarFee(paramIn, machineDto, carInoutDtos.get(0));

        //再去缴费
        hasFeeAndPayFee(carInoutPo, carInoutPaymentPo);
    }

    private CarInoutPo updateCarInoutState(JSONObject reqJson,MachineDto machineDto,CarInoutDto carInoutDto) {
        int flag;

        String requestId = DistributedLock.getLockUUID();
        String key = "updateInoutState_" + carInoutDto.getInoutId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            CarInoutDto newCarInoutDto = new CarInoutDto();
            newCarInoutDto.setCommunityId(reqJson.getString("communityId"));
            newCarInoutDto.setCarNum(reqJson.getString("carNum"));
            newCarInoutDto.setPaId(machineDto.getLocationObjId());
            newCarInoutDto.setStates(new String[]{
                    CarInoutDto.STATE_IN,
                    CarInoutDto.STATE_REPAY
            });
            List<CarInoutDto> carInoutDtos = carInoutV1InnerServiceSMOImpl.queryCarInouts(newCarInoutDto);
            if(carInoutDtos == null || carInoutDtos.size()<1){
                return BeanConvertUtil.covertBean(carInoutDto,CarInoutPo.class);
            }
            CarInoutPo carInoutPo = new CarInoutPo();
            carInoutPo.setInoutId(carInoutDto.getInoutId());
            carInoutPo.setPaId(carInoutDto.getPaId());
            carInoutPo.setCommunityId(carInoutDto.getCommunityId());
            carInoutPo.setState(CarInoutDto.STATE_PAY);
            flag = carInoutV1InnerServiceSMOImpl.updateCarInout(carInoutPo);
            if (flag < 1) {
                throw new CmdException("更新出场时间失败");
            }
            return carInoutPo;
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

    }

    private boolean hasFeeAndPayFee(CarInoutPo carInoutPo, CarInoutPaymentPo carInoutPaymentPo) {

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
        payFeePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setCommunityId(feeDtos.get(0).getCommunityId());
        int flag = feeInnerServiceSMOImpl.updateFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("更新费用失败");
        }


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        if (CarInoutPaymentDto.PAY_TYPE_CRASH.equals(carInoutPaymentPo.getPayType())) {
            payFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_CRASH);
        } else if (CarInoutPaymentDto.PAY_TYPE_WECHAT.equals(carInoutPaymentPo.getPayType())) {
            payFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_WECHAT_QRCODE);
        } else {
            payFeeDetailPo.setPrimeRate(FeeDetailDto.PRIME_REATE_ALI_QRCODE);
        }
        FeeDto feeDto = feeDtos.get(0);
        payFeeDetailPo.setStartTime(DateUtil.getFormatTimeString(feeDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeDetailPo.setCommunityId(carInoutPo.getCommunityId());
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

    private void saveTempCarFee(JSONObject reqJson, MachineDto machineDto, CarInoutDto carInoutDto) {


        //创建费用
        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setCommunityId(reqJson.getString("communityId"));
        tempCarFeeConfigDto.setPaId(carInoutDto.getPaId());
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
        feeAttrPo.setValue(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
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
}
