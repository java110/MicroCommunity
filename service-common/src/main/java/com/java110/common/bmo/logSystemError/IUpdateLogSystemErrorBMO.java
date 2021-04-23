package com.java110.common.bmo.logSystemError;
import com.java110.po.logSystemError.LogSystemErrorPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateLogSystemErrorBMO {


    /**
     * 修改系统异常
     * add by wuxw
     * @param logSystemErrorPo
     * @return
     */
    ResponseEntity<String> update(LogSystemErrorPo logSystemErrorPo);


}
