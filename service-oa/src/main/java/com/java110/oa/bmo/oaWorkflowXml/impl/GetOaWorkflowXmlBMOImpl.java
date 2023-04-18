package com.java110.oa.bmo.oaWorkflowXml.impl;

import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowXml.IGetOaWorkflowXmlBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getOaWorkflowXmlBMOImpl")
public class GetOaWorkflowXmlBMOImpl implements IGetOaWorkflowXmlBMO {

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    /**
     * @param oaWorkflowXmlDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(OaWorkflowXmlDto oaWorkflowXmlDto) {


        int count = oaWorkflowXmlInnerServiceSMOImpl.queryOaWorkflowXmlsCount(oaWorkflowXmlDto);

        List<OaWorkflowXmlDto> oaWorkflowXmlDtos = null;
        if (count > 0) {
            oaWorkflowXmlDtos = oaWorkflowXmlInnerServiceSMOImpl.queryOaWorkflowXmls(oaWorkflowXmlDto);
        } else {
            oaWorkflowXmlDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) oaWorkflowXmlDto.getRow()), count, oaWorkflowXmlDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
