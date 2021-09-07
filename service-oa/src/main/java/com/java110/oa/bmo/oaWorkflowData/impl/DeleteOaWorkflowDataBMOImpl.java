package com.java110.oa.bmo.oaWorkflowData.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowData.IDeleteOaWorkflowDataBMO;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOaWorkflowDataBMOImpl")
public class DeleteOaWorkflowDataBMOImpl implements IDeleteOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * @param oaWorkflowDataPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(OaWorkflowDataPo oaWorkflowDataPo) {

        int flag = oaWorkflowDataInnerServiceSMOImpl.deleteOaWorkflowData(oaWorkflowDataPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
