package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.fee.bmo.IQueryParkspaceFee;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryParkspaceFeeImpl implements IQueryParkspaceFee {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(JSONObject reqJson) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("code"));

        JSONArray data = new JSONArray();
        List<OwnerCarDto> cars = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        for (OwnerCarDto carDto : cars) {
            dealOwnerCar(reqJson, carDto, data);
        }
        return ResultVo.createResponseEntity(1, data.size(), data);
    }

    /**
     * @param reqJson
     * @param carDto
     * @param data
     */
    private void dealOwnerCar(JSONObject reqJson, OwnerCarDto carDto, JSONArray data) {

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_PARKING_SPACE);
        feeDto.setPayerObjId(carDto.getPsId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        for (FeeDto tmpFeeDto : feeDtos) {
            dealFee(reqJson, carDto, data, tmpFeeDto);
        }
    }

    /**
     * 处理费用
     *
     * @param reqJson
     * @param carDto
     * @param data
     * @param tmpFeeDto
     */
    private void dealFee(JSONObject reqJson, OwnerCarDto carDto, JSONArray data, FeeDto tmpFeeDto) {
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(reqJson.getString("code"));
        feeDetailDto.setFeeId(tmpFeeDto.getFeeId());
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        for (FeeDetailDto tmpFeeDetailDto : feeDetailDtos) {
            dealFeeDetail(reqJson, carDto, data, tmpFeeDto, tmpFeeDetailDto);
        }
    }

    /**
     * 缴费明细
     *
     * @param reqJson
     * @param carDto
     * @param data
     * @param tmpFeeDto
     * @param tmpFeeDetailDto
     */
    private void dealFeeDetail(JSONObject reqJson, OwnerCarDto carDto, JSONArray data, FeeDto tmpFeeDto, FeeDetailDto tmpFeeDetailDto) {

        JSONObject dataObj = new JSONObject();
        dataObj.put("Code", reqJson.getString("code"));
        dataObj.put("serialNo", tmpFeeDetailDto.getDetailId());
        dataObj.put("order", tmpFeeDetailDto.getDetailId());
        dataObj.put("account", tmpFeeDto.getPayerObjId());
        dataObj.put("payTime", tmpFeeDetailDto.getCreateTime());
        dataObj.put("payTyte", "1");
        dataObj.put("fee", tmpFeeDetailDto.getReceivedAmount());
        dataObj.put("license", carDto.getCarNum());
        dataObj.put("carType", carDto.getCarType());
        dataObj.put("parkType", "1");
        dataObj.put("carPhoto", "");
        dataObj.put("update_time", tmpFeeDetailDto.getCreateTime());
        data.add(dataObj);
    }
}
