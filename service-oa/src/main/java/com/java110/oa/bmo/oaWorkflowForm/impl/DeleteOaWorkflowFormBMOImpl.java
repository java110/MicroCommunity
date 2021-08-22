package com.java110.oa.bmo.oaWorkflowForm.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowForm.IDeleteOaWorkflowFormBMO;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOaWorkflowFormBMOImpl")
public class DeleteOaWorkflowFormBMOImpl implements IDeleteOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    /**
     * @param oaWorkflowFormPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(OaWorkflowFormPo oaWorkflowFormPo) {

        int flag = oaWorkflowFormInnerServiceSMOImpl.deleteOaWorkflowForm(oaWorkflowFormPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
