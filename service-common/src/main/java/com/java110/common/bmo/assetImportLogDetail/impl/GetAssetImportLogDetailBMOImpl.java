package com.java110.common.bmo.assetImportLogDetail.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.assetImportLogDetail.IGetAssetImportLogDetailBMO;
import com.java110.dto.log.AssetImportLogDetailDto;
import com.java110.dto.log.AssetImportLogTypeDto;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
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

        JSONArray datas = null;
        if (count > 0) {
            assetImportLogDetailDtos = assetImportLogDetailInnerServiceSMOImpl.queryAssetImportLogDetails(assetImportLogDetailDto);
            // todo 转换为jsonArray
            datas = covertToData(assetImportLogDetailDtos);
        } else {
            datas = new JSONArray();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) assetImportLogDetailDto.getRow()), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private JSONArray covertToData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        JSONArray datas = new JSONArray();

        if (assetImportLogDetailDtos == null || assetImportLogDetailDtos.size() < 1) {
            return datas;
        }

        JSONObject data = null;

        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {
            data = BeanConvertUtil.beanCovertJson(assetImportLogDetailDto);
            if (!StringUtil.isEmpty(assetImportLogDetailDto.getContent())) {
                data.putAll(JSONObject.parseObject(assetImportLogDetailDto.getContent()));
            }
            datas.add(data);
        }

        return datas;
    }

}
