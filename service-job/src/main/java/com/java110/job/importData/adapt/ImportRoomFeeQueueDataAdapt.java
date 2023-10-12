package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportCustomCreateFeeDto;
import com.java110.dto.importData.ImportFeeDto;
import com.java110.dto.importData.ImportRoomFee;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeeDetailPo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("importRoomFeeQueueData")
public class ImportRoomFeeQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

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

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

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
        ImportRoomFee importRoomFee = BeanConvertUtil.covertBean(data, ImportRoomFee.class);

        String communityId = importRoomFee.getCommunityId();
        String batchId = importRoomFee.getBatchId();

        doImportRoomCreateFee(importRoomFee, batchId);
        doImportCarCreateFee(importRoomFee, batchId);


    }

    /**
     * 房屋创建费用
     *
     * @param importRoomFee
     * @param batchId
     */
    private void doImportRoomCreateFee(ImportRoomFee importRoomFee, String batchId) {
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(importRoomFee.getObjType())) {
            return;
        }
        List<ImportRoomFee> rooms = new ArrayList<>();

        String[] objNames;
        objNames = importRoomFee.getPayerObjName().split("-", 3);
        if (objNames.length != 3) {
            throw new IllegalArgumentException("收费对象格式错误：" + importRoomFee.getPayerObjName());
        }
        importRoomFee.setFloorNum(objNames[0]);
        importRoomFee.setUnitNum(objNames[1]);
        importRoomFee.setRoomNum(objNames[2]);

        rooms.add(importRoomFee);


        List<ImportRoomFee> importRoomFees = roomInnerServiceSMOImpl.freshRoomIds(rooms);
        List<String> roomIds = new ArrayList<>();
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            if (StringUtil.isEmpty(tmpImportRoomFee.getRoomId())) {
                throw new IllegalArgumentException("房屋不存在，" + importRoomFee.getFloorNum() + "-" + importRoomFee.getUnitNum() + "-" + importRoomFee.getRoomNum());
            }
            roomIds.add(tmpImportRoomFee.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(importRoomFee.getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (tmpImportRoomFee.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    importRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    importRoomFee.setOwnerName(tmpOwnerDto.getName());
                    importRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }
        doCreateFeeAndAttrs(importRoomFees.get(0), batchId);
    }


    /**
     * 创建车辆费用
     *
     * @param importRoomFee
     * @param batchId
     */
    private void doImportCarCreateFee(ImportRoomFee importRoomFee, String batchId) {
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(importRoomFee.getObjType())) {
            return;
        }
        List<ImportRoomFee> cars = new ArrayList<>();
        importRoomFee.setCarNum(importRoomFee.getPayerObjName());
        cars.add(importRoomFee);


        List<ImportRoomFee> importRoomFees = ownerCarInnerServiceSMOImpl.freshCarIds(cars);
        for (ImportRoomFee tmpImportRoomFee : importRoomFees) {
            if (StringUtil.isEmpty(tmpImportRoomFee.getRoomId())) {
                throw new IllegalArgumentException(tmpImportRoomFee.getCarNum() + "不存在");
            }
        }
        doCreateFeeAndAttrs(importRoomFees.get(0), batchId);
    }


    private void doCreateFeeAndAttrs(ImportRoomFee importRoomFee, String batchId) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;

        if (StringUtil.isEmpty(importRoomFee.getRoomId())) {
            throw new IllegalArgumentException("收费对象不存在");
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(importRoomFee.getCommunityId());
        if (!StringUtil.isEmpty(importRoomFee.getConfigId())) {
            feeConfigDto.setConfigId(importRoomFee.getConfigId());
        } else {
            feeConfigDto.setFeeNameEq(importRoomFee.getFeeName());
        }
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            throw new IllegalArgumentException("费用项不存在");
        }
        payFeePo = new PayFeePo();
        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId, true));
        payFeePo.setEndTime(importRoomFee.getStartTime());
        payFeePo.setState(FeeDto.STATE_DOING);
        payFeePo.setCommunityId(importRoomFee.getCommunityId());
        payFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(importRoomFee.getObjType())) {
            payFeePo.setPayerObjId(importRoomFee.getContractId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(importRoomFee.getObjType())) {
            payFeePo.setPayerObjId(importRoomFee.getCarId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        } else {
            payFeePo.setPayerObjId(importRoomFee.getRoomId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        }
        payFeePo.setUserId(importRoomFee.getUserId());
        payFeePo.setIncomeObjId(importRoomFee.getStoreId());
        payFeePo.setFeeTypeCd(feeConfigDtos.get(0).getFeeTypeCd());
        payFeePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
        payFeePo.setAmount(importRoomFee.getAmount());
        payFeePo.setBatchId(batchId);
        payFeePo.setEndTime(importRoomFee.getStartTime());
        payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

        payFeePos.add(payFeePo);

        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
        feeAttrPo.setValue(feeConfigDtos.get(0).getFeeName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME);
        feeAttrPo.setValue(importRoomFee.getRoomName());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);

        //todo 不是周期性费用的场景需要写入结束时间

        feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
        feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
        feeAttrPo.setValue(importRoomFee.getEndTime());
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPos.add(feeAttrPo);


        if (!StringUtil.isEmpty(importRoomFee.getOwnerId())) {
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
            feeAttrPo.setValue(importRoomFee.getOwnerId());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
            feeAttrPo.setValue(importRoomFee.getOwnerName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(importRoomFee.getCommunityId());
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
            feeAttrPo.setValue(importRoomFee.getOwnerLink());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);
        }


        if (payFeePos.size() < 1) {
            return;
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        //todo 保存导入记录日志
        saveImportFee(importRoomFee, payFeePo);

        // todo 这里异步的方式计算 月数据 和欠费数据

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(importRoomFee.getCommunityId());
        payFeeDetailRefreshFeeMonthDto.setFeeId(payFeePos.get(0).getFeeId());
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto);
    }

    private void saveImportFee(ImportRoomFee importRoomFee, PayFeePo payFeePo) {
        ImportFeeDetailPo importFeeDetailPo;
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        importFeeDetailPo = new ImportFeeDetailPo();
        importFeeDetailPo.setAmount(importRoomFee.getAmount());
        importFeeDetailPo.setCommunityId(payFeePo.getCommunityId());
        importFeeDetailPo.setEndTime(importRoomFee.getEndTime());
        importFeeDetailPo.setFeeId(payFeePo.getFeeId());
        importFeeDetailPo.setFeeName(importRoomFee.getFeeName());
        importFeeDetailPo.setFloorNum(importRoomFee.getFloorNum());
        importFeeDetailPo.setUnitNum(importRoomFee.getUnitNum());
        importFeeDetailPo.setRoomNum(importRoomFee.getRoomNum());
        importFeeDetailPo.setRoomId(importRoomFee.getRoomId());
        importFeeDetailPo.setObjId(importRoomFee.getRoomId());
        importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        importFeeDetailPo.setObjName(!"0".equals(importRoomFee.getUnitNum())
                ? importRoomFee.getFloorNum() + "栋" + importRoomFee.getUnitNum() + "单元" + importRoomFee.getRoomNum() + "室" :
                importRoomFee.getFloorNum() + "栋" + importRoomFee.getRoomNum() + "室"
        );
        importFeeDetailPo.setStartTime(importRoomFee.getStartTime());
        importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId, true));
        importFeeDetailPo.setState("1000");
        importFeeDetailPo.setImportFeeId(importRoomFee.getBatchId());
        importFeeDetailPos.add(importFeeDetailPo);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(payFeePo.getCommunityId());
        importFeeDto.setImportFeeId(importRoomFee.getBatchId());

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(importRoomFee.getCommunityId());
            importFeePo.setFeeTypeCd(importRoomFee.getFeeTypeCd());
            importFeePo.setImportFeeId(importRoomFee.getBatchId());
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);
    }
}
