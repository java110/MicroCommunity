package com.java110.fee.bmo.feeManualCollectionDetail.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeManualCollectionDto;
import com.java110.dto.fee.FeeManualCollectionDetailDto;
import com.java110.fee.bmo.feeManualCollectionDetail.IGetExportCollectionBMO;
import com.java110.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("getCollectionBMOImpl")
public class GetCollectionBMOImpl implements IGetExportCollectionBMO {

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeManualCollectionDetailDto feeManualCollectionDetailDto) {

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(feeManualCollectionDetailDto.getCommunityId());
        feeConfigDto.setIsDefault(FeeConfigDto.CUSTOME_FEE_CONFIG);
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        JSONArray data = new JSONArray();

        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            return ResultVo.createResponseEntity(0, 0, data);
        }
        FeeManualCollectionDto feeManualCollectionDto = new FeeManualCollectionDto();
        feeManualCollectionDto.setCommunityId(feeManualCollectionDetailDto.getCommunityId());
        feeManualCollectionDto.setState(FeeManualCollectionDto.STATE_COLLECTION);
        List<FeeManualCollectionDto> feeManualCollectionDtos = feeManualCollectionInnerServiceSMOImpl.queryFeeManualCollections(feeManualCollectionDto);

        if (feeManualCollectionDtos == null || feeManualCollectionDtos.size() < 1) {
            return ResultVo.createResponseEntity(0, 0, data);
        }
        for (FeeManualCollectionDto tmpFeeManualCollectionDto : feeManualCollectionDtos) {

            doDealDetail(tmpFeeManualCollectionDto, feeConfigDtos, data);

        }
        return ResultVo.createResponseEntity(data);
    }

    private void doDealDetail(FeeManualCollectionDto tmpFeeManualCollectionDto, List<FeeConfigDto> feeConfigDtos, JSONArray data) {

        JSONObject dataObj = new JSONObject(true);

        List<FeeManualCollectionDetailDto> feeManualCollectionDetailDtos = null;

        FeeManualCollectionDetailDto feeManualCollectionDetailDto = new FeeManualCollectionDetailDto();
        feeManualCollectionDetailDto.setCollectionId(tmpFeeManualCollectionDto.getCollectionId());
        feeManualCollectionDetailDto.setCommunityId(tmpFeeManualCollectionDto.getCommunityId());
        feeManualCollectionDetailDto.setState(FeeManualCollectionDetailDto.STATE_COLLECTION);

        feeManualCollectionDetailDtos = feeManualCollectionDetailInnerServiceSMOImpl.queryFeeManualCollectionDetails(feeManualCollectionDetailDto);

        if (feeManualCollectionDetailDtos == null || feeManualCollectionDetailDtos.size() < 1) {
            return;
        }

        dataObj.put("房号", tmpFeeManualCollectionDto.getRoomName());
        dataObj.put("姓名", tmpFeeManualCollectionDto.getOwnerName());
        dataObj.put("联系电话", tmpFeeManualCollectionDto.getLink());
        dataObj.put("收费面积/m2", tmpFeeManualCollectionDto.getRoomArea());

        BigDecimal totalFee = new BigDecimal(0);

        for (FeeConfigDto feeConfigDto : feeConfigDtos) {

            FeeManualCollectionDetailDto tmpFeeManualCollectionDetailDto = hasFeeConfig(feeConfigDto, feeManualCollectionDetailDtos);

            if (tmpFeeManualCollectionDetailDto == null) {
                dataObj.put(feeConfigDto.getFeeName(), "0");
                dataObj.put(feeConfigDto.getFeeName() + "_起止日期", "-");
                continue;
            }

            BigDecimal amount = new BigDecimal(tmpFeeManualCollectionDetailDto.getAmount());

            totalFee = totalFee.add(amount);

            dataObj.put(feeConfigDto.getFeeName(), tmpFeeManualCollectionDetailDto.getAmount());
            dataObj.put(feeConfigDto.getFeeName() + "_起止日期",
                    DateUtil.dateTimeToDate(tmpFeeManualCollectionDetailDto.getStartTime())
                            + "至" + DateUtil.dateTimeToDate(tmpFeeManualCollectionDetailDto.getEndTime()));
        }

        dataObj.put("应交款总额", totalFee.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());

        data.add(dataObj);

    }

    private FeeManualCollectionDetailDto hasFeeConfig(FeeConfigDto feeConfigDto, List<FeeManualCollectionDetailDto> feeManualCollectionDetailDtos) {

        for (FeeManualCollectionDetailDto feeManualCollectionDetailDto : feeManualCollectionDetailDtos) {
            if (feeManualCollectionDetailDto.getConfigId().equals(feeConfigDto.getConfigId())) {
                return feeManualCollectionDetailDto;
            }
        }

        return null;
    }

}
