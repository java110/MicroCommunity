package com.java110.store.bmo.storeAds.impl;

import com.java110.dto.storeAds.StoreAdsDto;
import com.java110.intf.store.IStoreAdsInnerServiceSMO;
import com.java110.store.bmo.storeAds.IGetStoreAdsBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStoreAdsBMOImpl")
public class GetStoreAdsBMOImpl implements IGetStoreAdsBMO {

    @Autowired
    private IStoreAdsInnerServiceSMO storeAdsInnerServiceSMOImpl;

    /**
     * @param storeAdsDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StoreAdsDto storeAdsDto) {


        int count = storeAdsInnerServiceSMOImpl.queryStoreAdssCount(storeAdsDto);

        List<StoreAdsDto> storeAdsDtos = null;
        if (count > 0) {
            storeAdsDtos = storeAdsInnerServiceSMOImpl.queryStoreAdss(storeAdsDto);
            freshAdsUrl(storeAdsDtos);
        } else {
            storeAdsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) storeAdsDto.getRow()), count, storeAdsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshAdsUrl(List<StoreAdsDto> storeAdsDtos) {
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for (StoreAdsDto tmpStoreAdsDto : storeAdsDtos) {
            tmpStoreAdsDto.setUrl(imgUrl + tmpStoreAdsDto.getUrl());
        }
    }

}
