package com.java110.common.bmo.hcGovTranslate.impl;

import com.java110.common.bmo.hcGovTranslate.IGetHcGovTranslateBMO;
import com.java110.intf.common.IHcGovTranslateInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.hcGovTranslate.HcGovTranslateDto;

import java.util.ArrayList;
import java.util.List;

@Service("getHcGovTranslateBMOImpl")
public class GetHcGovTranslateBMOImpl implements IGetHcGovTranslateBMO {

    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    /**
     *
     *
     * @param  hcGovTranslateDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(HcGovTranslateDto hcGovTranslateDto) {


        int count = hcGovTranslateInnerServiceSMOImpl.queryHcGovTranslatesCount(hcGovTranslateDto);

        List<HcGovTranslateDto> hcGovTranslateDtos = null;
        if (count > 0) {
            hcGovTranslateDtos = hcGovTranslateInnerServiceSMOImpl.queryHcGovTranslates(hcGovTranslateDto);
        } else {
            hcGovTranslateDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) hcGovTranslateDto.getRow()), count, hcGovTranslateDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
