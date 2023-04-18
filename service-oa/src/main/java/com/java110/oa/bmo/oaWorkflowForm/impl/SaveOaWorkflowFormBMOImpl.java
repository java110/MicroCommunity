package com.java110.oa.bmo.oaWorkflowForm.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflow.OaWorkflowFormDto;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowForm.ISaveOaWorkflowFormBMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PinYinUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveOaWorkflowFormBMOImpl")
public class SaveOaWorkflowFormBMOImpl implements ISaveOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param oaWorkflowFormPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(OaWorkflowFormPo oaWorkflowFormPo) {
        //查询 流程存在不存在
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setFlowId(oaWorkflowFormPo.getFlowId());
        oaWorkflowDto.setStoreId(oaWorkflowFormPo.getStoreId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        oaWorkflowFormPo.setFormId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_formId));
        //设置版本
        oaWorkflowFormPo.setVersion(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        String tableName = PinYinUtil.getFirstSpell(oaWorkflowDtos.get(0).getFlowName() + oaWorkflowFormPo.getVersion());
        oaWorkflowFormPo.setTableName(OaWorkflowFormDto.TABLE_PRE + tableName);
        if (oaWorkflowFormPo.getTableName().length() > 60) { // 表名超长处理
            tableName = tableName.substring(tableName.length() - 30);
            oaWorkflowFormPo.setTableName(OaWorkflowFormDto.TABLE_PRE + tableName);
        }

        int flag = oaWorkflowFormInnerServiceSMOImpl.saveOaWorkflowForm(oaWorkflowFormPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        OaWorkflowPo oaWorkflowPo = new OaWorkflowPo();
        oaWorkflowPo.setFlowId(oaWorkflowFormPo.getFlowId());
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        oaWorkflowPo.setCurFormId(oaWorkflowFormPo.getFormId());
        flag = oaWorkflowInnerServiceSMOImpl.updateOaWorkflow(oaWorkflowPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
