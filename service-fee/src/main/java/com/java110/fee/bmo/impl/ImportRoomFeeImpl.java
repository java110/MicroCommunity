package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importFee.ImportFeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.fee.bmo.IImportRoomFee;
import com.java110.fee.listener.fee.UpdateFeeInfoListener;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 欠费缴费实现类
 */
@Service
public class ImportRoomFeeImpl implements IImportRoomFee {

    private static Logger logger = LoggerFactory.getLogger(UpdateFeeInfoListener.class);


    private static final String IMPORT_FEE_NAME = "导入费用";


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    /**
     * 欠费缴费
     *
     * @param reqJson
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importFee(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");


        JSONArray importRoomFees = reqJson.getJSONArray("importRoomFees");

        List<ImportRoomFee> tmpImportRoomFees = importRoomFees.toJavaList(ImportRoomFee.class);

        if (tmpImportRoomFees == null || tmpImportRoomFees.size() < 1) {
            throw new IllegalArgumentException("未包含导入费用");
        }

        if (StringUtil.isEmpty(feeName)) {
            feeName = tmpImportRoomFees.get(0).getFeeName();
        }

        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setFeeName(feeName);
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }

        for (ImportRoomFee tmpImportRoomFee : tmpImportRoomFees) {
            tmpImportRoomFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportRoomFees, "参数错误，未包含处理费用");

        tmpImportRoomFees = roomInnerServiceSMOImpl.freshRoomIds(tmpImportRoomFees);

        List<String> roomIds = new ArrayList<>();
        for (ImportRoomFee importRoomFee : tmpImportRoomFees) {
            roomIds.add(importRoomFee.getRoomId());
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(tmpImportRoomFees.get(0).getCommunityId());
        ownerDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
        for (ImportRoomFee importRoomFee : tmpImportRoomFees) {
            if (StringUtil.isEmpty(importRoomFee.getRoomId())) {
                throw new IllegalArgumentException("房屋不存在，" + importRoomFee.getFloorNum() + "-" + importRoomFee.getUnitNum() + "-" + importRoomFee.getRoomNum());
            }
            for (OwnerDto tmpOwnerDto : ownerDtos) {
                if (importRoomFee.getRoomId().equals(tmpOwnerDto.getRoomId())) {
                    importRoomFee.setOwnerId(tmpOwnerDto.getOwnerId());
                    importRoomFee.setOwnerName(tmpOwnerDto.getName());
                    importRoomFee.setOwnerLink(tmpOwnerDto.getLink());
                }
            }
        }

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportRoomFee importRoomFee : tmpImportRoomFees) {
            if (StringUtil.isEmpty(importRoomFee.getRoomId()) || importRoomFee.getRoomId().startsWith("-") ||
                    StringUtil.isEmpty(importRoomFee.getFloorNum()) || importRoomFee.getFloorNum().startsWith("-") ||
                    StringUtil.isEmpty(importRoomFee.getUnitNum()) || importRoomFee.getUnitNum().startsWith("-")
            ) {
                errorCount++;
                continue;
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importRoomFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(communityId);
            payFeePo.setConfigId(feeConfigDto.getConfigId());
            payFeePo.setPayerObjId(importRoomFee.getRoomId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setUserId(userId);
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setFeeTypeCd(feeTypeCd);
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setAmount(importRoomFee.getAmount());
            payFeePo.setBatchId(batchId);
            //payFeePo.setStartTime(importRoomFee.getStartTime());
            payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(importRoomFee.getFeeName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(importRoomFee.getEndTime());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);

            //todo 费用对象名称
            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME);
            feeAttrPo.setValue(importRoomFee.getRoomName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importRoomFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importRoomFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importRoomFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importRoomFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importRoomFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
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
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 车辆费用导入
     *
     * @param reqJson
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importCarFee(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");

        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setFeeName(feeName);
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }


        JSONArray importCarFees = reqJson.getJSONArray("importCarFees");

        List<ImportRoomFee> tmpImportCarFees = importCarFees.toJavaList(ImportRoomFee.class);

        for (ImportRoomFee tmpImportCarFee : tmpImportCarFees) {
            tmpImportCarFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportCarFees, "参数错误，未包含处理费用");

        tmpImportCarFees = ownerCarInnerServiceSMOImpl.freshCarIds(tmpImportCarFees);

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportRoomFee importCarFee : tmpImportCarFees) {
            if (StringUtil.isEmpty(importCarFee.getCarId()) || importCarFee.getCarId().startsWith("-") ||
                    StringUtil.isEmpty(importCarFee.getCarNum()) || importCarFee.getCarNum().startsWith("-")
            ) {
                errorCount++;
                continue;
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importCarFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(communityId);
            payFeePo.setConfigId(feeConfigDto.getConfigId());
            payFeePo.setPayerObjId(importCarFee.getCarId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
            payFeePo.setUserId(userId);
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setFeeTypeCd(feeTypeCd);
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setAmount(importCarFee.getAmount());
            payFeePo.setBatchId(batchId);
            //payFeePo.setStartTime(importRoomFee.getStartTime());
            payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(importCarFee.getFeeName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(importCarFee.getEndTime());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importCarFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importCarFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importCarFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importCarFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importCarFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
            importFeeDetailPo.setEndTime(importCarFee.getEndTime());
            importFeeDetailPo.setFeeId(payFeePo.getFeeId());
            importFeeDetailPo.setFeeName(importCarFee.getFeeName());
            importFeeDetailPo.setFloorNum("-");
            importFeeDetailPo.setUnitNum("-");
            importFeeDetailPo.setRoomNum("-");
            importFeeDetailPo.setRoomId("-");
            importFeeDetailPo.setObjId(importCarFee.getCarId());
            importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
            importFeeDetailPo.setObjName(importCarFee.getCarNum());
            importFeeDetailPo.setStartTime(importCarFee.getStartTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 合同费用导入
     *
     * @param reqJson
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importContractFees(JSONObject reqJson) {

        int successCount = 0;
        int errorCount = 0;

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");
        String feeName = reqJson.getString("feeName");
        String batchId = reqJson.getString("batchId");

        if (StringUtil.isEmpty(feeName)) {
            feeName = IMPORT_FEE_NAME;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setFeeName(feeName);
        feeConfigDto.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        feeConfigDto.setCommunityId(communityId);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        // 根据费用大类 判断是否有存在 费用导入收入项
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            //生成导入费
            feeConfigDto.setConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
            saveFeeConfig(feeConfigDto);
        } else {
            feeConfigDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        }


        JSONArray importCarFees = reqJson.getJSONArray("importRoomFees");

        List<ImportRoomFee> tmpImportContractFees = importCarFees.toJavaList(ImportRoomFee.class);

        for (ImportRoomFee tmpImportCarFee : tmpImportContractFees) {
            tmpImportCarFee.setCommunityId(communityId);
        }

        Assert.isNotNull(tmpImportContractFees, "参数错误，未包含处理费用");

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportRoomFee importCarFee : tmpImportContractFees) {
            if (StringUtil.isEmpty(importCarFee.getContractId()) || importCarFee.getContractId().startsWith("-")
            ) {
                errorCount++;
                continue;
            }
            successCount++;
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importCarFee.getStartTime());
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setCommunityId(communityId);
            payFeePo.setConfigId(feeConfigDto.getConfigId());
            payFeePo.setPayerObjId(importCarFee.getContractId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
            payFeePo.setUserId(userId);
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setFeeTypeCd(feeTypeCd);
            payFeePo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
            payFeePo.setAmount(importCarFee.getAmount());
            payFeePo.setStartTime(importCarFee.getStartTime());
            payFeePo.setBatchId(batchId);
            //payFeePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(importCarFee.getFeeName());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(importCarFee.getEndTime());
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPos.add(feeAttrPo);


            if (!StringUtil.isEmpty(importCarFee.getOwnerId())) {
                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_ID);
                feeAttrPo.setValue(importCarFee.getOwnerId());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_NAME);
                feeAttrPo.setValue(importCarFee.getOwnerName());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);

                feeAttrPo = new FeeAttrPo();
                feeAttrPo.setCommunityId(communityId);
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_OWNER_LINK);
                feeAttrPo.setValue(importCarFee.getOwnerLink());
                feeAttrPo.setFeeId(payFeePo.getFeeId());
                feeAttrPos.add(feeAttrPo);
            }

            importFeeDetailPo = new ImportFeeDetailPo();
            importFeeDetailPo.setAmount(importCarFee.getAmount());
            importFeeDetailPo.setCommunityId(communityId);
            importFeeDetailPo.setEndTime(importCarFee.getEndTime());
            importFeeDetailPo.setFeeId(payFeePo.getFeeId());
            importFeeDetailPo.setFeeName(importCarFee.getFeeName());
            importFeeDetailPo.setFloorNum("-");
            importFeeDetailPo.setUnitNum("-");
            importFeeDetailPo.setRoomNum("-");
            importFeeDetailPo.setRoomId("-");
            importFeeDetailPo.setObjId(importCarFee.getContractId());
            importFeeDetailPo.setObjType(FeeDto.PAYER_OBJ_TYPE_CONTRACT);
            importFeeDetailPo.setObjName("合同");
            importFeeDetailPo.setStartTime(importCarFee.getStartTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_IfdId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPo.setImportFeeId(importFeeId);
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        ImportFeeDto importFeeDto = new ImportFeeDto();
        importFeeDto.setCommunityId(communityId);
        importFeeDto.setImportFeeId(importFeeId);

        List<ImportFeeDto> importRoomFeess = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);

        if (importRoomFeess == null || importRoomFeess.size() < 1) {
            //保存日志
            ImportFeePo importFeePo = new ImportFeePo();
            importFeePo.setCommunityId(communityId);
            importFeePo.setFeeTypeCd(feeTypeCd);
            importFeePo.setImportFeeId(importFeeId);
            importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);
        }


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        JSONObject data = new JSONObject();
        data.put("successCount", successCount);
        data.put("errorCount", errorCount);

        return ResultVo.createResponseEntity(data);
    }

    /**
     * 保存保存导入费用配置
     *
     * @param feeConfigDto
     */
    private void saveFeeConfig(FeeConfigDto feeConfigDto) {

        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(feeConfigDto, PayFeeConfigPo.class);
        payFeeConfigPo.setAdditionalAmount("0");
        payFeeConfigPo.setBillType(FeeConfigDto.BILL_TYPE_MONTH);
        payFeeConfigPo.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        payFeeConfigPo.setEndTime(DateUtil.getLastTime());
        payFeeConfigPo.setFeeFlag("2006012");
        payFeeConfigPo.setIsDefault("F");
        payFeeConfigPo.setPaymentCd("2100");
        payFeeConfigPo.setFeeName(feeConfigDto.getFeeName());
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setPaymentCycle("1");
        payFeeConfigPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        payFeeConfigPo.setDeductFrom(FeeConfigDto.DEDUCT_FROM_N);
        payFeeConfigPo.setDecimalPlace("2");
        payFeeConfigPo.setScale("1");
        payFeeConfigPo.setUnits("元");
        payFeeConfigPo.setPayOnline("Y");
        int saveFlag = feeConfigInnerServiceSMOImpl.saveFeeConfig(payFeeConfigPo);

        if (saveFlag < 1) {
            throw new IllegalArgumentException("创建导入费用失败");
        }
    }


}
