package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.fee.bmo.IImportRoomFee;
import com.java110.fee.listener.fee.UpdateFeeInfoListener;
import com.java110.intf.IImportFeeDetailInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.importFee.ImportFeePo;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 欠费缴费
     *
     * @param reqJson
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> importFee(JSONObject reqJson) {

        //小区ID
        String communityId = reqJson.getString("communityId");
        String importFeeId = reqJson.getString("importFeeId");
        String feeTypeCd = reqJson.getString("feeTypeCd");//费用大类
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeTypeCd(feeTypeCd);
        feeConfigDto.setFeeName(IMPORT_FEE_NAME);
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


        JSONArray importRoomFees = reqJson.getJSONArray("importRoomFees");

        List<ImportRoomFee> tmpImportRoomFees = importRoomFees.toJavaList(ImportRoomFee.class);

        Assert.isNotNull(tmpImportRoomFees, "参数错误，未包含处理费用");

        tmpImportRoomFees = roomInnerServiceSMOImpl.freshRoomIds(tmpImportRoomFees);

        List<PayFeePo> payFeePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrPos = new ArrayList<>();
        List<ImportFeeDetailPo> importFeeDetailPos = new ArrayList<>();
        PayFeePo payFeePo = null;
        ImportFeeDetailPo importFeeDetailPo = null;
        for (ImportRoomFee importRoomFee : tmpImportRoomFees) {
            payFeePo = new PayFeePo();
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setEndTime(importRoomFee.getEndTime());
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

            payFeePos.add(payFeePo);

            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setCommunityId(communityId);
            feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_IMPORT_FEE_NAME);
            feeAttrPo.setValue(importRoomFee.getFeeName());
            feeAttrPos.add(feeAttrPo);
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
            importFeeDetailPo.setStartTime(importRoomFee.getStartTime());
            importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            importFeeDetailPo.setState("1000");
            importFeeDetailPos.add(importFeeDetailPo);
        }

        feeInnerServiceSMOImpl.saveFee(payFeePos);

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

        //保存日志
        ImportFeePo importFeePo = new ImportFeePo();
        importFeePo.setCommunityId(communityId);
        importFeePo.setFeeTypeCd(feeTypeCd);
        importFeePo.setImportFeeId(importFeeId);
        importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);


        importFeeDetailInnerServiceSMOImpl.saveImportFeeDetails(importFeeDetailPos);


        return ResultVo.success();
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
        payFeeConfigPo.setComputingFormula("4004");
        payFeeConfigPo.setEndTime(DateUtil.getLastTime());
        payFeeConfigPo.setFeeFlag("2006012");
        payFeeConfigPo.setIsDefault("T");
        payFeeConfigPo.setPaymentCd("2100");
        payFeeConfigPo.setFeeName(IMPORT_FEE_NAME);
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        int saveFlag = feeConfigInnerServiceSMOImpl.saveFeeConfig(payFeeConfigPo);

        if (saveFlag < 1) {
            throw new IllegalArgumentException("创建导入费用失败");
        }
    }


}
