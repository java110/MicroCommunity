package com.java110.common.bmo.hcGovTranslateDetail.impl;

import com.java110.common.bmo.hcGovTranslateDetail.IDeleteHcGovTranslateDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IHcGovTranslateDetailInnerServiceSMO;
import com.java110.po.hcGovTranslateDetail.HcGovTranslateDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteHcGovTranslateDetailBMOImpl")
public class DeleteHcGovTranslateDetailBMOImpl implements IDeleteHcGovTranslateDetailBMO {

    @Autowired
    private IHcGovTranslateDetailInnerServiceSMO hcGovTranslateDetailInnerServiceSMOImpl;

    /**
     * @param hcGovTranslateDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(HcGovTranslateDetailPo hcGovTranslateDetailPo) {

        int flag = hcGovTranslateDetailInnerServiceSMOImpl.deleteHcGovTranslateDetail(hcGovTranslateDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
