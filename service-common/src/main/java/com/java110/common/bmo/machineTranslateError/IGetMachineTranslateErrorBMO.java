package com.java110.common.bmo.machineTranslateError;

import com.java110.dto.machine.MachineTranslateErrorDto;
import org.springframework.http.ResponseEntity;

public interface IGetMachineTranslateErrorBMO {


    /**
     * 查询IOT同步错误日志记录
     * add by wuxw
     *
     * @param machineTranslateErrorDto
     * @return
     */
    ResponseEntity<String> get(MachineTranslateErrorDto machineTranslateErrorDto);


}
