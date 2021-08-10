package com.java110.common.bmo.assetImportLog.impl;

import com.java110.common.bmo.assetImportLog.IDeleteAssetImportLogBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IAssetImportLogInnerServiceSMO;
import com.java110.po.assetImportLog.AssetImportLogPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAssetImportLogBMOImpl")
public class DeleteAssetImportLogBMOImpl implements IDeleteAssetImportLogBMO {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    /**
     * @param assetImportLogPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(AssetImportLogPo assetImportLogPo) {

        int flag = assetImportLogInnerServiceSMOImpl.deleteAssetImportLog(assetImportLogPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
