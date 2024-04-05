package com.java110.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.importData.ImportRoomFee;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.job.importData.DefaultImportData;
import com.java110.job.importData.IImportDataAdapt;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 房屋缴费信息导入 适配器
 * 前端请求 时 必须传入
 * param.append('importAdapt', "importRoomOwner");
 */
@Service("importRoomHistoryFeeDetailQueueData")
public class ImportRoomHistoryFeeDetailQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {


    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;


    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        importDatas(assetImportLogDetailDtos);
    }

    private void importDatas(List<AssetImportLogDetailDto> infos) {

        List<ImportRoomFee> importRoomFees = new ArrayList<>();
        for (AssetImportLogDetailDto assetImportLogDetailDto : infos) {
            JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
            ImportRoomFee importRoomFee = BeanConvertUtil.covertBean(data, ImportRoomFee.class);
            importRoomFee.setDetailId(assetImportLogDetailDto.getDetailId());
            importRoomFees.add(importRoomFee);
        }

        if (ListUtil.isNull(importRoomFees)) {
            return;
        }

        importFeeDetails(importRoomFees.get(0).getStoreId(), importRoomFees.get(0).getUserId(), importRoomFees, importRoomFees.get(0).getBatchId());


    }

    private void importFeeDetails(String storeId, String userId, List<ImportRoomFee> importRoomFees, String batchId) {

        importRoomFees = roomInnerServiceSMOImpl.freshRoomIds(importRoomFees);
        String endTime = "";
        for (ImportRoomFee importRoomFee : importRoomFees) {

            endTime = importRoomFee.getEndTime();
            if (!endTime.contains(":")) {
                endTime += " 23:59:59";
                importRoomFee.setEndTime(endTime);
            }


            try {
                if (StringUtil.isEmpty(importRoomFee.getRoomId())) {
                    continue;
                }
                importFeeDetail(importRoomFee, storeId, userId, batchId);
                updateImportLogDetailState(importRoomFee.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(importRoomFee.getDetailId(), e);
            }
        }
    }

    /**
     * 导入 费用历史
     *
     * @param importRoomFee
     */
    private void importFeeDetail(ImportRoomFee importRoomFee, String storeId, String userId, String batchId) {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeNameEq(importRoomFee.getFeeName().trim());
        feeConfigDto.setCommunityId(importRoomFee.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos)) {
            return;
        }

        FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);

        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(tmpFeeConfigDto.getConfigId());
        feeDto.setCommunityId(importRoomFee.getCommunityId());
        feeDto.setPayerObjId(importRoomFee.getRoomId());
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        List<PayFeePo> payFeePos = null;
        if (ListUtil.isNull(feeDtos)) {
            List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setCommunityId(importRoomFee.getCommunityId());
            payFeePo.setConfigId(feeDto.getConfigId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setStartTime(importRoomFee.getStartTime());
            payFeePo.setEndTime(DateUtil.getNextSecTime(importRoomFee.getEndTime()));
            payFeePo.setAmount(importRoomFee.getAmount());
            payFeePo.setFeeFlag(tmpFeeConfigDto.getFeeFlag());
            payFeePo.setFeeTypeCd(tmpFeeConfigDto.getFeeTypeCd());
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setBatchId(batchId);
            if (FeeDto.FEE_FLAG_ONCE.equals(tmpFeeConfigDto.getFeeFlag())) {
                payFeePo.setState(FeeDto.STATE_FINISH);
            } else {
                payFeePo.setState(FeeDto.STATE_DOING);
            }
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setPayerObjId(importRoomFee.getRoomId());
            payFeePo.setUserId(userId);
            payFeePo.setCreateTime(importRoomFee.getCreateTime());
            payFeePos = new ArrayList<>();
            payFeePos.add(payFeePo);
            feeInnerServiceSMOImpl.saveFee(payFeePos);

            //查询业主信息
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(importRoomFee.getRoomId());
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() > 0) {
                if (!FeeDto.FEE_FLAG_CYCLE.equals(tmpFeeConfigDto.getFeeFlag())) {
                    feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            importRoomFee.getEndTime()));
                }
                feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_ID, ownerRoomRelDtos.get(0).getOwnerId()));
                feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_LINK, ownerRoomRelDtos.get(0).getLink()));
                feeAttrsPos.add(addFeeAttr(payFeePo, FeeAttrDto.SPEC_CD_OWNER_NAME, ownerRoomRelDtos.get(0).getOwnerName()));
                feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);
            }


            feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        }

        for (FeeDto tmpFeeDto : feeDtos) {
            doImportFeeDetail(tmpFeeDto, importRoomFee);
        }

    }

    public FeeAttrPo addFeeAttr(PayFeePo payFeePo, String specCd, String value) {
        FeeAttrPo feeAttrPo = new FeeAttrPo();
        feeAttrPo.setCommunityId(payFeePo.getCommunityId());
        feeAttrPo.setSpecCd(specCd);
        feeAttrPo.setValue(value);
        feeAttrPo.setFeeId(payFeePo.getFeeId());
        feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        return feeAttrPo;

    }


    private void doImportFeeDetail(FeeDto tmpFeeDto, ImportRoomFee importRoomFee) {


        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setCommunityId(importRoomFee.getCommunityId());
        payFeeDetailPo.setReceivedAmount(importRoomFee.getAmount());
        payFeeDetailPo.setReceivableAmount(importRoomFee.getReceivableAmount());
        payFeeDetailPo.setCycles(importRoomFee.getCycle());
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setFeeId(tmpFeeDto.getFeeId());
        payFeeDetailPo.setStartTime(importRoomFee.getStartTime());
        payFeeDetailPo.setEndTime(importRoomFee.getEndTime());
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setRemark(importRoomFee.getRemark());
        payFeeDetailPo.setCreateTime(importRoomFee.getCreateTime());
        payFeeDetailPo.setState("1400");
        payFeeDetailPo.setCashierName(importRoomFee.getStaffName());
        payFeeDetailPo.setPayableAmount(importRoomFee.getAmount());
        int saved = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);

        if (saved < 1) {
            return;
        }

        if (tmpFeeDto.getEndTime().getTime() >= DateUtil.getDateFromStringB(importRoomFee.getEndTime()).getTime()) {
            return;
        }

        //如果结束时间小于 缴费结束时间则延期
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setCommunityId(importRoomFee.getCommunityId());
        payFeePo.setStatusCd(StatusConstant.STATUS_CD_VALID);
        payFeePo.setFeeId(tmpFeeDto.getFeeId());
        payFeePo.setEndTime(DateUtil.getNextSecTime(importRoomFee.getEndTime()));
        if (FeeDto.FEE_FLAG_ONCE.equals(tmpFeeDto.getFeeFlag())) {
            payFeePo.setState(FeeDto.STATE_FINISH);
        }

        feeInnerServiceSMOImpl.updateFee(payFeePo);
    }


}
