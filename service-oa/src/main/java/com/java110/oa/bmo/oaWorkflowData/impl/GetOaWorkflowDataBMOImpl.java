package com.java110.oa.bmo.oaWorkflowData.impl;

import com.java110.dto.oaWorkflow.OaWorkflowDataDto;
import com.java110.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowData.IGetOaWorkflowDataBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getOaWorkflowDataBMOImpl")
public class GetOaWorkflowDataBMOImpl implements IGetOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * @param oaWorkflowDataDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(OaWorkflowDataDto oaWorkflowDataDto) {


        int count = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatasCount(oaWorkflowDataDto);

        List<OaWorkflowDataDto> oaWorkflowDataDtos = null;
        if (count > 0) {
            oaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);
        } else {
            oaWorkflowDataDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) oaWorkflowDataDto.getRow()), count, oaWorkflowDataDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
