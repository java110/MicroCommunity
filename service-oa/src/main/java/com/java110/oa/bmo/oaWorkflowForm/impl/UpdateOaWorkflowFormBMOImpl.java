package com.java110.oa.bmo.oaWorkflowForm.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowForm.IUpdateOaWorkflowFormBMO;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowFormBMOImpl")
public class UpdateOaWorkflowFormBMOImpl implements IUpdateOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    /**
     * @param oaWorkflowFormPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(OaWorkflowFormPo oaWorkflowFormPo) {

        int flag = oaWorkflowFormInnerServiceSMOImpl.updateOaWorkflowForm(oaWorkflowFormPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
