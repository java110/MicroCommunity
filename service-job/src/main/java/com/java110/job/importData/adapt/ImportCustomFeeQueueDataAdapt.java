package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.store.IContractRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeeDetailPo;
import com.java110.po.meter.MeterWaterPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importCustomFeeQueueData")
public class ImportCustomFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    //    @Autowired
//    private IPayFeeMonth payFeeMonthImpl;
    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {

        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportCustomCreateFeeDto importExportMeterWaterDto = BeanConvertUtil.covertBean(data, ImportCustomCreateFeeDto.class);

        String communityId = importExportMeterWaterDto.getCommunityId();
        String batchId = importExportMeterWaterDto.getBatchId();

        doImportRoomCreateFee(importExportMeterWaterDto, batchId);
        doImportCarCreateFee(importExportMeterWaterDto, batchId);


    }

    /**
     * 房屋创建费用
     *
     * @param importCustomCreateFeeDto
     * @param batchId
     */
    private void doImportRoomCreateFee(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (!ImportCustomCreateFeeDto.TYPE_ROOM.equals(importCustomCreateFeeDto.getObjType())) {
            return;
        }
        List<ImportCustomCreateFeeDto> rooms = new ArrayList<>();
        String[] objNames;
        objNames = importCustomCreateFeeDto.getObjName().split("-", 3);
        if (objNames.length != 3) {
            throw new IllegalArgumentException("收费对象格式错误：" + importCustomCreateFeeDto.getObjName());
        }
        importCustomCreateFeeDto.setFloorNum(objNames[0]);
        importCustomCreateFeeDto.setUnitNum(objNames[1]);
        importCustomCreateFeeDto.setRoomNum(objNames[2]);
        rooms.add(importCustomCreateFeeDto);


        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = roomInnerServiceSMOImpl.freshRoomIdsByImportCustomCreateFee(rooms);
        List<String> roomIds = new ArrayList<>();
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            roomIds.add(importRoomFee.getPayObjId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importCustomCreateFeeDtos.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportCustomCreateFeeDto importRoomFee : importCustomCreateFeeDtos) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (importRoomFee.getPayObjId().equals(tmpOwnerDto.getRoomId())) {
                    importRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    importRoomFee.setOwnerName(tmpOwnerDto.getName());
                    importRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }
        doCreateFeeAndAttrs(importCustomCreateFeeDtos.get(0), batchId);
    }


    /**
     * 创建车辆费用
     *
     * @param importCustomCreateFeeDto
     * @param batchId
     */
    private void doImportCarCreateFee(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        if (!ImportCustomCreateFeeDto.TYPE_CAR.equals(importCustomCreateFeeDto.getObjType())) {
            return;
        }
        List<ImportCustomCreateFeeDto> cars = new ArrayList<>();


        importCustomCreateFeeDto.setCarNum(importCustomCreateFeeDto.getObjName());

        cars.add(importCustomCreateFeeDto);


        List<ImportCustomCreateFeeDto> importCustomCreateFeeDtos = ownerCarInnerServiceSMOImpl.freshCarIdsByImportCustomCreateFee(cars);

        doCreateFeeAndAttrs(importCustomCreateFeeDtos.get(0), batchId);
    }


    private void doCreateFeeAndAttrs(ImportCustomCreateFeeDto importCustomCreateFeeDto, String batchId) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;

        if (StringUtil.isEmpty(importCustomCreateFeeDto.getPayObjId())) {
            throw new IllegalArgumentException("收费对象不存在");
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        feeConfigDto.setConfigId(importCustomCreateFeeDto.getConfigId());
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            throw new IllegalArgumentException("费用项不存在");
        }
        payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId, true));
        payFeePo.setEndTime(importCustomCreateFeeDto.getStartTime());
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        payFeePo.setConfigId(importCustomCreateFeeDto.getConfigId());
        payFeePo.setPayerObjId(importCustomCreateFeeDto.getPayObjId());
        if ("3003".equals(importCustomCreateFeeDto.getObjType())) {
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        } else if ("2002".equals(importCustomCreateFeeDto.getObjType())) {
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        } else {
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        }
        payFeePo.setUserId(importCustomCreateFeeDto.getUserId());
        payFeePo.setIncomeObjId(importCustomCreateFeeDto.getStoreId());
        payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        payFeePo.setAmount("-1");
        payFeePo.setBatchId(batchId);
        payFeePo.setEndTime(importCustomCreateFeeDto.getStartTime());
        payFeePo.setStartTime(importCustomCreateFeeDto.getCreateTime());
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())
                && !StringUtil.isEmpty(importCustomCreateFeeDto.getEndTime())) {
            payFeePo.setMaxTime(importCustomCreateFeeDto.getEndTime());
        } else {
            payFeePo.setMaxTime(feeConfigDtos.get(0).getEndTime());
        }

        payFeePos.add(payFeePo);

        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
        feeAttrPo.setValue(feeConfigDtos.get(0).getFeeName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME);
        feeAttrPo.setValue(importCustomCreateFeeDto.getObjName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        //todo 不是周期性费用的场景需要写入结束时间
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())
                && !StringUtil.isEmpty(importCustomCreateFeeDto.getEndTime())) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(importCustomCreateFeeDto.getEndTime());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
        }


        if (!StringUtil.isEmpty(importCustomCreateFeeDto.getOwnerId())) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
            feeAttrPo.setValue(importCustomCreateFeeDto.getOwnerId());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(importCustomCreateFeeDto.getOwnerName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importCustomCreateFeeDto.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(importCustomCreateFeeDto.getOwnerLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
        }


        if (payFeePos.size() < 1) {
            return;
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        // todo 这里异步的方式计算 月数据 和欠费数据

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(importCustomCreateFeeDto.getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setFeeId(payFeePos.get(0).getFeeId());
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto);
    }
}
