package com.java110.oa.bmo.oaWorkflowForm.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowForm.IGetOaWorkflowFormBMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("getOaWorkflowFormBMOImpl")
public class GetOaWorkflowFormBMOImpl implements IGetOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    /**
     * @param oaWorkflowFormDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(OaWorkflowFormDto oaWorkflowFormDto) {


        int count = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormsCount(oaWorkflowFormDto);

        List<OaWorkflowFormDto> oaWorkflowFormDtos = null;
        if (count > 0) {
            oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        } else {
            oaWorkflowFormDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) oaWorkflowFormDto.getRow()), count, oaWorkflowFormDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * {"schemaVersion":1,"exporter":{"name":"form-js","version":"0.1.0"},
     * "components":[{"text":"# Text","type":"text"},{"key":"textfield2","label":"文本框","type":"textfield"},{"key":"number2","label":"Number","type":"number"},
     * {"key":"checkbox2","label":"Checkbox","type":"checkbox"},
     * {"key":"radio2","label":"Radio","type":"radio","values":[{"label":"Value","value":"value"}]},
     * {"key":"select2","label":"Select","type":"select","values":[{"label":"Value","value":"value"}]},
     * {"text":"# Text","type":"text"},{"key":"textarea1","label":"多行文本框","type":"textarea"},
     * {"key":"textdate1","label":"日期","type":"textdate"},
     * {"key":"textdatetime1","label":"时间","type":"textdatetime"},
     * {"action":"submit","key":"button1","label":"Button","type":"button"}],"type":"default"}
     * @param oaWorkflowFormDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryOaWorkflowFormData(OaWorkflowFormDto oaWorkflowFormDto) {
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);

        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        String formJson = oaWorkflowFormDtos.get(0).getFormJson();

        JSONObject formObj = JSONObject.parseObject(formJson);
        Map paramIn = new HashMap();

        int count = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDataCount(paramIn);

        List<Map> datas = null;
        if (count > 0) {
            datas = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDatas(paramIn);
        } else {
            datas = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) oaWorkflowFormDto.getRow()), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
