package com.java110.oa.bmo.oaWorkflow.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflow.IUpdateOaWorkflowBMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowBMOImpl")
public class UpdateOaWorkflowBMOImpl implements IUpdateOaWorkflowBMO {

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    /**
     * @param oaWorkflowPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(OaWorkflowPo oaWorkflowPo) {
        //只要已修改 就 状态刷为待部署
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        int flag = oaWorkflowInnerServiceSMOImpl.updateOaWorkflow(oaWorkflowPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
