package com.java110.common.bmo.assetImportLogDetail.impl;

import com.java110.common.bmo.assetImportLogDetail.IDeleteAssetImportLogDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.java110.po.assetImportLogDetail.AssetImportLogDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAssetImportLogDetailBMOImpl")
public class DeleteAssetImportLogDetailBMOImpl implements IDeleteAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * @param assetImportLogDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AssetImportLogDetailPo assetImportLogDetailPo) {

        int flag = assetImportLogDetailInnerServiceSMOImpl.deleteAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
