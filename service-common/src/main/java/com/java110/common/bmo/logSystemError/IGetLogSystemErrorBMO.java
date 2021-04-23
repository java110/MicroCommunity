package com.java110.common.bmo.logSystemError;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import org.springframework.http.ResponseEntity;
public interface IGetLogSystemErrorBMO {


    /**
     * 查询系统异常
     * add by wuxw
     * @param  logSystemErrorDto
     * @return
     */
    ResponseEntity<String> get(LogSystemErrorDto logSystemErrorDto);


}
