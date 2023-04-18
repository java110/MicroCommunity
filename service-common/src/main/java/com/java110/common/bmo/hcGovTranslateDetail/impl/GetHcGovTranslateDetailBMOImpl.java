package com.java110.common.bmo.hcGovTranslateDetail.impl;

import com.java110.common.bmo.hcGovTranslateDetail.IGetHcGovTranslateDetailBMO;
import com.java110.intf.common.IHcGovTranslateDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.hcGovTranslate.HcGovTranslateDetailDto;

import java.util.ArrayList;
import java.util.List;

@Service("getHcGovTranslateDetailBMOImpl")
public class GetHcGovTranslateDetailBMOImpl implements IGetHcGovTranslateDetailBMO {

    @Autowired
    private IHcGovTranslateDetailInnerServiceSMO hcGovTranslateDetailInnerServiceSMOImpl;

    /**
     *
     *
     * @param  hcGovTranslateDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(HcGovTranslateDetailDto hcGovTranslateDetailDto) {


        int count = hcGovTranslateDetailInnerServiceSMOImpl.queryHcGovTranslateDetailsCount(hcGovTranslateDetailDto);

        List<HcGovTranslateDetailDto> hcGovTranslateDetailDtos = null;
        if (count > 0) {
            hcGovTranslateDetailDtos = hcGovTranslateDetailInnerServiceSMOImpl.queryHcGovTranslateDetails(hcGovTranslateDetailDto);
        } else {
            hcGovTranslateDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) hcGovTranslateDetailDto.getRow()), count, hcGovTranslateDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
