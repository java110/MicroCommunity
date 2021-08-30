package com.java110.oa.bmo.oaWorkflowData.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowData.IUpdateOaWorkflowDataBMO;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowDataBMOImpl")
public class UpdateOaWorkflowDataBMOImpl implements IUpdateOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * @param oaWorkflowDataPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(OaWorkflowDataPo oaWorkflowDataPo) {

        int flag = oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
