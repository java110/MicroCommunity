package com.java110.oa.bmo.oaWorkflowXml.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowXml.IDeleteOaWorkflowXmlBMO;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOaWorkflowXmlBMOImpl")
public class DeleteOaWorkflowXmlBMOImpl implements IDeleteOaWorkflowXmlBMO {

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    /**
     * @param oaWorkflowXmlPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(OaWorkflowXmlPo oaWorkflowXmlPo) {

        int flag = oaWorkflowXmlInnerServiceSMOImpl.deleteOaWorkflowXml(oaWorkflowXmlPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
