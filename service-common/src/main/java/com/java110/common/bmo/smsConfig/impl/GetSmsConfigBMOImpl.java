package com.java110.common.bmo.smsConfig.impl;

import com.java110.common.bmo.smsConfig.IGetSmsConfigBMO;
import com.java110.dto.smsConfig.SmsConfigDto;
import com.java110.intf.common.ISmsConfigInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getSmsConfigBMOImpl")
public class GetSmsConfigBMOImpl implements IGetSmsConfigBMO {

    @Autowired
    private ISmsConfigInnerServiceSMO smsConfigInnerServiceSMOImpl;

    /**
     * @param smsConfigDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(SmsConfigDto smsConfigDto) {


        int count = smsConfigInnerServiceSMOImpl.querySmsConfigsCount(smsConfigDto);

        List<SmsConfigDto> smsConfigDtos = null;
        if (count > 0) {
            smsConfigDtos = smsConfigInnerServiceSMOImpl.querySmsConfigs(smsConfigDto);
        } else {
            smsConfigDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) smsConfigDto.getRow()), count, smsConfigDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
