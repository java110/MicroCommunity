package com.java110.common.bmo.assetImportLog.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.assetImportLog.ISaveAssetImportLogBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.po.assetImportLog.AssetImportLogPo;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("saveAssetImportLogBMOImpl")
public class SaveAssetImportLogBMOImpl implements ISaveAssetImportLogBMO {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reqJson
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(JSONObject reqJson) {
        AssetImportLogPo assetImportLogPo = new AssetImportLogPo();
        assetImportLogPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        assetImportLogPo.setCommunityId(reqJson.getString("communityId"));
        assetImportLogPo.setErrorCount(reqJson.getString("errorCount"));
        assetImportLogPo.setLogType(reqJson.getString("logType"));
        assetImportLogPo.setSuccessCount(reqJson.getString("successCount"));
        int flag = assetImportLogInnerServiceSMOImpl.saveAssetImportLog(assetImportLogPo);

        if (flag < 1) {
            throw new IllegalArgumentException("保存 导入日志失败");
        }

        if (!reqJson.containsKey("assetImportLogDetailDtos")) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        JSONArray assetImportLogDetailDtos = reqJson.getJSONArray("assetImportLogDetailDtos");
        if (assetImportLogDetailDtos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        List<AssetImportLogDetailPo> assetImportLogDetailPos = new ArrayList<>();
        AssetImportLogDetailPo assetImportLogDetailPo = null;
        for (int detailIndex = 0; detailIndex < assetImportLogDetailDtos.size(); detailIndex++) {
            assetImportLogDetailPo = new AssetImportLogDetailPo();
            assetImportLogDetailPo.setCommunityId(assetImportLogPo.getCommunityId());
            assetImportLogDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            assetImportLogDetailPo.setLogId(assetImportLogPo.getLogId());
            assetImportLogDetailPo.setMessage(assetImportLogDetailDtos.getJSONObject(detailIndex).getString("message"));
            assetImportLogDetailPo.setObjName(assetImportLogDetailDtos.getJSONObject(detailIndex).getString("objName"));
            assetImportLogDetailPo.setState(assetImportLogDetailDtos.getJSONObject(detailIndex).getString("state"));
            assetImportLogDetailPos.add(assetImportLogDetailPo);
        }

        flag = assetImportLogDetailInnerServiceSMOImpl.saveAssetImportLogDetails(assetImportLogDetailPos);
        if (flag < 1) {
            throw new IllegalArgumentException("保存 导入日志失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
