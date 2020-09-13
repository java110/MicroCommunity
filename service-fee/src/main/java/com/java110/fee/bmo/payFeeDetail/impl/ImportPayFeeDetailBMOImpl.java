package com.java110.fee.bmo.payFeeDetail.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.fee.bmo.payFeeDetail.IImportPayFeeBMODetail;
import com.java110.intf.IImportFeeDetailInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("importPayFeeDetailBMOImpl")
public class ImportPayFeeDetailBMOImpl implements IImportPayFeeBMODetail {

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

    /**
     * @param reqJson
     * @return 订单服务能够接受的报文
     */
    //@Java110Transactional
    public ResponseEntity<String> importPayFeeDetail(JSONObject reqJson) {


        String communityId = reqJson.getString("communityId");
        String storeId = reqJson.getString("storeId");
        String userId = reqJson.getString("userId");

        JSONArray datas = reqJson.getJSONArray("importRoomFees");

        JSONObject data = null;
        List<ImportRoomFee> importRoomFees = new ArrayList<>();
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            data = datas.getJSONObject(dataIndex);
            data.put("communityId", communityId);
            importRoomFees.add(BeanConvertUtil.covertBean(data, ImportRoomFee.class));
        }

        if (importRoomFees.size() < 1) {
            return ResultVo.success();
        }

        importFeeDetails(storeId, userId, importRoomFees);

        return ResultVo.success();
    }

    private void importFeeDetails(String storeId, String userId, List<ImportRoomFee> importRoomFees) {

        importRoomFees = roomInnerServiceSMOImpl.freshRoomIds(importRoomFees);

        for (ImportRoomFee importRoomFee : importRoomFees) {
            importFeeDetail(importRoomFee, storeId, userId);
        }
    }

    /**
     * 导入 费用历史
     *
     * @param importRoomFee
     */
    private void importFeeDetail(ImportRoomFee importRoomFee, String storeId, String userId) {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeName(importRoomFee.getFeeName());
        feeConfigDto.setCommunityId(importRoomFee.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "费用不存在或存在多条");

        FeeConfigDto tmpFeeConfigDto = feeConfigDtos.get(0);

        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(tmpFeeConfigDto.getConfigId());
        feeDto.setCommunityId(importRoomFee.getCommunityId());
        feeDto.setPayerObjId(importRoomFee.getRoomId());
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        List<PayFeePo> payFeePos = null;
        if (feeDtos == null || feeDtos.size() < 1) {
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setCommunityId(importRoomFee.getCommunityId());
            payFeePo.setConfigId(feeDto.getConfigId());
            payFeePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            payFeePo.setStartTime(importRoomFee.getStartTime());
            payFeePo.setEndTime(importRoomFee.getEndTime());
            payFeePo.setAmount(importRoomFee.getAmount());
            payFeePo.setFeeFlag(tmpFeeConfigDto.getFeeFlag());
            payFeePo.setFeeTypeCd(tmpFeeConfigDto.getFeeTypeCd());
            payFeePo.setIncomeObjId(storeId);
            payFeePo.setState(FeeDto.STATE_DOING);
            payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
            payFeePo.setPayerObjId(importRoomFee.getRoomId());
            payFeePo.setUserId(userId);
            payFeePos = new ArrayList<>();
            payFeePos.add(payFeePo);
            feeInnerServiceSMOImpl.saveFee(payFeePos);
            feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        }

        for (FeeDto tmpFeeDto : feeDtos) {
            try {
                doImportFeeDetail(tmpFeeDto, importRoomFee);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private void doImportFeeDetail(FeeDto tmpFeeDto, ImportRoomFee importRoomFee) throws ParseException {
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(importRoomFee.getCommunityId());
        feeDetailDto.setFeeId(tmpFeeDto.getFeeId());


        feeDetailDto.setStartTime(DateUtil.getDateFromString(importRoomFee.getStartTime(), DateUtil.DATE_FORMATE_STRING_B));
        feeDetailDto.setEndTime(DateUtil.getDateFromString(importRoomFee.getEndTime(), DateUtil.DATE_FORMATE_STRING_B));

        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        if (feeDetailDtos != null && feeDetailDtos.size() > 0) {//说明已经导入过了
            return;
        }

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setCommunityId(importRoomFee.getCommunityId());
        payFeeDetailPo.setReceivedAmount(importRoomFee.getAmount());
        payFeeDetailPo.setReceivableAmount(importRoomFee.getAmount());
        payFeeDetailPo.setCycles(importRoomFee.getCycle());
        payFeeDetailPo.setPrimeRate("1.0");
        payFeeDetailPo.setFeeId(tmpFeeDto.getFeeId());
        payFeeDetailPo.setStartTime(importRoomFee.getStartTime());
        payFeeDetailPo.setEndTime(importRoomFee.getEndTime());
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setRemark(importRoomFee.getRemark());
        int saved = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);

        if (saved < 1) {
            return;
        }

        if (tmpFeeDto.getEndTime().getTime() >= DateUtil.getDateFromString(importRoomFee.getEndTime(), DateUtil.DATE_FORMATE_STRING_B).getTime()) {
            return;
        }

        //如果结束时间小于 缴费结束时间则延期
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setCommunityId(importRoomFee.getCommunityId());

        payFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        payFeePo.setPayerObjId(tmpFeeDto.getFeeId());
        payFeePo.setEndTime(importRoomFee.getEndTime());
        feeInnerServiceSMOImpl.updateFee(payFeePo);
    }


}
