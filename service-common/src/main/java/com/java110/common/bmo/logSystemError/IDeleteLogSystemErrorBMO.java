package com.java110.common.bmo.logSystemError;
import com.java110.po.logSystemError.LogSystemErrorPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteLogSystemErrorBMO {


    /**
     * 修改系统异常
     * add by wuxw
     * @param logSystemErrorPo
     * @return
     */
    ResponseEntity<String> delete(LogSystemErrorPo logSystemErrorPo);


}
