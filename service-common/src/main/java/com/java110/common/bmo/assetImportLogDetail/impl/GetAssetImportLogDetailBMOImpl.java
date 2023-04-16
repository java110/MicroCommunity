package com.java110.common.bmo.assetImportLogDetail.impl;

import com.java110.common.bmo.assetImportLogDetail.IGetAssetImportLogDetailBMO;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAssetImportLogDetailBMOImpl")
public class GetAssetImportLogDetailBMOImpl implements IGetAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * @param assetImportLogDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(AssetImportLogDetailDto assetImportLogDetailDto) {


        int count = assetImportLogDetailInnerServiceSMOImpl.queryAssetImportLogDetailsCount(assetImportLogDetailDto);

        List<AssetImportLogDetailDto> assetImportLogDetailDtos = null;
        if (count > 0) {
            assetImportLogDetailDtos = assetImportLogDetailInnerServiceSMOImpl.queryAssetImportLogDetails(assetImportLogDetailDto);
        } else {
            assetImportLogDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) assetImportLogDetailDto.getRow()), count, assetImportLogDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
