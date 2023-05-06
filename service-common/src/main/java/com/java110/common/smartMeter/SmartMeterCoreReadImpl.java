package com.java110.common.smartMeter;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.meterMachine.MeterMachineDetailDto;
import com.java110.dto.meterMachine.MeterMachineFactoryDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.intf.common.IMeterMachineDetailV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineFactoryV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meterMachine.MeterMachinePo;
import com.java110.po.meterMachineDetail.MeterMachineDetailPo;
import com.java110.po.meterWater.MeterWaterPo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmartMeterCoreReadImpl implements ISmartMeterCoreRead {

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineDetailV1InnerServiceSMO meterMachineDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;


    @Autowired
    private IMeterWaterV1InnerServiceSMO meterWaterV1InnerServiceSMOImpl;


    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineFactoryV1InnerServiceSMO meterMachineFactoryV1InnerServiceSMOImpl;

    @Override
    public void saveMeterAndCreateFee(MeterMachineDetailDto meterMachineDetailDto, String degree, String batchId) {

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setMachineId(meterMachineDetailDto.getMachineId());
        meterMachineDto.setCommunityId(meterMachineDetailDto.getCommunityId());
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);
        Assert.listOnlyOne(meterMachineDtos, "表不存在");

        String preDegrees = "0";
        String preReadingTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A);

        //费用抄表
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setObjType(MeterWaterDto.OBJ_TYPE_ROOM);
        meterWaterDto.setObjId(meterMachineDtos.get(0).getRoomId());
        meterWaterDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        meterWaterDto.setMeterType(meterMachineDtos.get(0).getMeterType());
        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);

        if (meterWaterDtos != null && meterWaterDtos.size() > 0) {
            preDegrees = meterWaterDtos.get(0).getCurDegrees();
            preReadingTime = meterWaterDtos.get(0).getCurReadingTime();
        }

        MeterMachineDetailPo meterMachineDetailPo = new MeterMachineDetailPo();
        meterMachineDetailPo.setDetailId(meterMachineDetailDto.getDetailId());
        meterMachineDetailPo.setCurDegrees(degree);
        meterMachineDetailPo.setState(MeterMachineDetailDto.STATE_C);
        meterMachineDetailPo.setPrestoreDegrees(preDegrees);
        meterMachineDetailPo.setCurReadingTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterMachineDetailV1InnerServiceSMOImpl.updateMeterMachineDetail(meterMachineDetailPo);


        MeterMachinePo meterMachinePo = new MeterMachinePo();
        meterMachinePo.setMachineId(meterMachineDtos.get(0).getMachineId());
        meterMachinePo.setCurDegrees(degree);
        meterMachinePo.setPrestoreDegrees(degree);
        meterMachinePo.setCurReadingTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterMachineV1InnerServiceSMOImpl.updateMeterMachine(meterMachinePo);

        if (!MeterMachineDto.MACHINE_MODEL_READ.equals(meterMachineDtos.get(0).getMachineModel())) {
            return;
        }



        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        communityMemberDto.setMemberTypeCd("390001200002");
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        if (communityMemberDtos == null || communityMemberDtos.size() < 1) {
            throw new CmdException("未查询到小区和商户的关系，请联系管理员");
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        feeConfigDto.setConfigId(meterMachineDtos.get(0).getFeeConfigId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setIncomeObjId(communityMemberDtos.get(0).getMemberId());
        payFeePo.setAmount("-1");
        payFeePo.setStartTime(preReadingTime);
        payFeePo.setEndTime(preReadingTime);
        payFeePo.setPayerObjId(meterMachineDtos.get(0).getRoomId());
        //payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        payFeePo.setbId("-1");
        payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
       // payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setBatchId(batchId);
        payFeePo.setUserId("-1");
        payFeePo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        payFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeePo.setDeductFrom(feeConfigDtos.get(0).getDeductFrom());

        int flag = payFeeV1InnerServiceSMOImpl.savePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
        feeAttrPo.setValue(meterMachinePo.getCurReadingTime());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        ownerDto.setRoomId(meterMachineDtos.get(0).getRoomId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);

        if (ownerDtos != null && ownerDtos.size() > 0) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
            feeAttrPo.setValue(ownerDtos.get(0).getOwnerId());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(ownerDtos.get(0).getLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(ownerDtos.get(0).getName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }
        MeterWaterPo meterWaterPo = new MeterWaterPo();
        if (StringUtil.isEmpty(meterWaterPo.getbId())) {
            meterWaterPo.setbId("-1");
        }
        meterWaterPo.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        meterWaterPo.setMeterType(meterMachineDtos.get(0).getMeterType());
        meterWaterPo.setFeeId(payFeePo.getFeeId());
        meterWaterPo.setCurDegrees(degree + "");
        meterWaterPo.setCurReadingTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterWaterPo.setObjId(meterMachineDtos.get(0).getRoomId());
        meterWaterPo.setObjName(meterMachineDtos.get(0).getRoomName());
        meterWaterPo.setObjType(MeterWaterDto.OBJ_TYPE_ROOM);
        meterWaterPo.setPreDegrees(preDegrees);
        meterWaterPo.setPreReadingTime(preReadingTime);
        meterWaterPo.setPrice(0.0);
        meterWaterPo.setRemark(meterMachineDtos.get(0).getMachineName() + "抄表");
        meterWaterPo.setWaterId(GenerateCodeFactory.getGeneratorId("11"));

        flag = meterWaterV1InnerServiceSMOImpl.saveMeterWater(meterWaterPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }

    /**
     * 生成批次号
     *
     * @param communityId
     */
    public String generatorBatch(String communityId) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(communityId);
        payFeeBatchPo.setCreateUserId("-1");
        payFeeBatchPo.setCreateUserName("电表推送");
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        return payFeeBatchPo.getBatchId();
    }

    @Override
    public double getMeterDegree(MeterMachineDto meterMachineDto) {

        MeterMachineFactoryDto meterMachineFactoryDto = new MeterMachineFactoryDto();
        meterMachineFactoryDto.setFactoryId(meterMachineDto.getImplBean());
        List<MeterMachineFactoryDto> meterMachineFactoryDtos = meterMachineFactoryV1InnerServiceSMOImpl.queryMeterMachineFactorys(meterMachineFactoryDto);
        Assert.listOnlyOne(meterMachineFactoryDtos, "智能水电表厂家不存在");
        ISmartMeterFactoryAdapt smartMeterFactoryAdapt = ApplicationContextFactory.getBean(meterMachineFactoryDtos.get(0).getBeanImpl(), ISmartMeterFactoryAdapt.class);
        if (smartMeterFactoryAdapt == null) {
            throw new CmdException("厂家接口未实现");
        }

        ResultVo resultVo = smartMeterFactoryAdapt.requestRead(meterMachineDto);

        if(ResultVo.CODE_OK != resultVo.getCode()){
            return 0.0;
        }
        return Double.parseDouble(resultVo.getData().toString());
    }

}
