package com.java110.common.bmo.assetImportLogDetail.impl;

import com.java110.common.bmo.assetImportLogDetail.IUpdateAssetImportLogDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAssetImportLogDetailBMOImpl")
public class UpdateAssetImportLogDetailBMOImpl implements IUpdateAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * @param assetImportLogDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(AssetImportLogDetailPo assetImportLogDetailPo) {

        int flag = assetImportLogDetailInnerServiceSMOImpl.updateAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
