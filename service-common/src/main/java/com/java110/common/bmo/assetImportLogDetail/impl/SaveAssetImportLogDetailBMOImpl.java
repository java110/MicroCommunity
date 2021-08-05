package com.java110.common.bmo.assetImportLogDetail.impl;

import com.java110.common.bmo.assetImportLogDetail.ISaveAssetImportLogDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAssetImportLogDetailBMOImpl")
public class SaveAssetImportLogDetailBMOImpl implements ISaveAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param assetImportLogDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AssetImportLogDetailPo assetImportLogDetailPo) {

        assetImportLogDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = assetImportLogDetailInnerServiceSMOImpl.saveAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
