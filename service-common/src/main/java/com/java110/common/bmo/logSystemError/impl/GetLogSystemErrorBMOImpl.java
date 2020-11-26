package com.java110.common.bmo.logSystemError.impl;

import com.java110.common.bmo.logSystemError.IGetLogSystemErrorBMO;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getLogSystemErrorBMOImpl")
public class GetLogSystemErrorBMOImpl implements IGetLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * @param logSystemErrorDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(LogSystemErrorDto logSystemErrorDto) {


        int count = logSystemErrorInnerServiceSMOImpl.queryLogSystemErrorsCount(logSystemErrorDto);

        List<LogSystemErrorDto> logSystemErrorDtos = null;
        if (count > 0) {
            logSystemErrorDtos = logSystemErrorInnerServiceSMOImpl.queryLogSystemErrors(logSystemErrorDto);
        } else {
            logSystemErrorDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) logSystemErrorDto.getRow()), count, logSystemErrorDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
