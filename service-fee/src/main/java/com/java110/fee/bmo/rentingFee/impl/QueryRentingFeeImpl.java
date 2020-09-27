package com.java110.fee.bmo.rentingFee.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.store.StoreDto;
import com.java110.fee.bmo.rentingFee.IQueryRentingFee;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("queryRentingFeeImpl")
public class QueryRentingFeeImpl implements IQueryRentingFee {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> queryFees(FeeDto feeDto) {

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return ResultVo.createResponseEntity(new JSONArray());
        }

        List<String> storeIds = new ArrayList<>();
        for(FeeDto tmpFeeDto: feeDtos){
            storeIds.add(tmpFeeDto.getIncomeObjId());
        }

        if(storeIds.size() < 1){
            return ResultVo.createResponseEntity(feeDtos);
        }

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreIds(storeIds.toArray(new String[storeIds.size()]));
        List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);

        for(StoreDto tmpStoreDto: storeDtos){
            for(FeeDto tmpFeeDto: feeDtos){
                if(tmpStoreDto.getStoreId().equals(tmpFeeDto.getIncomeObjId())){
                    tmpFeeDto.setIncomeObjName(tmpStoreDto.getStoreName());
                }
            }
        }

        return ResultVo.createResponseEntity(feeDtos);
    }
}
